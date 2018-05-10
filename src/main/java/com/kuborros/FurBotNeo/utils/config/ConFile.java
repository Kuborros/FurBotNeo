package com.kuborros.FurBotNeo.utils.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

@SuppressWarnings("ResultOfMethodCallIgnored")
class ConFile {
    private static final Logger LOG = LoggerFactory.getLogger(ConFile.class);
    private static File CONFIG;

    static void ConFileCheck() {

        CONFIG = new File(new File(System.getProperty("user.home"), ".DiscordBot"), "config.cfg");

        if (!CONFIG.exists()) {
            try {
                new File(System.getProperty("user.home"), ".DiscordBot").mkdirs();
                CONFIG.createNewFile();
            } catch (IOException e) {
                LOG.error("Error while creating configuration file!");
                System.exit(255);
            }
            Properties prop = new Properties();
            OutputStream output = null;
            LOG.info("Generating configuration file...");
            try {

                output = new FileOutputStream(CONFIG);

                prop.setProperty("BotToken", "");
                prop.setProperty("OwnerId", "0");
                prop.setProperty("LastFmToken", "0");
                prop.setProperty("PostGuildMessages", "false");

                prop.store(output, null);

                LOG.info("You need to populate this file with your bot token and you userID!");

            } catch (IOException e) {
                e.printStackTrace();
                LOG.info("... but it went horribly wrong!");
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


// --Commented out by Inspection START (2017-12-25 23:52):
//    static void modifyProperty(String key, String value) {
//        Properties prop = new Properties();
//        InputStream input = null;
//        OutputStream output = null;
//        try {
//
//            input = new FileInputStream(CONFIG);
//            prop.load(input);
//            output = new FileOutputStream(CONFIG);
//            prop.setProperty(key, value);
//            prop.store(output, null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (output != null) {
//                try {
//                    output.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (input != null) {
//                try {
//                    input.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }
// --Commented out by Inspection STOP (2017-12-25 23:52)

}
