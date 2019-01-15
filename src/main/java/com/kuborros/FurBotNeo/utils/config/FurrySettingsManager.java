package com.kuborros.FurBotNeo.utils.config;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import net.dv8tion.jda.core.entities.Guild;

import static com.kuborros.FurBotNeo.BotMain.db;


public class FurrySettingsManager implements GuildSettingsManager {

    private static final FurConfig defaultConfig = new FurConfig();

    @Override
    public FurConfig getSettings(Guild guild) {
        if (guild.getId().equals("298561661198139392")) {
            return new FurConfig("Maria Notte", true, true, true, "!", "00000000000000");
        }
        return defaultConfig;
    }

    @Override
    public void init() {

    }

    @Override
    public void shutdown() {
        db.close();
    }

}
