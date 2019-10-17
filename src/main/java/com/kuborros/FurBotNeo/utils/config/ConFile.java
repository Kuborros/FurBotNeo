package com.kuborros.FurBotNeo.utils.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

class ConFile {
    private static final Logger LOG = LoggerFactory.getLogger(ConFile.class);
    private static File CONFIG;

    static void ConFileCheck() {

        CONFIG = new File("config.cfg");

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
                    e.printStackTrace();
                    LOG.error("... but it went horribly wrong! ", e);
                } finally {
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    }

                }
            }
        } catch (IOException e) {
            LOG.error("Error while creating configuration file!");
            System.exit(255);
        }
    }


    static Properties getProperties() {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(CONFIG);

            prop.load(input);

            return prop;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
