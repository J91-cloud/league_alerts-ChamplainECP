package com.calerts.computer_alertsbe.readersubdomain.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReaderRequestModel {
    private String readerId;
    private String emailAddress;
//    private String auth0UserID;
    private String address;

    private String firstName;
    private String lastName;
}
