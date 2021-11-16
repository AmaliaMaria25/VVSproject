package com.vvs.Webserver;

import com.vvs.Webserver.Configuration.Config;
import com.vvs.Webserver.Configuration.ConfigMapper;
import com.vvs.Webserver.ServerThread.ServerListener;
import com.vvs.Webserver.Utils.StoppedServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WebserverRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(WebserverRunner.class);

    public static void main(String[] args){
        LOGGER.info("Starting server..");
        ConfigMapper.getConfigMapper().loadConfigFile("src/main/resources/configFile.json");
        Config config = ConfigMapper.getConfigMapper().getCurrentConfig();

        LOGGER.info("The port is: "+ config.getPort());
        LOGGER.info("The path is: "+ config.getPath());
        LOGGER.info("The status is: "+ config.getStatus());
        if(config.getStatus() > 0) {
            ServerListener serverListener = null;
            try {
                serverListener = new ServerListener(config.getPort(), config.getPath(), config.getStatus());
                serverListener.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            LOGGER.info("Connection timeout");
            throw new StoppedServerException("Server is stopped. Connection Timeout");
        }
    }
}
