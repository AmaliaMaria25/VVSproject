package com.vvs.Webserver.HTTP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpParser {
    private final static Logger LOGGER  = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20; // 32
    private static final int CR = 0x0D; // 13
    private static final int LF = 0x0A; // 10

    public Request parseHttpRequest(InputStream input) throws HttpParserException{
        InputStreamReader inputReader = new InputStreamReader(input, StandardCharsets.US_ASCII);
        Request request = new Request();

        try {
            BufferedReader in = new BufferedReader(inputReader);
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while (!(inputLine = in.readLine()).isEmpty()) {
                stringBuilder.append(inputLine + "\r\n");
            }
            String requestString = stringBuilder.toString();
            LOGGER.info("/*/*aici---" + requestString);
        }catch(IOException e){
            e.printStackTrace();
        }

        try {
            parseRequestLine(request, inputReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            parseHeaders(request, inputReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        parseBody(request, inputReader);
        return request;
    }

    public void parseRequestLine(Request request, InputStreamReader input) throws IOException, HttpParserException {
        StringBuilder stringBuilder = new StringBuilder();
        boolean methodParsed = false;
        boolean targetParsed = false;
        int byteToRead;
        while((byteToRead = input.read())>=0) {
            if (byteToRead == CR) {
                byteToRead = input.read();
                if (byteToRead == LF) {
                    LOGGER.debug("Request Line Version: {}", stringBuilder.toString());
                    if(!methodParsed || !targetParsed){
                        throw new HttpParserException(StatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }

                    try {
                        request.setHttpVersion(stringBuilder.toString());
                    } catch (HttpBadVersionException e) {
                        throw new HttpParserException(StatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    return;
                }else{
                    throw new HttpParserException(StatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }
            if (byteToRead == SP) {
                if (!methodParsed) {
                    LOGGER.debug("Request Line Method: {}", stringBuilder.toString());
                    request.setMethod(stringBuilder.toString());
                    methodParsed = true;
                } else if (!targetParsed) {
                    LOGGER.debug("Request Line Target: {}", stringBuilder.toString());
                    request.setTarget(stringBuilder.toString());
                    targetParsed = true;
                }else{
                    throw new HttpParserException(StatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }

                stringBuilder.delete(0, stringBuilder.length());

            } else {
                stringBuilder.append((char) byteToRead);
                if(!methodParsed){
                    if(stringBuilder.length() > HttpMethod.MAX_LENGTH)
                        throw new HttpParserException(StatusCode.CLIENT_ERROR_501_NOT_IMPLEMENTED);
                }
            }
        }
    }

    public void parseHeaders(Request request, InputStreamReader input) throws IOException, HttpParserException {

        BufferedReader in = new BufferedReader(input);
        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();
        while (!(inputLine = in.readLine()).isEmpty()) {
            stringBuilder.append(inputLine + "\r\n");
        }
        String requestString = stringBuilder.toString();
        LOGGER.info("/*/*"+requestString);

        //parse host
        String[] requestsLines = requestString.split("\r\n");
        request.setHost(requestsLines[0].split(" ")[0]);

        //parse headers
        ArrayList<String> headers = new ArrayList<>();
        for (int h = 1; h < requestsLines.length; h++) {
            String header = requestsLines[h];
            headers.add(header);
        }
        request.setHeaders(headers);

        //String accessLog = String.format("headers %s",headers.toString());
        //LOGGER.debug(accessLog);

    }

    public void parseBody(Request request, InputStreamReader input){

    }


}
