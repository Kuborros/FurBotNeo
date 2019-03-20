package com.kuborros.FurBotNeo.utils.config;

import java.util.Properties;


public class Config {

    private final String BOT_TOKEN;
    private final String OWNER_ID;

    private final boolean GUILD_MSGS;



    public String getBOT_TOKEN() {
        return BOT_TOKEN;
    }

    public String getOWNER_ID() {
        return OWNER_ID;
    }

    @SuppressWarnings("SameReturnValue")
    public String getPREFIX() {
        return "!";
    }

    public boolean isGUILD_MSGS() {
        return GUILD_MSGS;
    }

    public Config() {
        ConFile.ConFileCheck();
        Properties properties = ConFile.getProperties();

        BOT_TOKEN = properties != null ? properties.getProperty("BotToken") : "0";
        OWNER_ID = properties != null ? properties.getProperty("OwnerId") : "0";
        GUILD_MSGS = properties != null && properties.getProperty("PostGuildMessages").equalsIgnoreCase("true");

    }

}
