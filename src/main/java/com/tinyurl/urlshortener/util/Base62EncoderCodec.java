package com.tinyurl.urlshortener.util;

public class Base62EncoderCodec {
    private final static String encode = "abcdefghijklmnopqrstuvwyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String encode (long randomValue) {
        StringBuilder hashStr = new StringBuilder();
        while (randomValue> 0) {
            Long base62Encode = randomValue % 62;
            hashStr.append(encode.charAt(base62Encode.intValue()));
            randomValue/=62;
        }

        return hashStr.toString();
    }
}
