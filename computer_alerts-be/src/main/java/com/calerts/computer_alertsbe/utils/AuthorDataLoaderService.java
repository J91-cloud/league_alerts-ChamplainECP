package com.calerts.computer_alertsbe.utils;

import com.calerts.computer_alertsbe.authorsubdomain.datalayer.Author;
import com.calerts.computer_alertsbe.authorsubdomain.datalayer.AuthorIdentifier;
import com.calerts.computer_alertsbe.authorsubdomain.datalayer.AuthorRepository;
import com.calerts.computer_alertsbe.authorsubdomain.datalayer.Biography;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuthorDataLoaderService implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public void run(String... args) throws Exception {

        Author author1 = Author.builder()
                .authorIdentifier(new AuthorIdentifier("3b63de68-9161-4925-b38b-e686dd88f848"))
                .emailAddress("dupont.nicky@email.com")
                .firstName("Nicky")
                .lastName("Dupont")
                .biography(new Biography("Nicky Dupont is a NBA enthusiast and met Micheal Jordan when he just started out."))
                .build();

        Author author2 = Author.builder()
                .authorIdentifier(new AuthorIdentifier("2b23d817-acc3-494f-bdeb-28a002308188"))
                .emailAddress("chan.jack@email.com")
                .firstName("Jack")
                .lastName("Chan")
                .biography(new Biography())
                .build();

        Flux.just(author1, author2)
                .flatMap(s -> authorRepository.insert(Mono.just(s))
                        .log(s.toString()))
                .subscribe();

    }
}
