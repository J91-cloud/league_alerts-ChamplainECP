package com.calerts.computer_alertsbe.authorsubdomain.datalayer;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
}
