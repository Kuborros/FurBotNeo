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
                "I don't listen to users you, silly.",
                "Your attempt at using 'Very sad kitty eyes' has failed.",
                "Well... No.",
                "Nice try.",
                "I'll pretend you didn't say that.",
                "Not today.",
                "*yawn*",
                "User not in sudoers file. This incident will be reported.",
                "Access Denied.",
                "Please don't joke about that."));
        if (config.isFurry()) {
            messageList.addAll(Arrays.asList(
                    "No. UwU",
                    "You are not fluffy enough to run this!"));
        }
        if (config.isNSFW()) {
            messageList.add("Succ me, then we can talk~");
        }
        return messageList.get(new Random().nextInt(messageList.size()));
    }

    public String getRandomErrorMessage(Guild guild) {

        FurConfig config = settings.getSettings(guild);


        LinkedList<String> messageList = new LinkedList<>(Arrays.asList(
                "I think i did something wrong...",
                "Its not my fault, i swear.",
                "The server is currently on fire, please wait.",
                "--AI core reboot in progress--",
                "That's a bad sign.",
                "No, i have no idea why.",
                "Hopefully the error message is usefull...",
                "Segmentation fault at 0xOHNO621",
                "Aaaaaaaaa",
                "Connection interrupted. Disconnecting in 39.5s...",
                "Bot machine broke."));
        if (config.isFurry()) {
            messageList.addAll(Arrays.asList(
                    "Im sowwy UwU",
                    "*Angry bat noises*",
                    "I got my tail stuck in the door..."));
        }
        if (config.isNSFW()) {
            messageList.add("That's what you get for lewding me!");
        }
        return messageList.get(new Random().nextInt(messageList.size()));
    }

}
