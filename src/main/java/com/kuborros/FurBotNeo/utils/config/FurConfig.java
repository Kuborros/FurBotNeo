package com.kuborros.FurBotNeo.utils.config;

import com.jagrosh.jdautilities.command.GuildSettingsProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FurConfig implements GuildSettingsProvider {

    private final List<String> prefixes = new ArrayList<>(5);
    private final String botName;
    private final boolean welcomeMsg;
    private final boolean isFurry;
    private final boolean isNSFW;
    private final String audioChannel;


    //Fallback configuration
    FurConfig() {
        this.botName = "FurryBot";
        this.audioChannel = "0";
        this.isFurry = true;
        this.welcomeMsg = false;
        this.isNSFW = false;
    }

    FurConfig(String botName, boolean welcomeMsg, boolean isFurry, boolean isNSFW, String prefix, String audioChannel) {
        this.botName = botName;
        this.prefixes.add(prefix);
        this.audioChannel = audioChannel;
        this.isFurry = isFurry;
        this.welcomeMsg = welcomeMsg;
        this.isNSFW = isNSFW;

    }

    @Nullable
    @Override
    public Collection<String> getPrefixes() {
        return prefixes.isEmpty() ? null : prefixes;
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

    public boolean isNSFW() {
        return isNSFW;
    }

    public String getAudioChannel() {
        return audioChannel;
    }
}

