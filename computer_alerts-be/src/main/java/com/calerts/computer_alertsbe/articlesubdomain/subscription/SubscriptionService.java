package com.calerts.computer_alertsbe.articlesubdomain.subscription;


import reactor.core.publisher.Mono;

import java.util.List;

public interface SubscriptionService {
    void subscribe(String userEmail, String category);
    void unsubscribe(String userEmail, String category);
    Mono<Boolean> isAlreadySubscribed(String userEmail, String category);
    List<String> getSubscribedCategories(String userEmail);
    List<String> getSubscribersByCategory(String category);
}

