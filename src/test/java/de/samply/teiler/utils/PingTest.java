package de.samply.teiler.utils;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class PingTest {

    private String [] urls = {"http://localhost:8180/auth/", "http://localhost:5000", "http://localhost:8280/ID-Manager/index.html"};

    //TODO: Finish test
    @Test
    void ping() {
        Ping ping = new Ping(10, 10);
        Arrays.stream(urls).forEach(url -> assertTrue(ping.ping(url)));
    }
}
