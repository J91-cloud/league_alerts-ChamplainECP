package com.calerts.computer_alertsbe.AuthDomain.presentationLayer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDTO {
    private String email;
    private String password;
    private String connection;

}