package com.vvs.Webserver.HTTP;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HttpVersionTest {

    @Test
    void getCompatibleVersionMatch(){
        HttpVersion version = null;
        try {
            version = HttpVersion.getCompatibleVersion("HTTP/1.1");
        } catch (HttpBadVersionException e) {
            fail();
        }
        assertNotNull(version);
        assertEquals(version, HttpVersion.VERSION_1_1);
    }


    @Test
    void getCompatibleVersionNoMatch(){
        HttpVersion version = null;
        try {
            version = HttpVersion.getCompatibleVersion("http/1.1");
            fail();
        } catch (HttpBadVersionException e) {
        }
    }

    @Test
    void getCompatibleVersionNoMatchHigherVersion(){
        HttpVersion version = null;
        try {
            version = HttpVersion.getCompatibleVersion("HTTP/1.2");
            assertNotNull(version);
            assertEquals(version, HttpVersion.VERSION_1_1);
        } catch (HttpBadVersionException e) {
            fail();
        }
    }
}
