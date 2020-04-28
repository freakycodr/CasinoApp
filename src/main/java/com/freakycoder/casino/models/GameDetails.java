package com.freakycoder.casino.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDetails {
    private Long gameId;
    private Long dealerId;
    private Date startTime;
    private String status;
    private Long thrownNumber;
}
