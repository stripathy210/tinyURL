package com.tinyurl.urlshortener.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortUrlEntity {
    private String url;
    private String hitCnt;

    public ShortUrlEntity(String url, String hitCnt) {
        this.url = url;
        this.hitCnt = hitCnt;
    }
}