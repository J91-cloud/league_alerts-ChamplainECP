package com.calerts.computer_alertsbe.authorsubdomain.presentationlayer;

import com.calerts.computer_alertsbe.authorsubdomain.datalayer.AuthorIdentifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@Builder
//@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponseModel {

    private String authorId;
    //    private Username username;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String biography;
//    private ArticleList articles;
//    private Password password;
}
