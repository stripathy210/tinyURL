package com.tinyurl.urlshortener.service;

import com.tinyurl.urlshortener.bl.URLConverterIf;
import com.tinyurl.urlshortener.entity.LongUrlEntity;
import com.tinyurl.urlshortener.exceptions.TinyUrlException;
import com.tinyurl.urlshortener.util.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tinyURL")
public class TinyURLService {

    @Autowired
    private URLConverterIf urlConverter;

    @GetMapping("/{shortURL}")
    public String getLongURL(@PathVariable("shortURL") String shortURL) {
        try {
            UrlValidator.validateURL(shortURL);
            String longUrl = urlConverter.getLongURL(shortURL);

            return longUrl;
        } catch (TinyUrlException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/{shortURL}/hitCount")
    public Long getHitCount(@PathVariable("shortURL") String shortURL) {
        try {
            return urlConverter.getHitCnt(shortURL);
        } catch (TinyUrlException e) {
            return 0l;
        }
    }

    @PostMapping
    public String getShortenedURL(@RequestBody LongUrlEntity longUrlEntity) {
        List<String> list ;
        try {
            list = UrlValidator.getUrl(longUrlEntity);
            String shortURL = urlConverter.getShortURL(new LongUrlEntity(list.get(1),longUrlEntity.getClientId(),list.get(0)));
            return list.get(0) + "/" + shortURL;
        } catch (TinyUrlException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping
    public void flush() {
        urlConverter.flush();
    }
}