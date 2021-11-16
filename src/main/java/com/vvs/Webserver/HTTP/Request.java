package com.vvs.Webserver.HTTP;

import java.util.ArrayList;
import java.util.List;

public class Request extends Message {
    private HttpMethod method;
    private String target;
    private String initialVersion;
    private HttpVersion compatibleVersion;
    private String host;
    private ArrayList<String> headers;

    Request(){ }

    public HttpMethod getMethod(){
        return method;
    }

    public String getTarget() { return target; }

    public String getInitialVersion() { return initialVersion; }

    public HttpVersion getCompatibleVersion() { return compatibleVersion; }

    public String getHost() { return host; }

    public List<String> getHeaders() { return headers; }

    void setMethod(String method) throws HttpParserException {
        for(HttpMethod httpMethod:HttpMethod.values()){
            if(method.equals(httpMethod.name())){
                this.method = httpMethod;
                return;
            }
        }
        throw new HttpParserException(StatusCode.CLIENT_ERROR_501_NOT_IMPLEMENTED);
    }

    void setTarget(String target) throws HttpParserException {
        if(target == null || target.length() == 0){
            throw new HttpParserException(StatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.target = target;
    }

    void setHttpVersion(String initialVersion) throws HttpBadVersionException, HttpParserException {
        this.initialVersion = initialVersion;
        this.compatibleVersion = HttpVersion.getCompatibleVersion(initialVersion);
        if(this.compatibleVersion == null){
            throw new HttpParserException(StatusCode.SERVER_ERROR_505_VERSION_NOT_SUPPORTED);
        }
    }

    void setHost(String host) throws HttpParserException {
        if(host == null || host.length() == 0){
            throw new HttpParserException(StatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.host = host;
    }

    void setHeaders(ArrayList<String> headers) throws HttpParserException {
        if(headers == null || headers.size() == 0){
            throw new HttpParserException(StatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.headers = headers;
    }
}
