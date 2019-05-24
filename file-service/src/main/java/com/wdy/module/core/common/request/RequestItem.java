package com.wdy.module.core.common.request;

import lombok.Data;

@Data
public class RequestItem {
    private String query;
    private String queryString;

    public RequestItem() {
    }

    public RequestItem(String query, String queryString) {
        this.query = query;
        this.queryString = queryString;
    }
}
