package com.tinyurl.urlshortener.util;

import com.tinyurl.urlshortener.entity.LongUrlEntity;
import com.tinyurl.urlshortener.exceptions.TinyUrlError;
import com.tinyurl.urlshortener.exceptions.TinyUrlException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UrlValidator {
    public static void validateURL(String shortUrl) throws TinyUrlException {
        if (shortUrl == null || shortUrl.length()  == 0 ) {
            throw new TinyUrlException(TinyUrlError.INVALID_REQ_ERROR);
        }

    }

    public static List<String> getUrl (LongUrlEntity longUrlEntity) throws TinyUrlException {
        if (longUrlEntity == null || longUrlEntity.getLongURL() == null || longUrlEntity.getClientId() == null) {
            throw new TinyUrlException(TinyUrlError.INVALID_REQ_ERROR);
        }

        if (longUrlEntity.getLongURL().length() == 0 || longUrlEntity.getClientId() == null) {
            throw new TinyUrlException(TinyUrlError.INVALID_REQ_ERROR);
        }
        return getBaseUrl(longUrlEntity.getLongURL());
    }

    private static List<String> getBaseUrl(String urlStr) throws TinyUrlException{
        List<String> list = new ArrayList<>();
        try {
            URL url = new URL(urlStr);
            String protocol = url.getProtocol() != null ? url.getProtocol() : "";
            protocol = protocol +"://" + url.getHost();
            list.add(protocol);
            list.add(url.getFile());
        } catch (MalformedURLException e) {
            throw new TinyUrlException(TinyUrlError.INVALID_REQ_ERROR);
        }
        return list;
    }
}
