package com.calerts.computer_alertsbe.articleinteractionsubdomain.presentationlayer;

import com.calerts.computer_alertsbe.articleinteractionsubdomain.dataaccesslayer.*;
import com.calerts.computer_alertsbe.articlesubdomain.dataaccesslayer.Article;
import com.calerts.computer_alertsbe.articlesubdomain.dataaccesslayer.ArticleIdentifier;
import com.calerts.computer_alertsbe.articlesubdomain.dataaccesslayer.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test") // Activates the test profile
@AutoConfigureWebTestClient
class InteractionControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private SaveRepository saveRepository;

    private final String BASE_URL = "/api/v1/interactions";

    @BeforeEach
    public void setUp() {
        likeRepository.deleteAll().block();
        commentRepository.deleteAll().block();
        articleRepository.deleteAll().block();
        articleRepository.save(Article.builder()
                .articleIdentifier(new ArticleIdentifier("article-1"))
                .title("Article 1")
                .build()).block();
        saveRepository.deleteAll().block();
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenGetLikesByArticle_thenReturnAllLikes() {
        // Arrange
        var articleId = new ArticleIdentifier("article-1");

        var like1 = Like.builder()
                .likeIdentifier(new LikeIdentifier())
                .articleIdentifier(articleId)
                .readerId("reader-001")
                .timestamp(LocalDateTime.now())
                .build();

        var like2 = Like.builder()
                .likeIdentifier(new LikeIdentifier())
                .articleIdentifier(articleId)
                .readerId("reader-002")
                .timestamp(LocalDateTime.now())
                .build();

        var like3 = Like.builder()
                .likeIdentifier(new LikeIdentifier())
                .articleIdentifier(new ArticleIdentifier("article-2"))
                .readerId("reader-003")
                .timestamp(LocalDateTime.now())
                .build();

        likeRepository.saveAll(List.of(like1, like2, like3)).blockLast();

        String url = BASE_URL + "/likes/article/" + articleId.getArticleId();

        // Act & Assert
        webTestClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(LikeResponseModel.class)
                .value((response) -> {
                    assertNotNull(response);
                    assertEquals(2, response.size());
                    response.forEach(like -> assertEquals(articleId.getArticleId(), like.getArticleId()));
                });
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenGetLikesByReader_thenReturnAllLikes() {
        // Arrange
        var readerId = "reader-001";

        var like1 = Like.builder()
                .likeIdentifier(new LikeIdentifier())
                .articleIdentifier(new ArticleIdentifier("article-1"))
                .readerId(readerId)
                .timestamp(LocalDateTime.now())
                .build();

        var like2 = Like.builder()
                .likeIdentifier(new LikeIdentifier())
                .articleIdentifier(new ArticleIdentifier("article-2"))
                .readerId(readerId)
                .timestamp(LocalDateTime.now())
                .build();

        var like3 = Like.builder()
                .likeIdentifier(new LikeIdentifier())
                .articleIdentifier(new ArticleIdentifier("article-3"))
                .readerId("reader-002")
                .timestamp(LocalDateTime.now())
                .build();

        likeRepository.saveAll(List.of(like1, like2, like3)).blockLast();

        String url = BASE_URL + "/likes/reader/" + readerId;

        // Act & Assert
        webTestClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(LikeResponseModel.class)
                .value((response) -> {
                    assertNotNull(response);
                    assertEquals(2, response.size());
                    response.forEach(like -> assertEquals(readerId, like.getReaderId()));
                });
    }


    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenLikeArticle_thenReturnCreatedLike() {
        // Arrange
        var articleId = new ArticleIdentifier("article-1");
        var readerId = "reader-001";

        String url = BASE_URL + "/like?articleId=" + articleId + "&readerId=" + readerId;

        // Act & Assert
        webTestClient.post()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(LikeResponseModel.class)
                .value((response) -> {
                    assertNotNull(response);
                    assertEquals(articleId.toString(), response.getArticleId());
                    assertEquals(readerId, response.getReaderId());
                });
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenUnlikeArticle_thenReturnNoContent() {
        // Arrange
        var articleId = new ArticleIdentifier("article-1");
        var readerId = "06a7d573-bcab-4db3-956f-773324b92a80";

        var like = Like.builder()
                .likeIdentifier(new LikeIdentifier())
                .articleIdentifier(articleId)
                .readerId(readerId)
                .timestamp(LocalDateTime.now())
                .build();

        likeRepository.save(like).block();

        String url = BASE_URL + "/unlike?articleId=" + articleId + "&readerId=" + readerId;

        // Act & Assert

        webTestClient.delete()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();


    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenGetLikeByIdentifier_thenReturnLike() {
        // Arrange
        var articleId = new ArticleIdentifier("article-1");
        var readerId = "06a7d573-bcab-4db3-956f-773324b92a80";

        var like = Like.builder()
                .likeIdentifier(new LikeIdentifier())
                .articleIdentifier(articleId)
                .readerId(readerId)
                .timestamp(LocalDateTime.now())
                .build();

        likeRepository.save(like).block();

        String url = BASE_URL + "/likes/" + like.getLikeIdentifier().getLikeId();

        // Act & Assert
        webTestClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(LikeResponseModel.class)
                .value((response) -> {
                    assertNotNull(response);
                    assertEquals(like.getLikeIdentifier().getLikeId(), response.getLikeId());
                    assertEquals(articleId.getArticleId(), response.getArticleId());
                    assertEquals(readerId, response.getReaderId());
                });
    }

    // Positive test case for getAllComments
//    @Test
//    @WithMockUser(username = "testuser", roles = {"USER"})
//    public void whenGetAllComments_thenReturnAllComments() {
//        // Arrange
//        var articleId = new ReaderIdentifier("article-1");
//
//        var comment1 = Comment.builder()
//                .commentId(new CommentIdentifier())
//                .content("This is a comment")
//                .wordCount(4)
//                .timestamp(LocalDateTime.now())
//                .articleId(articleId)
//                .readerId("06a7d573-bcab-4db3-956f-773324b92a80")
//                .readerId("reader-001")
//
//                .build();
//
//        var comment2 = Comment.builder()
//                .commentId(new CommentIdentifier())
//                .content("This is another comment")
//                .wordCount(4)
//                .timestamp(LocalDateTime.now())
//                .articleId(articleId)
//
//                .readerId("06a7d573-bcab-4db3-956f-773324b92a80")
//
//                .readerId("reader-002")
//
//                .build();
//
//        var comment3 = Comment.builder()
//                .commentId(new CommentIdentifier())
//                .content("This is a third comment")
//                .wordCount(4)
//                .timestamp(LocalDateTime.now())
//                .articleId(articleId)
//
//                .readerId("06a7d573-bcab-4db3-956f-773324b92a80")
//
//                .readerId("reader-003")
//
//                .build();
//
//        commentRepository.saveAll(List.of(comment1, comment2, comment3)).blockLast();
//
//        String url = BASE_URL + "/comments";
//
//        // Act & Assert
//        webTestClient.get()
//                .uri(url)
//                .accept(MediaType.TEXT_EVENT_STREAM)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType("text/event-stream;charset=UTF-8")
//                .expectBodyList(CommentResponseModel.class)
//                .value((response) -> {
//                    assertNotNull(response);
//                    assertEquals(3, response.size());
//                    response.forEach(comment -> assertEquals(articleId.getArticleId(), comment.getArticleId()));
//                });
//    }

//     //Positive test case for addComment
//    @Test
//    @WithMockUser(username = "testuser", roles = {"USER"})
//    public void whenAddComment_thenReturnNothing() {
//        // Arrange
//        CommentRequestModel commentRequestModel = CommentRequestModel.builder()
//                .content("This is a comment")
//                .articleId("article-1")
//
//                .readerId("06a7d573-bcab-4db3-956f-773324b92a80")
//
//                .readerId("reader-001")
//
//                .build();
//
//        String url = BASE_URL + "/comments";
//
//        // Act & Assert
//        webTestClient
//                .post()
//                .uri(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(commentRequestModel), CommentRequestModel.class)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectHeader();
//    }


    // Negative test case for addComment
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenAddCommentWithEmptyContent_thenReturnBadRequest() {
        // Arrange
        CommentRequestModel commentRequestModel = CommentRequestModel.builder()
                .content("")
                .articleId("e09e8812-32fb-434d-908f-40d5e3b137ca")
                .readerId("06a7d573-bcab-4db3-956f-773324b92a80")
                .build();

        String url = BASE_URL + "/comments";

        // Act & Assert
        webTestClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(commentRequestModel), CommentRequestModel.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(String.class)
                .value((response) -> {
                    assertNotNull(response);
                    assertTrue(response.contains("Comment content cannot be empty"));
                });
    }

    // Negative test case for addComment
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenAddCommentWithInvalidArticleId_thenReturnNotFound() {
        // Arrange
        CommentRequestModel commentRequestModel = CommentRequestModel.builder()
                .content("This is a comment")
                .articleId("invalid-article-id")
                .readerId("reader-001")
                .build();

        String url = BASE_URL + "/comments";

        // Act & Assert
        webTestClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(commentRequestModel), CommentRequestModel.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(String.class)
                .value((response) -> {
                    assertNotNull(response);
                    assertTrue(response.contains("Article id was not found: invalid-article-id"));
                });
    }

    // Negative test case for addComment
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenAddCommentWithExceedingWordCount_thenReturnBadRequest() {
        // Arrange
        CommentRequestModel commentRequestModel = CommentRequestModel.builder()
                .content("This is a comment with more than 50 words. Apple mountain laptop freedom universe " +
                        "lighthouse keyboard ocean guitar willow cloud chocolate forest puzzle eagle rainbow sunset " +
                        "journey memory adventure snowflake volcano window dream notebook river camera compass heart " +
                        "horizon butterfly rocket whisper symphony puzzle treasure star galaxy secret flame oasis " +
                        "harmony meadow turtle dolphin book breeze lantern melody anchor mountain.")
                .articleId("e09e8812-32fb-434d-908f-40d5e3b137ca")
                .readerId("reader-001")
                .build();

        String url = BASE_URL + "/comments";

        // Act & Assert
        webTestClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(commentRequestModel), CommentRequestModel.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(String.class)
                .value((response) -> {
                    assertNotNull(response);
                    assertTrue(response.contains("Comment exceeds 50 words."));
                });
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenGetSharesByArticle_thenReturnAllShares() {
        // Arrange
        var articleId = new ArticleIdentifier("article-1");

        var share1 = Share.builder()
                .shareIdentifier(new ShareIdentifier())
                .articleIdentifier(articleId)
                .readerId("reader-001")
                .timestamp(LocalDateTime.now())
                .build();

        var share2 = Share.builder()
                .shareIdentifier(new ShareIdentifier())
                .articleIdentifier(articleId)
                .readerId("reader-002")
                .timestamp(LocalDateTime.now())
                .build();

        var share3 = Share.builder()
                .shareIdentifier(new ShareIdentifier())
                .articleIdentifier(new ArticleIdentifier("article-2"))
                .readerId("reader-003")
                .timestamp(LocalDateTime.now())
                .build();

        shareRepository.saveAll(List.of(share1, share2, share3)).blockLast();

        String url = BASE_URL + "/shares/article/" + articleId.getArticleId();

        // Act & Assert
        webTestClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ShareResponseModel.class)
                .value((response) -> {
                    assertNotNull(response);
                    assertEquals(2, response.size());
                    response.forEach(share -> assertEquals(articleId.getArticleId(), share.getArticleId()));
                });
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenShareArticle_thenReturnCreatedShare() {
        // Arrange
        var articleId = new ArticleIdentifier("article-1");
        var readerId = "reader-001";

        String url = BASE_URL + "/share?articleId=" + articleId + "&readerId=" + readerId;

        // Act & Assert
        webTestClient.post()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ShareResponseModel.class)
                .value((response) -> {
                    assertNotNull(response);
                    assertEquals(articleId.toString(), response.getArticleId());
                    assertEquals(readerId, response.getReaderId());
                });
    }

    // Positive test case for getSavesByReader
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenGetSavesByReader_thenReturnAllSaves() {
        // Arrange
        var readerId = "06a7d573-bcab-4db3-956f-773324b92a80";

        var save1 = Save.builder()
                .saveId(new SaveIdentifier())
                .articleId(new ArticleIdentifier("article-1"))
                .readerId(readerId)
                .timestamp(LocalDateTime.now())
                .build();

        var save2 = Save.builder()
                .saveId(new SaveIdentifier())
                .articleId(new ArticleIdentifier("article-2"))
                .readerId(readerId)
                .timestamp(LocalDateTime.now())
                .build();

        saveRepository.save(save1).block();
        saveRepository.save(save2).block();

        String url = BASE_URL + "/saves/" + readerId;

        // Act & Assert
        webTestClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(SaveResponseModel.class)
                .value((response) -> {
                    assertNotNull(response);
//                    assertEquals(2, response.size());
                    response.forEach(save -> assertEquals(readerId, save.getReaderId()));
                });
    }

    // Positive test case for addSave
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenAddSave_thenReturnNothing() {
        // Arrange
        SaveRequestModel saveRequestModel = SaveRequestModel.builder()
                .articleId("article-1")
                .readerId("reader-001")
                .build();

        String url = BASE_URL + "/saves";

        // Act & Assert
        webTestClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(saveRequestModel), SaveRequestModel.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader();
    }

    // Negative test case for addSave
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenAddSaveWithDuplicateSave_thenReturnConflict() {
        // Arrange
        SaveRequestModel saveRequestModel = SaveRequestModel.builder()
                .articleId("article-1")
                .readerId("reader-001")
                .build();

        var save = Save.builder()
                .saveId(new SaveIdentifier())
                .articleId(new ArticleIdentifier("article-1"))
                .readerId("reader-001")
                .timestamp(LocalDateTime.now())
                .build();

        saveRepository.save(save).block();

        String url = BASE_URL + "/saves";

        // Act & Assert
        webTestClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(saveRequestModel), SaveRequestModel.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody(String.class)
                .value((response) -> {
                    assertNotNull(response);
                    assertTrue(response.contains("Article is already saved. Article id: article-1"));
                });
    }

    // Negative test case for addSave
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenAddSaveWithInvalidArticleId_thenReturnNotFound() {
        // Arrange
        SaveRequestModel saveRequestModel = SaveRequestModel.builder()
                .articleId("xxx")
                .readerId("reader-001")
                .build();

        String url = BASE_URL + "/saves";

        // Act & Assert
        webTestClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(saveRequestModel), SaveRequestModel.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value((response) -> {
                    assertNotNull(response);
                    assertTrue(response.contains("Article id not found: xxx"));
                });
    }

    // Positive test case for deleteSave
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenDeleteSave_thenReturnNoContent() {
        // Arrange
        var saveId = new SaveIdentifier();

        var save = Save.builder()
                .saveId(saveId)
                .articleId(new ArticleIdentifier("article-1"))
                .readerId("reader-001")
                .timestamp(LocalDateTime.now())
                .build();

        saveRepository.save(save).block();

        String url = BASE_URL + "/saves/" + saveId.getSaveId();

        // Act & Assert
        webTestClient.delete()
                .uri(url)
                .exchange()
                .expectStatus().isNoContent();
    }

    // Negative test case for deleteSave
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void whenDeleteSaveWithInvalidSaveId_thenReturnNotFound() {
        // Arrange
        String url = BASE_URL + "/saves/invalid-save-id";

        // Act & Assert
        webTestClient.delete()
                .uri(url)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value((response) -> {
                    assertNotNull(response);
                    assertTrue(response.contains("Save id not found: invalid-save-id"));
                });
    }
}
