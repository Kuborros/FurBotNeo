package com.kuborros.FurBotNeo.utils.config;

import java.util.Properties;


public class Config {

    private final String BOT_TOKEN;
    private final String OWNER_ID;
    private final String LASTFM_KEY;
    private final String LASTFM_SECRET;

    private final boolean GUILD_MSGS;



    public String getBOT_TOKEN() {
        return BOT_TOKEN;
    }

    public String getOWNER_ID() {
        return OWNER_ID;
    }

    public String getPREFIX() {
        return "!";
    }

    public String getVERSION() {
        return "V2.3";
    }

    public boolean isGUILD_MSGS() {
        return GUILD_MSGS;
    }

    public String getLASTFM_KEY() {
        return LASTFM_KEY;
    }

    public String getLASTFM_SECRET() {
        return LASTFM_SECRET;
    }

    public Config() {
        ConFile.ConFileCheck();
        Properties properties = ConFile.getProperties();

        BOT_TOKEN = properties != null ? properties.getProperty("BotToken") : "0";
        OWNER_ID = properties != null ? properties.getProperty("OwnerId") : "0";
        LASTFM_KEY = properties != null ? properties.getProperty("LastFmKey") : "0";
        LASTFM_SECRET = properties != null ? properties.getProperty("LastFmSecret") : "0";
        GUILD_MSGS = properties != null && properties.getProperty("PostGuildMessages").equalsIgnoreCase("true");

    }

}
