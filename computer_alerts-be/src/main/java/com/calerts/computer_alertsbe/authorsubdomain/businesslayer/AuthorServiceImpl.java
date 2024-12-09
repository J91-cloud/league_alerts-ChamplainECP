package com.calerts.computer_alertsbe.authorsubdomain.businesslayer;

import com.calerts.computer_alertsbe.authorsubdomain.datalayer.AuthorRepository;
import com.calerts.computer_alertsbe.authorsubdomain.presentationlayer.AuthorResponseModel;
import com.calerts.computer_alertsbe.utils.EntityModelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AuthorServiceImpl implements AuthorService{

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Flux<AuthorResponseModel> getAllAuthors() {
        return authorRepository.findAll()
                .map(EntityModelUtil::toAuthorResponseModel)
                .doOnError(e -> log.error("Error retrieving authors", e));
    }
}
