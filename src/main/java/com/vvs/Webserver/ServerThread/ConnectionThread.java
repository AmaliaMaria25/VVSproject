package com.vvs.Webserver.ServerThread;

import com.vvs.Webserver.HTTP.HttpMethod;
import com.vvs.Webserver.HTTP.HttpParser;
import com.vvs.Webserver.HTTP.HttpParserException;
import com.vvs.Webserver.HTTP.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionThread extends Thread {

    private Socket socket;
    private int serverState;
    private final static Logger LOGGER = LoggerFactory.getLogger(ConnectionThread.class);

    public ConnectionThread(Socket socket, int serverState){

        this.socket = socket;
        this.serverState = serverState;
    }

    @Override
    public void run() {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = socket.getInputStream();
            output = socket.getOutputStream();


            HttpParser httpParser = new HttpParser();
            Request request = httpParser.parseHttpRequest(input);
            String response;
            if(request.getMethod().equals(HttpMethod.GET) || request.getMethod().equals(HttpMethod.HEAD)){

                if(serverState == 1){
                    //Running state  ---> response with the requested page
                    String html = "<html><head><title>Java Web SERVER</title></head><body><h1>This is a Test!</h1></body></html>";
                    final String CRLF = "\n\r";
                    response = "HTTP/1.1 200 OK" + CRLF +
                            "Content-Length: " + html.getBytes().length + CRLF +
                            CRLF +
                            html + CRLF +
                            CRLF;

                    output.write(response.getBytes());
                }else if(serverState ==2){
                    //Maintenance state ---> response with a predefined page


                    String html = "<html><head><title>Java Web SERVER</title></head><body><h1>This is a Test!</h1></body></html>";
                    final String CRLF = "\n\r";
                   response = "HTTP/1.1 200 OK" + CRLF +
                            "Content-Length: " + html.getBytes().length + CRLF +
                            CRLF +
                            html + CRLF +
                            CRLF;

                    output.write(response.getBytes());
                }

            }



            LOGGER.info("Connection finished");
        } catch (IOException | HttpParserException e) {
            LOGGER.error("Communication error ", e);
            e.printStackTrace();
        }finally{
            if(input!=null){
                try { input.close();} catch (IOException e) {}
            }

            if(output!=null) {
                try {
                    output.close();
                } catch (IOException e) {}
            }

            if(socket!=null) {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }
    }
}
