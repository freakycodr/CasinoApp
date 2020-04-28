package com.freakycoder.casino.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DealerDetails {
    private Long dealerId;
    private String dealerName;
    private Long casinoId;
    private Boolean status;
}
