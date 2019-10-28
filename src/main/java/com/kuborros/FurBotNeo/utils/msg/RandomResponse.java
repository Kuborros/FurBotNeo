package com.kuborros.FurBotNeo.utils.msg;

import com.kuborros.FurBotNeo.utils.config.FurConfig;
import com.kuborros.FurBotNeo.utils.config.FurrySettingsManager;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class RandomResponse {

    private static FurrySettingsManager settings;

    public RandomResponse(FurrySettingsManager settingsManager) {
        settings = settingsManager;
    }


    public String getRandomDeniedMessage(Guild guild) {

        FurConfig config = settings.getSettings(guild);

        LinkedList<String> messageList = new LinkedList<>(Arrays.asList(
                "You can't order me like that~.",
                "I don't listen to you, silly.",
                "Try harder, maybe it will work some day.",
                "Your attempt at using 'Very sad kitty eyes' has failed.",
                "Well... No.",
                "Nice try.",
                "I'll pretend you didn't say that.",
                "Not today.",
                "*yawn*",
                "I have the power. You don't.",
                "User not in sudoers file. This incident will be reported.",
                "Access Denied.",
                "Please don't joke about that."));
        if (config.isFurry()) {
            messageList.addAll(Arrays.asList(
                    "",
                    ""));
        }
        if (config.isNSFW()) {
            messageList.addAll(Arrays.asList(
                    "",
                    ""));
        }
        return messageList.get(new Random().nextInt(messageList.size()));
    }

    public String getRandomWarningMessage(Guild guild) {

        FurConfig config = settings.getSettings(guild);

        LinkedList<String> messageList = new LinkedList<>(Arrays.asList(
                "",
                ""));
        if (config.isFurry()) {
            messageList.addAll(Arrays.asList(
                    "",
                    ""));
        }
        if (config.isNSFW()) {
            messageList.addAll(Arrays.asList(
                    "",
                    ""));
        }
        return messageList.get(new Random().nextInt(messageList.size()));
    }

    public String getRandomErrorMessage(Guild guild) {

        FurConfig config = settings.getSettings(guild);

        LinkedList<String> messageList = new LinkedList<>(Arrays.asList(
                "",
                ""));
        if (config.isFurry()) {
            messageList.addAll(Arrays.asList(
                    "",
                    ""));
        }
        if (config.isNSFW()) {
            messageList.addAll(Arrays.asList(
                    "",
                    ""));
        }
        return messageList.get(new Random().nextInt(messageList.size()));
    }


}
