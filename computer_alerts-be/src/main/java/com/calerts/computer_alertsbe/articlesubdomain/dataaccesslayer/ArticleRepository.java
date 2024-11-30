package com.calerts.computer_alertsbe.articlesubdomain.dataaccesslayer;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ArticleRepository extends ReactiveMongoRepository<Article, String> {
        Mono<Article> findArticleByArticleId(String articleId);

        Flux<Article> findAllArticleByTags(String tagName);

}