package com.calerts.computer_alertsbe.articlesubdomain.subscription;

import com.calerts.computer_alertsbe.emailingsubdomain.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final EmailSenderService emailSenderService;

    private static Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, EmailSenderService emailSenderService) {
        this.subscriptionRepository = subscriptionRepository;
        this.emailSenderService = emailSenderService;
    }

    @Override
    public void subscribe(String userEmail, String category) {
        subscriptionRepository.existsByUserEmailAndCategory(userEmail, category)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalStateException("User is already subscribed to this category."));
                    }
                    Subscription subscription = new Subscription(userEmail, category);
                    return subscriptionRepository.save(subscription)
                            .doOnSuccess(sub -> sendSubscriptionConfirmationEmail(userEmail, category));
                })
                .subscribe();
    }


    private void sendSubscriptionConfirmationEmail(String userEmail, String category) {
        String body = "<div style='font-family: Arial, sans-serif; line-height: 1.5; max-width: 600px; margin: auto; "
                + "border: 1px solid #ddd; padding: 20px; border-radius: 8px;'>"
                + "<h1 style='color: #a4050a; text-align: center;'>Subscription Confirmation</h1>"
                + "<p style='color: #555;'>Hello,</p>"
                + "<p style='color: #555;'>You've successfully subscribed to updates for <strong>" + category + "</strong> on LeagueAlerts!</p>"
                + "<p style='margin-top: 20px; color: #555;'>Stay updated with the latest news and updates directly in your inbox!</p>"
                + "<p style='margin-top: 20px; color: #555;'>Follow us on social media:</p>"
                + "<div style='text-align: center;'>"
                + "  <a href='https://instagram.com/LeagueAlerts' style='color: #3067f2; text-decoration: none; margin-right: 10px;'>Instagram</a>"
                + "  | <a href='https://x.com/LeagueAlerts' style='color: #3067f2; text-decoration: none; margin-left: 10px;'>X</a>"
                + "</div>"
                + "<p style='margin-top: 20px; color: #555;'>Best regards,<br>LeagueAlerts Team</p>"

                // A horizontal rule to separate the main content from the unsubscribe link:
                + "<hr style='margin-top: 20px; border: none; border-top: 1px solid #ccc;' />"

                + "<p style='font-size: 12px; color: #999; text-align: center; margin-top: 20px;'>"
                + "If you wish to unsubscribe from <strong>" + category + "</strong> updates, please "
//                + "<a href='https://league-alerts.web.app/unsubscribe?email=" + userEmail + "&category=" + category
                + "<a href='https://league-alerts.web.app/unsubscribe?email=" + userEmail + "&category=" + category

                + "' style='color: #a4050a; text-decoration: underline;'>click here</a>."
                + "</p>"
                + "</div>";

        emailSenderService.sendEmail(userEmail, "Subscription Confirmation", body).subscribe();
    }





    @Override
    public void unsubscribe(String userEmail, String category) {
        subscriptionRepository.deleteByUserEmailAndCategory(userEmail, category);
    }

    @Override
    public Mono<Boolean> isAlreadySubscribed(String userEmail, String category) {
        return subscriptionRepository.existsByUserEmailAndCategory(userEmail, category)
                .defaultIfEmpty(false);  // Provide a default value if no result is found
    }


    @Override
    public List<String> getSubscribedCategories(String userEmail) {
        return subscriptionRepository.findByUserEmail(userEmail)
                .toStream()
                .map(Subscription::getCategory)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getSubscribersByCategory(String category) {
        return subscriptionRepository.findByCategory(category)
                .toStream()
                .map(Subscription::getUserEmail)
                .collect(Collectors.toList());
    }
}

