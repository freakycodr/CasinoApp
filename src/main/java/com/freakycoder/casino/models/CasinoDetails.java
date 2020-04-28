package com.freakycoder.casino.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CasinoDetails {
    private Long casinoId;
    private String casinoName;
    private Double balanceAmount;
    private Boolean status;
}
