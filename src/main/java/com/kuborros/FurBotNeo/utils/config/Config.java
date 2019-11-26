package com.kuborros.FurBotNeo.utils.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;


public class Config {

    private final String BOT_TOKEN;
    private final String OWNER_ID;

    private static final Logger LOG = LoggerFactory.getLogger(Config.class);
    private static final File CONFIG = new File("config.cfg");

    public String getBOT_TOKEN() {
        return BOT_TOKEN;
    }

    public String getOWNER_ID() {
        return OWNER_ID;
    }

    public Config() {
        ConFileCheck();
        Properties properties = getProperties();

        BOT_TOKEN = properties != null ? properties.getProperty("BotToken") : "0";
        OWNER_ID = properties != null ? properties.getProperty("OwnerId") : "0";
    }


    private void ConFileCheck() {

        try {
            if (CONFIG.createNewFile()) {
                Properties prop = new Properties();
                OutputStream output = null;
                LOG.info("Generating configuration file...");
                try {

                    output = new FileOutputStream(CONFIG);

                    prop.setProperty("BotToken", "");
                    prop.setProperty("OwnerId", "0");

                    prop.store(output, null);

                    LOG.info("You need to populate this file with your bot token and you userID!");

                } catch (IOException e) {
                    LOG.error("... and failed! ", e);
                } finally {
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            LOG.error("Failed to close file: ", e);
                        }
                    }

                }
            }
        } catch (IOException e) {
            LOG.error("Error while creating configuration file! ", e);
        }
    }

    private Properties getProperties() {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(CONFIG);

            prop.load(input);

            return prop;

        } catch (IOException e) {
            LOG.error("Failed to obtain properties: ", e);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOG.error("Failed to close file: ", e);
                }
            }
        }

    }

}
