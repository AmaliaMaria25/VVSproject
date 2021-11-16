package com.vvs.Webserver.HTTP;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {
    VERSION_1_1("HTTP/1.1",1,1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;

    private static final Pattern versionPattern = Pattern.compile("^HTTP/(?<major>\\d+).(?<minor>\\d+)");


    HttpVersion(String LITERAL, int MAJOR, int MINOR) {
        this.LITERAL = LITERAL;
        this.MAJOR = MAJOR;
        this.MINOR = MINOR;
    }

    public static HttpVersion getCompatibleVersion(String literal) throws HttpBadVersionException {
        Matcher matcher = versionPattern.matcher(literal);
        if(!matcher.find() || matcher.groupCount() != 2){
            throw new HttpBadVersionException();
        }
        int major = Integer.parseInt(matcher.group("major"));
        int minor = Integer.parseInt(matcher.group("minor"));
        HttpVersion compatibleVersion = null;
        for(HttpVersion version:HttpVersion.values()){
            if(version.LITERAL.equals(literal)){
                return version;
            }else{
                if(version.MAJOR == major){
                    if(version.MINOR < minor){
                        compatibleVersion = version;
                    }
                }
            }
        }
        return compatibleVersion;
    }
}
