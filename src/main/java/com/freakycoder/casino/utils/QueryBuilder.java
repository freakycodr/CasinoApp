package com.freakycoder.casino.utils;


import com.freakycoder.casino.models.Queries;

public class QueryBuilder {
    private String id;
    private String comment;
    private String sql;

    public QueryBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public QueryBuilder setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public QueryBuilder setSql(String sql) {
        this.sql = sql;
        return this;
    }

    public Queries create() {
        return new Queries(id, comment, sql);
    }
}
