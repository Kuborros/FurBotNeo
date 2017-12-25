package com.kuborros.FurBotNeo.utils.config;

import java.util.Properties;


@SuppressWarnings("FieldCanBeLocal")
public class Config {

    private final Properties properties;
    private final String BOT_TOKEN;
    private final String OWNER_ID;
    private final String PREFIX = "!";
    private final String VERSION = "V2.3";
    private final boolean GUILD_MSGS;



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

    public boolean isGUILD_MSGS() {
        return GUILD_MSGS;
    }

    public Config() {
        ConFile.ConFileCheck();
        properties = ConFile.getProperties();

        BOT_TOKEN = properties != null ? properties.getProperty("BotToken") : "0";
        OWNER_ID = properties != null ? properties.getProperty("OwnerId") : "0";
        GUILD_MSGS = properties != null && properties.getProperty("PostGuildMessages").equalsIgnoreCase("true");

    }

}
