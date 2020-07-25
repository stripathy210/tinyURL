package com.tinyurl.urlshortener;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UrlshortenerApplicationTests {

    String longURL = "http://www.amazon.com/Kindle-Wireless-Reading-Display-Globally/dp/B003FSUDM4/ref=amb_link_353259562_2?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=center-10&pf_rd_r=11EYKTN682A79T370AM3&pf_rd_t=201&pf_rd_p=1270985982&pf_rd_i=B002Y27P3M";
    String clientId = "22222222";

    @After
    public void flush() {
        try {
            HttpURLConnectionExample.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testValidTests() throws IOException {
        String json = "{ \n" +
                "    \"longURL\":" + "\"" + longURL + "\" \n" +
                    ",\"clientId\":" + "\"" + clientId + "\""
                    +"\n }";
        for (int i =0; i < 10; i++) {
           String shortURL = HttpURLConnectionExample.sendPOST(json);
            URL url = new URL(shortURL);
            Assert.assertEquals(url.getFile().length(), 9);
            String value = HttpURLConnectionExample.sendGET(url.getFile());
            Assert.assertEquals(value, longURL);
        }

        String shortURL = HttpURLConnectionExample.sendPOST(json);
        URL url = new URL(shortURL);
        String value = HttpURLConnectionExample.getCnt(url.getFile());
        Assert.assertEquals(value, "10");
    }

    @Test
    public void testForEachClientWithSameURL() throws IOException {

        for (int i =0; i < 10; i++) {
            String json = "{ \n" +
                    "    \"longURL\":" + "\"" + longURL + "\" \n" +
                    ",\"clientId\":" + "\"" + clientId + i + "\""
                    +"\n }";
            String shortURL = HttpURLConnectionExample.sendPOST(json);
            URL url = new URL(shortURL);
            Assert.assertEquals(url.getFile().length(), 9);
            String value = HttpURLConnectionExample.sendGET(url.getFile());
            Assert.assertEquals(value, longURL);
            value = HttpURLConnectionExample.getCnt(url.getFile());
            Assert.assertEquals(value, "1");
        }
    }

    @Test
    public void testNullLongUrl() throws IOException {

        for (int i =0; i < 10; i++) {
            String json = "{ \n" +
                    "    \"longURL\":" + "\"" + "" + "\" \n" +
                    ",\"clientId\":" + "\"" + clientId + i + "\""
                    +"\n }";
            String shortURL = HttpURLConnectionExample.sendPOST(json);
            Assert.assertEquals(shortURL, "T004-- Invalid Request");
        }
    }

    @Test
    public void testValidTestsMultiThreaded() throws IOException, InterruptedException {
        String json = "{ \n" +
                "    \"longURL\":" + "\"" + longURL + "\" \n" +
                ",\"clientId\":" + "\"" + clientId + "\""
                +"\n }";

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i =0; i < 1000; i++) {
            AsyncExec asyncExec = new AsyncExec(json);
            executor.submit(asyncExec);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        String shortURL = HttpURLConnectionExample.sendPOST(json);
        URL url = new URL(shortURL);
        String value = HttpURLConnectionExample.getCnt(url.getFile());
        Assert.assertEquals(value, "1000");
    }

    @Test
    public void testValidTestsMultiThreadedWithDiffClientIds() throws IOException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        String json = null;
        for (int i =0; i < 500; i++) {

            if ( i < 100) {
                json = "{ \n" +
                        "    \"longURL\":" + "\"" + longURL + "\" \n" +
                        ",\"clientId\":" + "\"" + clientId +i + "\""
                        +"\n }";
            }

            AsyncExec asyncExec = new AsyncExec(json);
            executor.submit(asyncExec);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        String shortURL = HttpURLConnectionExample.sendPOST(json);
        URL url = new URL(shortURL);
        String value = HttpURLConnectionExample.getCnt(url.getFile());
        Assert.assertEquals(value, "401");

    }

}
