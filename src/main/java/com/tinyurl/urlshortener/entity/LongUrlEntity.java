package com.tinyurl.urlshortener.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
public class LongUrlEntity {
    private String baseURL;
    private String longURL;
    private String clientId;
    private AtomicLong cnt;

    public LongUrlEntity(String longURL, String clientId, String baseURL) {
        this.longURL = longURL;
        this.clientId = clientId;
        this.baseURL = baseURL;
        cnt = new AtomicLong(0l);
    }

    @Override
    public int hashCode() {
        return longURL.hashCode() + clientId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LongUrlEntity)) {
            return false;
        }
        LongUrlEntity entity = (LongUrlEntity) o;
        return longURL.equals(entity.longURL)
                && clientId.equals(entity.clientId);
    }
}
