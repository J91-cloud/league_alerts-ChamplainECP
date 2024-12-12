package com.calerts.computer_alertsbe.articleinteractionsubdomain.dataaccesslayer;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter
@Embeddable
public class LikeIdentifier {


    private String likeId;

    public LikeIdentifier() {
        this.likeId = UUID.randomUUID().toString();
    }

}