package com.calerts.computer_alertsbe.articlesubdomain.businesslayer;


import com.calerts.computer_alertsbe.articlesubdomain.dataaccesslayer.Article;
import com.calerts.computer_alertsbe.articlesubdomain.dataaccesslayer.ArticleRepository;

import com.calerts.computer_alertsbe.articlesubdomain.dataaccesslayer.ArticleStatus;

import com.calerts.computer_alertsbe.articlesubdomain.presentationlayer.ArticleRequestModel;
import com.calerts.computer_alertsbe.articlesubdomain.presentationlayer.ArticleResponseModel;
import com.calerts.computer_alertsbe.utils.CloudinaryService.CloudinaryService;
import com.calerts.computer_alertsbe.articlesubdomain.subscription.SubscriptionRepository;
import com.calerts.computer_alertsbe.emailingsubdomain.EmailSenderService;
import com.calerts.computer_alertsbe.utils.EntityModelUtil;
import com.calerts.computer_alertsbe.utils.exceptions.BadRequestException;
import com.calerts.computer_alertsbe.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import java.time.LocalDateTime;

import static com.calerts.computer_alertsbe.articlesubdomain.dataaccesslayer.Content.calculateWordCount;


@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);


    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public Flux<ArticleResponseModel> getAllArticles() {
        return articleRepository.findAll().map(EntityModelUtil::toArticleResponseModel);
    }

    @Override
    public Flux<ArticleResponseModel> getAllArticleForSpecificSport(String category) {
        return articleRepository.findAllArticleByCategory(category)
                .map(EntityModelUtil::toArticleResponseModel);

    }

    @Override
    public Mono<ArticleResponseModel> getArticleByArticleId(String articleId) {
        return articleRepository.findArticleByArticleIdentifier_ArticleId(articleId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException("No article with this id was found " + articleId))))
                .map(EntityModelUtil::toArticleResponseModel);
    }

    @Override
    public Mono<ArticleResponseModel> editArticle(String articleId, Mono<ArticleRequestModel> articleRequestModel) {
        return articleRepository.findArticleByArticleIdentifier_ArticleId(articleId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException("No article with this id was found " + articleId))))
                .flatMap(foundArticle -> articleRequestModel
                        .map(EntityModelUtil::toArticleEntity)
                        .doOnNext(

                                article -> {
                                    article.setRequestCount(foundArticle.getRequestCount());
                                    article.setLikeCount(foundArticle.getLikeCount());
                                    article.setArticleIdentifier(foundArticle.getArticleIdentifier());
                                    article.setArticleStatus(foundArticle.getArticleStatus());
                                    article.setId(foundArticle.getId());
                                })
                )
                .flatMap(article -> {
                    int wordCount = calculateWordCount(article.getBody());
                    article.setWordCount(wordCount);
                    return articleRepository.save(article);

                })
                .map(EntityModelUtil::toArticleResponseModel);

    }


    @Override
    public Mono<Void> requestCount(String articleId) {
        return articleRepository.findArticleByArticleIdentifier_ArticleId(articleId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException("article id was not found: " + articleId))))
                .flatMap(article -> {
                    Integer currentCount = article.getRequestCount() != null ? article.getRequestCount() : 0;
                    article.setRequestCount(currentCount + 1);
                    return articleRepository.save(article).then(); // Save and complete
                });
    }


    @Scheduled(cron = "0 0 0 */30 * *")
    public Mono<Void> resetRequestCounts() {
        return articleRepository.findAll()
                .flatMap(article -> {
                    article.setRequestCount(0);
                    return articleRepository.save(article);
                })
                .then();
    }



    @Override
    public Mono<ArticleResponseModel> createArticle(Mono<ArticleRequestModel> articleRequestModel) {
        return articleRequestModel
                .filter(article -> article.getTitle() != null && !article.getTitle().isEmpty())
                .switchIfEmpty(Mono.error(new BadRequestException("Article title must not be empty")))
                .filter(article -> article.getWordCount() != 0 && article.getWordCount() > 112)
                .switchIfEmpty(Mono.error(new BadRequestException("Article must have a valid word count")))
                .map(EntityModelUtil::toArticleEntity)
                .map(article -> {
                    article.setArticleStatus(ArticleStatus.ARTICLE_REVIEW);
                    article.setRequestCount(0);
                    return article;
                })
                .flatMap(articleRepository::save)
                .map(EntityModelUtil::toArticleResponseModel);

    }

    @Override
    public Mono<Void> acceptArticle(String articleId) {
        return articleRepository.findArticleByArticleIdentifier_ArticleId(articleId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException("article id was not found: " + articleId))))
                .flatMap(article -> {
                    article.setArticleStatus(ArticleStatus.PUBLISHED);
                    return articleRepository.save(article)
                            .then(notifySubscribers(article));
                });
    }



    @Override
    public Mono<ArticleResponseModel> createArticleDraft(Mono<ArticleRequestModel> articleRequestModel) {
        return articleRequestModel
                .filter(article -> article.getTitle() != null && !article.getTitle().isEmpty())
                .switchIfEmpty(Mono.error(new BadRequestException("Article title must not be empty")))
                .filter(article -> article.getWordCount() != 0 && article.getWordCount() > 112)
                .switchIfEmpty(Mono.error(new BadRequestException("Article must have a valid word count")))
                .map(EntityModelUtil::toArticleEntity)
                .map(article -> {
                    article.setArticleStatus(ArticleStatus.DRAFT);
                    article.setRequestCount(0);
                    return article;
                })
                .flatMap(articleRepository::save)
                .map(EntityModelUtil::toArticleResponseModel);
    }

    private String buildEmailContent(Article article, String userEmail) {
        return String.format(
                "<div style='font-family: Arial, sans-serif; line-height: 1.5; max-width: 600px; margin: auto; border: 1px solid #ddd; padding: 20px; border-radius: 8px;'>"
                        + "<h1 style='color: #a4050a; text-align: center;'>New Article Published!</h1>"
                        + "<p style='color: #555;'>Hello,</p>"
                        + "<p style='color: #555;'>A new article titled <strong>'%s'</strong> has been published in the <strong>%s</strong> category.</p>"
                        + "<p style='margin-top: 20px; text-align: center;'><a href='%s' style='display: inline-block; padding: 10px 15px; background-color: #a4050a; color: white; text-decoration: none; border-radius: 5px;'>Read Now</a></p>"
                        + "<p style='margin-top: 20px; color: #555;'>Follow us on social media:</p>"
                        + "<div style='text-align: center;'>"
                        + "  <a href='https://instagram.com/LeagueAlerts' style='color: #3067f2; text-decoration: none; margin-right: 10px;'>Instagram</a>"
                        + "  | <a href='https://x.com/LeagueAlerts' style='color: #3067f2; text-decoration: none; margin-left: 10px;'>X</a>"
                        + "</div>"
                        + "<p style='margin-top: 20px; color: #555;'>Best regards,<br>LeagueAlerts Team</p>"
                        // Unsubscribe link section:
                        + "<hr style='margin-top: 20px; border: none; border-top: 1px solid #ccc;' />"
                        + "<p style='font-size: 12px; color: #999; text-align: center;'>"
                        + "If you wish to unsubscribe from <strong>%s</strong> notifications, please "
                        + "<a href='https://league-alerts.web.app/unsubscribe?email=%s&category=%s' style='color: #a4050a; text-decoration: underline;'>click here</a>."
                        + "</p>"
                        + "</div>",
                article.getTitle(),
                article.getCategory(),
                "https://league-alerts.web.app/articles/" + article.getArticleIdentifier().getArticleId(),
                article.getCategory(), // used for unsubscribe text
                userEmail,             // subscriber email for unsubscribe link
                article.getCategory()  // category for unsubscribe link
        );
    }




    private Mono<Void> notifySubscribers(Article article) {
        return subscriptionRepository.findByCategory(article.getCategory().getCategoryName())
                .flatMap(subscription -> {
                    // Pass the subscriber's email so the unsubscribe link is properly built.
                    String emailBody = buildEmailContent(article, subscription.getUserEmail());
                    return emailSenderService.sendEmail(
                                    subscription.getUserEmail(),
                                    "New Article in " + article.getCategory(),
                                    emailBody)
                            .doOnSuccess(result ->
                                    log.info("Email sent successfully to {}", subscription.getUserEmail()))
                            .doOnError(error ->
                                    log.error("Failed to send email to {}: {}", subscription.getUserEmail(), error.getMessage()));
                })
                .then();
    }



    @Override
    public Mono<List<ArticleResponseModel>> searchArticles(String category, String query) {
        return articleRepository
                .findByCategoryContainingAndTitleContainingIgnoreCase(category, query)
                .map(EntityModelUtil::toArticleResponseModel)
                .collectList();

    }


    @Override
    public Mono<String> updateArticleImage(String articleId, FilePart filePart) {
        return articleRepository.findArticleByArticleIdentifier_ArticleId(articleId)
                .switchIfEmpty(Mono.error(new NotFoundException("No article found with ID: " + articleId)))
                .flatMap(article -> cloudinaryService.uploadImage(filePart)
                        .flatMap(imageUrl -> {
                            article.setPhotoUrl(imageUrl); // Update the article's photo URL
                            return articleRepository.save(article).thenReturn(imageUrl); // Save and return the URL
                        })
                );
    }

    @Override
    public Mono<String> uploadImage(FilePart filePart) {
        return cloudinaryService.uploadImage(filePart)
                .switchIfEmpty(Mono.error(new NotFoundException("No image found")));
    }

    public static int calculateWordCount(String body) {
        if (body == null || body.trim().isEmpty()) {
            return 0;
        }
        return body.trim().split("\\s+").length; // Split by whitespace and count
    }


}
