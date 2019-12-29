package com.kuborros.FurBotNeo.utils.config;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JConfig {

    private static final Logger LOG = LoggerFactory.getLogger(JConfig.class);
    private static final File CONFILE = new File("config.json");
    private String bot_token;
    private String owner_id;
    private boolean invidio_enabled;
    private boolean sharding_enabled;
    private List<String> bannedGuilds;

    public JConfig() {
        JSONObject config = loadOrCreateConfig();
        if (config == null) {

        } else {
            bot_token = config.getString("bot_token");
            owner_id = config.getString("owner_id");

            JSONObject bools = config.getJSONObject("config_options");
            invidio_enabled = bools.getBoolean("invidio");
            sharding_enabled = bools.getBoolean("shard");

            JSONArray banned = config.getJSONArray("blacklist_servers");

        }
    }

    public String getBotToken() {
        return bot_token;
    }

    public String getOwnerId() {
        return owner_id;
    }

    public boolean isInvidioEnabled() {
        return invidio_enabled;
    }

    public boolean isShardingEnabled() {
        return sharding_enabled;
    }

    public List<String> getBannedGuilds() {
        return bannedGuilds;
    }

    private JSONObject loadOrCreateConfig() {

        if (CONFILE.canRead()) {
            try {
                return new JSONObject(FileUtils.openInputStream(CONFILE));
            } catch (IOException e) {
                LOG.error("Loading configuration failed: ", e);
                return null;
            }
        } else {
            LOG.info("Generating default configuration file...");
            try (InputStream def = getClass().getClassLoader().getResourceAsStream("defaultConfig.json")) {
                assert def != null;
                FileUtils.copyInputStreamToFile(def, CONFILE);
                LOG.info("You need to populate this file with your bot token and you userID!");
            } catch (IOException e) {
                LOG.error("... and failed! ", e);
            }
            return null;
        }
    }

}
