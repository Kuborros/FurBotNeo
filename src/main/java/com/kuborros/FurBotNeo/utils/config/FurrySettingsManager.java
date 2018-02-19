package com.kuborros.FurBotNeo.utils.config;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import net.dv8tion.jda.core.entities.Guild;

import javax.annotation.Nullable;

import static com.kuborros.FurBotNeo.BotMain.db;

public class FurrySettingsManager implements GuildSettingsManager {
    @Nullable
    @Override
    public Object getSettings(Guild guild) {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    public void shutdown() {
        db.close();
    }

}
