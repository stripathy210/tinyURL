package com.tinyurl.urlshortener.bl;

import com.tinyurl.urlshortener.entity.LongUrlEntity;
import com.tinyurl.urlshortener.exceptions.TinyUrlError;
import com.tinyurl.urlshortener.exceptions.TinyUrlException;
import com.tinyurl.urlshortener.util.Base62EncoderCodec;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class URLConverterEngine implements URLConverterIf {

    private ConcurrentHashMap<LongUrlEntity, String> longToShortURLMap;
    private ConcurrentHashMap<String, LongUrlEntity> shortToLongEntityMap;

    private long randomSeedValue = 99999999999999l;
    private ReentrantLock reentrantLock = new ReentrantLock();

    public URLConverterEngine() {
        longToShortURLMap = new ConcurrentHashMap<>();
        shortToLongEntityMap = new ConcurrentHashMap<>();
    }

    @Override
    public String getLongURL(String shortUrl) throws TinyUrlException {
        if (shortToLongEntityMap.containsKey(shortUrl)) {
            LongUrlEntity urlKey = shortToLongEntityMap.get(shortUrl);
            urlKey.getCnt().incrementAndGet();
            return urlKey.getBaseURL()+urlKey.getLongURL();
        }

        throw new TinyUrlException(TinyUrlError.NO_SHORT_URL_FOUND_ERROR);
    }

    @Override
    public Long getHitCnt(String shortUrl) throws TinyUrlException {
        if (shortToLongEntityMap.containsKey(shortUrl)) {
            return shortToLongEntityMap.get(shortUrl).getCnt().get();
        }

        throw new TinyUrlException(TinyUrlError.NO_SHORT_URL_FOUND_ERROR);
    }

    @Override
    public String getShortURL(LongUrlEntity longUrlEntity) throws TinyUrlException {
        try {
            reentrantLock.tryLock(1000, TimeUnit.MILLISECONDS);
            if (longToShortURLMap.containsKey(longUrlEntity)) {
                return longToShortURLMap.get(longUrlEntity);
            }

            String shortUrl = generateUniqueShortURL();
            longToShortURLMap.put(longUrlEntity, shortUrl);
            shortToLongEntityMap.put(shortUrl, longUrlEntity);
            return shortUrl;
        } catch (InterruptedException e) {
            throw new TinyUrlException(TinyUrlError.UNABLE_TO_LOCK);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public void flush() {
        longToShortURLMap = new ConcurrentHashMap<>();
        shortToLongEntityMap = new ConcurrentHashMap<>();
    }

    private String generateUniqueShortURL() throws TinyUrlException{
        if (randomSeedValue == Long.MAX_VALUE) {
            throw new TinyUrlException(TinyUrlError.ERROR_ALLOCATE_MEMORY);
        }
        return Base62EncoderCodec.encode(randomSeedValue++);
    }
}