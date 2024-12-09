package com.calerts.computer_alertsbe.authorsubdomain.datalayer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AuthorIdentifier {

    private String authorId;

    public AuthorIdentifier() {
        this.authorId = UUID.randomUUID().toString();
    }
}
