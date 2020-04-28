package com.freakycoder.casino.models;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Queries {
    private String id;
    private String comment;
    private String sql;
}
