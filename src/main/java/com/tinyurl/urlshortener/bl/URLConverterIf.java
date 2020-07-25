package com.tinyurl.urlshortener.bl;

import com.tinyurl.urlshortener.entity.LongUrlEntity;
import com.tinyurl.urlshortener.exceptions.TinyUrlException;

public interface URLConverterIf {

    String getLongURL(String shortUrl) throws TinyUrlException;
    Long getHitCnt(String shortUrl) throws TinyUrlException;
    String getShortURL(LongUrlEntity longUrlEntity) throws TinyUrlException;
    void flush();

}
