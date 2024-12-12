package com.calerts.computer_alertsbe.articleinteractionsubdomain.presentationlayer;

import com.calerts.computer_alertsbe.articleinteractionsubdomain.businesslayer.LikeService;
import com.calerts.computer_alertsbe.utils.EntityModelUtil;
import com.calerts.computer_alertsbe.articleinteractionsubdomain.presentationlayer.LikeResponseModel;
import com.calerts.computer_alertsbe.articlesubdomain.dataaccesslayer.ArticleIdentifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/interactions")
public class InteractionController {

    private final LikeService likeService;

    public InteractionController(LikeService likeService) {
        this.likeService = likeService;
    }


    @PostMapping("/like")
    public Mono<ResponseEntity<LikeResponseModel>> likeArticle(
            @RequestParam String articleId,
            @RequestParam String readerId) {

        ArticleIdentifier articleIdentifier = new ArticleIdentifier(articleId);
        return likeService.likeArticle(articleIdentifier, readerId)
                .map(EntityModelUtil::toLikeResponseModel)
                .map(responseModel -> ResponseEntity.status(HttpStatus.CREATED).body(responseModel));
    }


    @GetMapping("/likes/article/{articleId}")
    public Mono<ResponseEntity<List<LikeResponseModel>>> getLikesByArticle(@PathVariable String articleId) {
        ArticleIdentifier identifier = new ArticleIdentifier(articleId);
        return likeService.getLikesByArticle(identifier)
                .map(EntityModelUtil::toLikeResponseModel)
                .collectList() // Collect the Flux into a List
                .map(ResponseEntity::ok);
    }

    @GetMapping("/likes/reader/{readerId}")
    public Mono<ResponseEntity<List<LikeResponseModel>>> getLikesByReader(@PathVariable String readerId) {
        return likeService.getLikesByReader(readerId)
                .map(EntityModelUtil::toLikeResponseModel)
                .collectList()
                .map(likes -> {
                    if (likes.isEmpty()) {
                        return ResponseEntity.noContent().build();
                    }
                    return ResponseEntity.ok(likes);
                });
    }

    @GetMapping("/likes/{likeId}")
    public Mono<ResponseEntity<LikeResponseModel>> getLikeByIdentifier(@PathVariable String likeId) {
        return likeService.getLikeByIdentifier(likeId)
                .map(EntityModelUtil::toLikeResponseModel)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/unlike")
    public Mono<ResponseEntity<Void>> unlikeArticle(
            @RequestParam String articleId,
            @RequestParam String readerId) {

        ArticleIdentifier articleIdentifier = new ArticleIdentifier(articleId);
        return likeService.unlikeArticle(articleIdentifier, readerId)
                .then(Mono.just(ResponseEntity.noContent().build())); // Return 204 No Content on success
    }
}