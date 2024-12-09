package com.calerts.computer_alertsbe.authorsubdomain.datalayer;

import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "authors")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    @Id
    private String id;

    @Embedded
    private AuthorIdentifier authorIdentifier;
//    private Username username;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private Biography biography;
//    private ArticleList articles;
//    private Password password;
}
