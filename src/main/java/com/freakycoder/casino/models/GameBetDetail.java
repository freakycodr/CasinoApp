package com.freakycoder.casino.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GameBetDetail {
    private Long betId;
    private Long gameId;
    private Integer betNumber;
    private Double betAmount;
    private Date betTime;
    @JsonIgnore
    private Long userId;
}
