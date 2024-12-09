package com.calerts.computer_alertsbe.authorsubdomain.businesslayer;

import com.calerts.computer_alertsbe.authorsubdomain.presentationlayer.AuthorResponseModel;
import reactor.core.publisher.Flux;

public interface AuthorService {

    Flux<AuthorResponseModel> getAllAuthors();
}
