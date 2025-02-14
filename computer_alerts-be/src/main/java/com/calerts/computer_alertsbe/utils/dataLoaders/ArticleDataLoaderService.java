//package com.calerts.computer_alertsbe.utils.dataLoaders;
//
//import com.calerts.computer_alertsbe.articlesubdomain.dataaccesslayer.*;
//import com.calerts.computer_alertsbe.articlesubdomain.dataaccesslayer.Categories.Categories;
//import com.calerts.computer_alertsbe.articlesubdomain.dataaccesslayer.Tags.Tags;
//import com.calerts.computer_alertsbe.authorsubdomain.datalayer.AuthorIdentifier;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//
//import java.time.ZonedDateTime;
//
//@Service
//public class ArticleDataLoaderService implements CommandLineRunner {
//
//    @Autowired
//    private ArticleRepository articleRepository;
//
//    @Autowired
//    private ContentRepository contentRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        // NBA article
//        Content nbaContent1 = Content.builder()
//                .title("The NBA is Back!")
//                .body("\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\"\n")
//                .build();
//
//        Article nbaArticle1 = Article.builder()
//                .articleIdentifier(new ArticleIdentifier("e09e8812-32fb-434d-908f-40d5e3b137ca"))
//                .title(nbaContent1.getTitle())
//                .body(nbaContent1.getBody())
//                .requestCount(21)
//                .wordCount(Content.calculateWordCount(nbaContent1.getBody()))
//                .articleStatus(ArticleStatus.PUBLISHED)
//                .category(Categories.builder().categoryName("NBA").build())
//                .likeCount(3)
//                .shareCount(35)
//                .tagsTag(Tags.builder().tagName("NBA").build())
//                .timePosted(ZonedDateTime.now().toLocalDateTime())
//                .authorIdentifier(new AuthorIdentifier("3b63de68-9161-4925-b38b-e686dd88f848")) // Nicky Dupont
//                .photoUrl("https://res.cloudinary.com/ddihej6gw/image/upload/v1733944103/pexels-pixabay-71103_p1ungr.jpg")
//                .articleDescription("An in-depth exploration of key principles and practical approaches to balancing professional performance and personal well-being.")
//                .build();
//
//        // NBA article
//        Content nbaContent2 = Content.builder()
//                .title("NBA Playoff")
//                .body("\"The National Basketball Association (NBA) is more competitive than ever, with teams striving to outdo each other in every game. Superstars like LeBron James, Kevin Durant, and Stephen Curry continue to dazzle fans with their incredible performances. This season, the playoff race is heating up, with every win and loss carrying significant weight.\"\n")
//                .build();
//
//        Article nbaArticle2 = Article.builder()
//                .articleIdentifier(new ArticleIdentifier("806d087b-e708-4293-925b-40b2972741e5"))
//                .title(nbaContent2.getTitle())
//                .body(nbaContent2.getBody())
//                .wordCount(Content.calculateWordCount(nbaContent2.getBody()))
//                .articleStatus(ArticleStatus.PUBLISHED)
//                .category(Categories.builder().categoryName("NBA").build())
//                .likeCount(2)
//                .shareCount(5)
//                .requestCount(8)
//                .tagsTag(Tags.builder().tagName("NBA").build())
//                .timePosted(ZonedDateTime.now().toLocalDateTime())
//                .authorIdentifier(new AuthorIdentifier("3b63de68-9161-4925-b38b-e686dd88f848")) // Nicky Dupont
//                .photoUrl("https://res.cloudinary.com/ddihej6gw/image/upload/v1733944101/pexels-corleone-brown-2930373-4500123_zcgbae.jpg")
//                .articleDescription("An overview of the competitive dynamics of the NBA, featuring key superstars and the intensifying playoff race.")
//                .build();
//
//        // NHL article
//        Content nhlContent1 = Content.builder()
//                .title("What is Happening in the NHL?")
//                .body("\"The National Hockey League (NHL) is in the spotlight as teams battle for playoff positions. The season has been full of thrilling moments, from jaw-dropping goals to spectacular saves. With rivalries reignited and new stars emerging, hockey fans have plenty to cheer for. The race for the Stanley Cup promises to be more intense than ever.\"\n")
//                .build();
//
//        Article nhlArticle1 = Article.builder()
//                .articleIdentifier(new ArticleIdentifier("1f9e4567-8901-4b5c-a23d-4567890123ef"))
//                .title(nhlContent1.getTitle())
//                .body(nhlContent1.getBody())
//                .wordCount(Content.calculateWordCount(nhlContent1.getBody()))
//                .articleStatus(ArticleStatus.PUBLISHED)
//                .category(Categories.builder().categoryName("NHL").build())
//                .tagsTag(Tags.builder().tagName("NHL").build())
//                .likeCount(1)
//                .shareCount(3)
//                .requestCount(1)
//                .timePosted(ZonedDateTime.now().toLocalDateTime())
//                .photoUrl("https://res.cloudinary.com/ddihej6gw/image/upload/v1733944094/pexels-bylukemiller-13978862_sm4ynn.jpg")
//                .authorIdentifier(new AuthorIdentifier("7e93579d-cf40-44a0-9b82-6f8b05f3185b")) // George Smith
//                .articleDescription("An exciting NHL season filled with rivalries, emerging stars, and spectacular plays sets the stage for an intense race to the Stanley Cup.")
//                .build();
//
//        // NHL article
//        Content nhlContent2 = Content.builder()
//                .title("NHL Season Update")
//                .body("\"Hockey fans around the world are celebrating the NHL’s most exciting season yet. Teams are pushing their limits as they chase the coveted Stanley Cup. Players like Connor McDavid and Auston Matthews are delivering MVP-caliber performances, while goaltenders are showcasing their skills in highlight-reel fashion. The passion of hockey is alive and well.\"\n")
//                .build();
//
//        Article nhlArticle2 = Article.builder()
//                .articleIdentifier(new ArticleIdentifier("ca1d0478-6a9c-421b-b815-84965e3c7b4a"))
//                .title(nhlContent2.getTitle())
//                .body(nhlContent2.getBody())
//                .wordCount(Content.calculateWordCount(nhlContent2.getBody()))
//                .articleStatus(ArticleStatus.PUBLISHED)
//                .category(Categories.builder().categoryName("NHL").build())
//                .tagsTag(Tags.builder().tagName("NHL").build())
//                .likeCount(6)
//                .shareCount(1)
//                .requestCount(4)
//                .timePosted(ZonedDateTime.now().toLocalDateTime())
//                .photoUrl("https://res.cloudinary.com/ddihej6gw/image/upload/v1733944091/pexels-introspectivedsgn-7783413_r7s5xx.jpg")
//                .authorIdentifier(new AuthorIdentifier("5a87fa4e-f2a4-4874-b7f6-c22c29decb97")) // Michael Jones
//                .articleDescription("The NHL's thrilling season showcases MVP performances, highlight-reel saves, and teams fiercely competing for the Stanley Cup.")
//                .build();
//
//        // NFL article
//        Content nflContent1 = Content.builder()
//                .title("What's New in the NFL?")
//                .body("\"The National Football League (NFL) is in full swing, with teams vying for playoff spots and showcasing incredible talent. Star quarterbacks like Patrick Mahomes and Josh Allen are delivering jaw-dropping performances, while defensive units are stepping up in clutch moments. The road to the Super Bowl is filled with surprises, making this season one of the most exciting in recent memory.\"\n")
//                .build();
//
//        Content nflContent2 = Content.builder()
//                .title("NFL Season Highlights")
//                .body("\"The NFL season is thrilling, with star quarterbacks and dominant defenses making the road to the Super Bowl unforgettable. From game-winning drives to game-changing interceptions, every week brings new excitement and drama. The battle for playoff spots is heating up, and fans can't get enough of the action.\"\n")
//                .build();
//
//        Article nflArticle1 = Article.builder()
//                .articleIdentifier(new ArticleIdentifier("2a3b4c5d-6e7f-8g9h-0i1j-2k3l4m5n6o7p"))
//                .title(nflContent1.getTitle())
//                .body(nflContent1.getBody())
//                .wordCount(Content.calculateWordCount(nflContent1.getBody()))
//                .articleStatus(ArticleStatus.PUBLISHED)
//                .category(Categories.builder().categoryName("NFL").build())
//                .likeCount(1)
//                .shareCount(17)
//                .requestCount(18)
//                .tagsTag(Tags.builder().tagName("NFL").build())
//                .timePosted(ZonedDateTime.now().toLocalDateTime())
//                .photoUrl("https://res.cloudinary.com/ddihej6gw/image/upload/v1733944084/pexels-pixabay-47331_qhejgo.jpg")
//                .authorIdentifier(new AuthorIdentifier("6d2bb1f9-0c4f-4691-a6f5-5599e7f2068c")) // Lily Williams
//                .articleDescription("The NFL season is thrilling, with star quarterbacks and dominant defenses making the road to the Super Bowl unforgettable.")
//                .build();
//
//        // NFL article
//        Article nflArticle2 = Article.builder()
//                .articleIdentifier(new ArticleIdentifier("3d4e5f6g-7h8i-9j0k-1l2m-3n4o5p6q7r8s"))
//                .title(nflContent2.getTitle())
//                .body(nflContent2.getBody())
//                .wordCount(Content.calculateWordCount(nflContent2.getBody()))
//                .articleStatus(ArticleStatus.PUBLISHED)
//                .category(Categories.builder().categoryName("NFL").build())
//                .likeCount(9)  // Added missing dot before likeCount
//                .shareCount(9)
//                .requestCount(9)
//                .tagsTag(Tags.builder().tagName("NFL").build())
//                .timePosted(ZonedDateTime.now().toLocalDateTime())
//                .photoUrl("https://res.cloudinary.com/ddihej6gw/image/upload/v1733944081/pexels-pixabay-160577_tf74ef.jpg")
//                .authorIdentifier(new AuthorIdentifier("1c35b82c-d9a3-4f8f-a0a6-bde7a5096017")) // Thomas Clark
//                .articleDescription("The NFL season captivates fans with thrilling comebacks, intense battles for division supremacy, and breakout stars redefining the game.")
//                .build();
//
//        // UFC article
//        Content ufcContent1 = Content.builder()
//                .title("UFC News")
//                .body("\"The Ultimate Fighting Championship (UFC) continues to captivate audiences with its electrifying matchups. From jaw-dropping knockouts to masterful submissions, fighters are leaving it all in the octagon. Champions like Israel Adesanya and Amanda Nunes are cementing their legacies, while rising stars are making their mark. The anticipation for upcoming fights has never been higher.\"\n")
//                .build();
//
//        Article ufcArticle1 = Article.builder()
//                .articleIdentifier(new ArticleIdentifier("4e5f6g7h-8i9j-0k1l-2m3n-4o5p6q7r8s9t"))
//                .title(ufcContent1.getTitle())
//                .body(ufcContent1.getBody())
//                .wordCount(Content.calculateWordCount(ufcContent1.getBody()))
//                .articleStatus(ArticleStatus.PUBLISHED)
//                .category(Categories.builder().categoryName("UFC").build())
//                .tagsTag(Tags.builder().tagName("UFC").build())
//                .likeCount(2)
//                .shareCount(2)
//                .requestCount(9)
//                .timePosted(ZonedDateTime.now().toLocalDateTime())
//                .photoUrl("https://res.cloudinary.com/ddihej6gw/image/upload/v1733944134/pexels-brunogobofoto-2204179_byfjyn.jpg")
//                .authorIdentifier(new AuthorIdentifier("9a21d4f8-85fa-477b-bc0a-76819b2d7c8f")) // Kevin Lee
//                .articleDescription("The article covers the excitement of UFC, highlighting thrilling fights, champions like Israel Adesanya and Amanda Nunes, and the rise of new stars.")
//                .build();
//
//        // UFC article
//        Content ufcContent2 = Content.builder()
//                .title("UFC Fight Night")
//                .body("\"The UFC's octagon is the stage for some of the most intense rivalries in combat sports. Fighters like Conor McGregor and Kamaru Usman continue to deliver unforgettable moments, while thrilling undercards showcase the depth of talent in the promotion. With upcoming events promising blockbuster main events, fans are in for a wild ride this season.\"\n")
//                .build();
//
//        Article ufcArticle2 = Article.builder()
//                .articleIdentifier(new ArticleIdentifier("5f6g7h8i-9j0k-1l2m-3n4o-5p6q7r8s9t0u"))
//                .title(ufcContent2.getTitle())
//                .body(ufcContent2.getBody())
//                .requestCount(12)
//                .wordCount(Content.calculateWordCount(ufcContent2.getBody()))
//                .articleStatus(ArticleStatus.PUBLISHED)
//                .category(Categories.builder().categoryName("UFC").build())
//                .tagsTag(Tags.builder().tagName("UFC").build())
//                .likeCount(10)
//                .shareCount(13)
//                .timePosted(ZonedDateTime.now().toLocalDateTime())
//                .authorIdentifier(new AuthorIdentifier("8f5be0c1-0fc1-4cfb-b7b8-fc46e221d31e")) // Nancy Davis
//                .photoUrl("https://res.cloudinary.com/ddihej6gw/image/upload/v1733944132/pexels-pixabay-163403_xjkj1n.jpg")
//                .articleDescription("The UFC's octagon is the setting for intense rivalries, unforgettable moments, and blockbuster main events that keep fans on the edge of their seats.")
//                .build();
//
//        // MLB article
//        Content mlbContent1 = Content.builder()
//                .title("MLB Season Update")
//                .body("\"The Major League Baseball (MLB) season has been a rollercoaster of emotions for fans. With players like Shohei Ohtani and Aaron Judge redefining excellence on the field, every game offers unforgettable moments. Teams are battling for playoff spots, and the intensity of the pennant races is unmatched. The postseason is set to deliver even more drama and excitement.\"\n")
//                .build();
//
//        Article mlbArticle1 = Article.builder()
//                .articleIdentifier(new ArticleIdentifier("6g7h8i9j-0k1l-2m3n-4o5p-6q7r8s9t0u1v"))
//                .title(mlbContent1.getTitle())
//                .body(mlbContent1.getBody())
//                .wordCount(Content.calculateWordCount(mlbContent1.getBody()))
//                .articleStatus(ArticleStatus.PUBLISHED)
//                .requestCount(3)
//                .category(Categories.builder().categoryName("MLB").build())
//                .tagsTag(Tags.builder().tagName("MLB").build())
//                .likeCount(13)
//                .shareCount(30)
//                .timePosted(ZonedDateTime.now().toLocalDateTime())
//                .photoUrl("https://res.cloudinary.com/ddihej6gw/image/upload/v1733944130/pexels-glauco-moquete-1697414982-27899035_ynojsd.jpg")
//                .authorIdentifier(new AuthorIdentifier("1b71f87d-d5c7-47c2-90e5-0e11b83ed978")) // Elizabeth Brown
//                .articleDescription("The MLB season is a rollercoaster of emotions, with stars like Shohei Ohtani and Aaron Judge delivering unforgettable moments on the field.")
//                .build();
//
//        // MLB article
//        Content mlbContent2 = Content.builder()
//                .title("MLB Playoff")
//                .body("\"The MLB season continues to thrill fans with its mix of power hitting, stellar pitching, and dramatic finishes. Rising stars and veteran players alike are showcasing their skills in a season full of surprises. As the postseason looms, every game becomes a must-watch as teams fight to keep their championship dreams alive.\"\n")
//                .build();
//
//        Article mlbArticle2 = Article.builder()
//                .articleIdentifier(new ArticleIdentifier("7h8i9j0k-1l2m-3n4o-5p6q-7r8s9t0u1v2w"))
//                .title(mlbContent2.getTitle())
//                .body(mlbContent2.getBody())
//                .requestCount(9)
//                .wordCount(Content.calculateWordCount(mlbContent2.getBody()))
//                .articleStatus(ArticleStatus.PUBLISHED)
//                .category(Categories.builder().categoryName("MLB").build())
//                .likeCount(12)
//                .shareCount(9)
//                .tagsTag(Tags.builder().tagName("MLB").build())
//                .timePosted(ZonedDateTime.now().toLocalDateTime())
//                .photoUrl("https://res.cloudinary.com/ddihej6gw/image/upload/v1733944128/pexels-courtney-garner-585522281-17061702_nts9s1.jpg")
//                .authorIdentifier(new AuthorIdentifier("2d87fa3e-a1b1-4b7d-b5b6-c33982a8728c")) // Rebecca Martin
//                .articleDescription("The MLB season is full of power hitting, stellar pitching, and dramatic finishes, with rising stars and veteran players delivering unforgettable moments.")
//                .build();
//
//        // Inserting Articles
//        Flux.just(nbaArticle1, nbaArticle2,
//                        nhlArticle1, nhlArticle2,
//                        nflArticle1, nflArticle2,
//                        ufcArticle1, ufcArticle2,
//                        mlbArticle1, mlbArticle2)
//                .flatMap(articleRepository::insert)
//                .log()
//                .subscribe();
//
//        // Inserting Contents
//        Flux.just(nbaContent1, nbaContent2,
//                        nhlContent1, nhlContent2,
//                        nflContent1, nflContent2,
//                        ufcContent1, ufcContent2,
//                        mlbContent1, mlbContent2)
//                .flatMap(contentRepository::insert)
//                .log()
//                .subscribe();
//    }
//}
