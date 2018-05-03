package com.kuborros.FurBotNeo.utils.config;

import com.jagrosh.jdautilities.command.GuildSettingsProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


class FurConfig implements GuildSettingsProvider {

    private final List<String> prefixes = new ArrayList<>();
    private final String botName;
    private boolean welcomeMsg;
    private boolean isFurry;
    private String audioChannel;

    public FurConfig() {
        this.botName = "FurryBot";
        this.audioChannel = "0";
        this.isFurry = true;
        this.welcomeMsg = true;
    }

    public FurConfig(String botName, boolean welcomeMsg, boolean isFurry, String prefix, String audioChannel) {
        this.botName = botName;
        this.prefixes.add(prefix);
        this.audioChannel = audioChannel;
        this.isFurry = isFurry;
        this.welcomeMsg = welcomeMsg;

    }

    @Nullable
    @Override
    public Collection<String> getPrefixes() {
        if (prefixes.isEmpty()) {
            return null;
        } else return prefixes;
    }

    public String getBotName() {
        return botName;
    }

    public boolean isWelcomeMsg() {
        return welcomeMsg;
    }

    public boolean isFurry() {
        return isFurry;
    }

    public String getAudioChannel() {
        return audioChannel;
    }
}

