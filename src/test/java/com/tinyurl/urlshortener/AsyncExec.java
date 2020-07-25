package com.tinyurl.urlshortener;

import org.junit.Assert;

import java.net.URL;

public class AsyncExec implements Runnable {
    private String json;
    String longURL = "http://www.amazon.com/Kindle-Wireless-Reading-Display-Globally/dp/B003FSUDM4/ref=amb_link_353259562_2?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=center-10&pf_rd_r=11EYKTN682A79T370AM3&pf_rd_t=201&pf_rd_p=1270985982&pf_rd_i=B002Y27P3M";
    public AsyncExec (String json) {
        this.json = json;
    }

    @Override
    public void run() {
        try {
            String shortURL = HttpURLConnectionExample.sendPOST(json);
            URL url = null;
            url = new URL(shortURL);
            Assert.assertEquals(url.getFile().length(), 9);
            String value = HttpURLConnectionExample.sendGET(url.getFile());
            Assert.assertEquals(value, longURL);
        }catch (Exception e) {
            System.out.println(e);
        }
    }
}
