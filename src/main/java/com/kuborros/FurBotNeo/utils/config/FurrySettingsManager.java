package com.kuborros.FurBotNeo.utils.config;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import net.dv8tion.jda.core.entities.Guild;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.kuborros.FurBotNeo.BotMain.db;


public class FurrySettingsManager implements GuildSettingsManager {

    private static final FurConfig defaultConfig = new FurConfig();

    private static final Map<String, FurConfig> confCache = new HashMap<>();

    @Override
    public FurConfig getSettings(Guild guild) {
        if (confCache.containsKey(guild.getId())) {
            if (db.guildNeedsUpdate(guild)) {
                FurConfig conf = updateFromDb(guild);
                confCache.remove(guild.getId());
                confCache.put(guild.getId(), conf);
                return conf;
            } else {
                return confCache.get(guild.getId());
            }

        } else {
            FurConfig conf = updateFromDb(guild);
            confCache.put(guild.getId(), conf);
            return conf;
        }

    }

    private FurConfig updateFromDb(Guild guild) {
        try {
            return db.getGuildConfig(guild);
        } catch (SQLException e) {
            e.printStackTrace();
            return defaultConfig;
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void shutdown() {
        db.close();
    }

}
