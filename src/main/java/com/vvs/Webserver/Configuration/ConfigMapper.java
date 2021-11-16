package com.vvs.Webserver.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.vvs.Webserver.Utils.ParseJson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigMapper {

    private static ConfigMapper configMapper; //we need just a single mapper for configuration
    private static Config currentConfig;

    private ConfigMapper(){}

    public static ConfigMapper getConfigMapper(){
        if(configMapper == null)
            configMapper = new ConfigMapper();
        return configMapper;
    }

    public void loadConfigFile(String path) {
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new ConfigException(e);
        }

        StringBuffer stringBuffer = new StringBuffer();
        int i;
        while(true){
            try {
                if (!(( i = fileReader.read() ) != -1)) break;
            } catch (IOException e) {
                throw new ConfigException(e);
            }
            stringBuffer.append((char)i);
        }

        JsonNode conf = null;
        try {
            conf = ParseJson.parse(stringBuffer.toString());
        } catch (JsonProcessingException e) {
            throw new ConfigException("An error occured while parsing the Config File-",e);
        }
        try {
            currentConfig = ParseJson.parsedJson(conf, Config.class);
        } catch (JsonProcessingException e) {
            throw new ConfigException("An error occured inside of parsing the Config File-",e);
        }
    }

    public Config getCurrentConfig(){
        if(currentConfig == null){
            throw new ConfigException("The configuration was not set");
        }
        return currentConfig;

    }
}
