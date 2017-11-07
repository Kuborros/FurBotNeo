package com.kuborros.FurBotNeo.utils.config;

import java.util.Properties;


public class Config {

    private Properties properties;
    private String BOT_TOKEN = "";
    private String OWNER_ID = "";
    private String PREFIX = "!!";
    private String VERSION = "V2.0B";
    private boolean FILE_LOGGING = true;
    private boolean GUILD_MSGS = true;


    public Properties getProperties() {
        return properties;
    }

    public String getBOT_TOKEN() {
        return BOT_TOKEN;
    }

    public String getOWNER_ID() {
        return OWNER_ID;
    }

    public String getPREFIX() {
        return PREFIX;
    }

    public String getVERSION() {
        return VERSION;
    }

    public boolean isFILE_LOGGING() {
        return FILE_LOGGING;
    }

    public boolean isGUILD_MSGS() {
        return GUILD_MSGS;
    }

    public Config() {
        ConFile.ConFileCheck();
        properties = ConFile.getProperties();

        BOT_TOKEN = properties.getProperty("BotToken");
        OWNER_ID = properties.getProperty("OwnerId");
        FILE_LOGGING =  properties.getProperty("LogToFile").equalsIgnoreCase("true");
        GUILD_MSGS = properties.getProperty("PostGuildMessages").equalsIgnoreCase("true");

    }

}
