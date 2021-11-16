package com.vvs.Webserver.HTTP;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass(){
        httpParser = new HttpParser();
    }

    @Test
    void parseHttpRequestValid() {
        Request request = null;
        try {
            request = httpParser.parseHttpRequest(validTestGET());
        } catch (HttpParserException e) {
            fail(e);
        }
        assertNotNull(request);
        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getTarget(),"/");
        assertEquals(request.getInitialVersion(), "HTTP/1.1");
        assertEquals(request.getCompatibleVersion(), HttpVersion.VERSION_1_1);
    }

    @Test
    void parseHttpRequestBadMethod() {
        try {
            Request request = httpParser.parseHttpRequest(invalidTestGET());
            fail();
        } catch (HttpParserException e) {
            assertEquals(e.getErrorCode(), StatusCode.CLIENT_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @Test
    void parseHttpRequestBadMethod2() {
        try {
            Request request = httpParser.parseHttpRequest(invalidTestGET2());
            fail();
        } catch (HttpParserException e) {
            assertEquals(e.getErrorCode(), StatusCode.CLIENT_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @Test
    void parseHttpRequestInvalidItemsNumber() {
        try {
            Request request = httpParser.parseHttpRequest(invalidTestRequestItemsNumber());
            fail();
        } catch (HttpParserException e) {
            assertEquals(e.getErrorCode(), StatusCode.CLIENT_ERROR_400_BAD_REQUEST);

        }
    }

    @Test
    void parseHttpRequestEmpty() {
        try {
            Request request = httpParser.parseHttpRequest(invalidTestEmptyRequest());
            fail();
        } catch (HttpParserException e) {
            assertEquals(e.getErrorCode(), StatusCode.CLIENT_ERROR_400_BAD_REQUEST);

        }
    }

    @Test
    void parseHttpRequestNoLF() {
        try {
            Request request = httpParser.parseHttpRequest(invalidTestRequestNoLF());
            fail();
        } catch (HttpParserException e) {
            assertEquals(e.getErrorCode(), StatusCode.CLIENT_ERROR_400_BAD_REQUEST);

        }
    }

    @Test
    void parseHttpRequestBadVersion() {
        try {
            Request request = httpParser.parseHttpRequest(invalidTestRequestVersion());
            fail();
        } catch (HttpParserException e) {
            assertEquals(e.getErrorCode(), StatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseHttpRequestUnsupportedVersion() {
        try {
            Request request = httpParser.parseHttpRequest(invalidTestRequestUnsupportedVersion());
            fail();
        } catch (HttpParserException e) {
            assertEquals(e.getErrorCode(), StatusCode.SERVER_ERROR_505_VERSION_NOT_SUPPORTED);
        }
    }

    @Test
    void parseHttpRequestSupportedVersion() {
        try {
            Request request = httpParser.parseHttpRequest(validTestRequestSupportedVersion());
            assertNotNull(request);
            assertEquals(request.getCompatibleVersion(),HttpVersion.VERSION_1_1);
            assertEquals(request.getInitialVersion(), "HTTP/1.2");
        } catch (HttpParserException e) {
            fail();
        }
    }



    private InputStream validTestGET(){
        String request = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.US_ASCII));

        return input;
    }

    private InputStream invalidTestGET(){
        String request = "GeT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.US_ASCII));

        return input;
    }

    private InputStream invalidTestGET2(){
        String request = "GETTTTTT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.US_ASCII));

        return input;
    }

    private InputStream invalidTestRequestItemsNumber(){
        String request = "GET / AnotherItem HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.US_ASCII));

        return input;
    }

    private InputStream invalidTestEmptyRequest(){
        String request = "\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.US_ASCII));

        return input;
    }

    private InputStream invalidTestRequestNoLF(){
        String request = "GET / AnotherItem HTTP/1.1\r" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.US_ASCII));

        return input;
    }

    private InputStream invalidTestRequestVersion(){
        String request = "GET / HTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.US_ASCII));

        return input;
    }

    private InputStream invalidTestRequestUnsupportedVersion(){
        String request = "GET / HTTP/2.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.US_ASCII));

        return input;
    }

    private InputStream validTestRequestSupportedVersion(){
        String request = "GET / HTTP/1.2\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        InputStream input = new ByteArrayInputStream(request.getBytes(StandardCharsets.US_ASCII));

        return input;
    }
}