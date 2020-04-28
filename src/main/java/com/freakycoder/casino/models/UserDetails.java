package com.freakycoder.casino.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetails {
    private Long userId;
    private String userName;
    private Boolean status;
}
