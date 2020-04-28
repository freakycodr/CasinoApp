package com.freakycoder.casino.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BettableGameDetails {
    private Long gameId;
    private Date gameStartTime;
    private Long gameDealerId;
    private String gameDealerName;
}
