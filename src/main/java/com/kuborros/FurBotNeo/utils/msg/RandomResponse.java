package com.kuborros.FurBotNeo.utils.msg;

import com.kuborros.FurBotNeo.utils.config.FurConfig;
import com.kuborros.FurBotNeo.utils.config.FurrySettingsManager;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RandomResponse {

    private static FurrySettingsManager settings;
    private final Random rando = new Random();

    public RandomResponse(FurrySettingsManager settingsManager) {
        settings = settingsManager;
    }

    public static String getRandomBaseErrorMessage() {
        ArrayList<String> messageList = getGenericErrorMsg();
        return messageList.get(new Random().nextInt(messageList.size()));
    }

    private static ArrayList<String> getGenericErrorMsg() {
        return new ArrayList<>(Arrays.asList(
                "I think i did something wrong...",
                "Its not my fault, i swear.",
                "The server is currently on fire, please wait.",
                "--AI core reboot in progress--",
                "That's a bad sign.",
                "No, i have no idea why.",
                "Hopefully the error message is useful...",
                "Segmentation fault at 0xOHNO621",
                "Aaaaaaaaa",
                "Connection interrupted. Disconnecting in 39.5s...",
                "Bot machine broke."));
    }

    public String getRandomBootupMessage() {
        ArrayList<String> messageList = new ArrayList<>(Arrays.asList(
                "*Floppy drive seek noises*",
                "Node graph out of date, rebuilding...",
                "Starting up...",
                "It's alive!",
                "Showtime~",
                "Connecting to *the world wide web*",
                "OwO",
                "*yawn*",
                "Waking up!",
                "(Some funny startup message here)",
                "Hi!"));
        return messageList.get(rando.nextInt(messageList.size()));
    }

    public String getRandomShutdownMessage() {
        ArrayList<String> messageList = new ArrayList<>(Arrays.asList(
                "Sleepy time!",
                "If you say so...",
                "See you later!",
                "Bye~",
                "I needed a break anyways...",
                "..."));
        return messageList.get(rando.nextInt(messageList.size()));
    }

    public String getRandomNotNSFWMessage() {
        ArrayList<String> messageList = new ArrayList<>(Arrays.asList(
                "This command works only on NSFW channels! (For obvious reasons)",
                "OwO That's naughty~. (But do that on NSFW channel)",
                "This is not place for these *indecent* actions! (Do them over at NSFW channel)",
                "*Get that stuff over to NSFW channel before they kick us both!*",
                "T-That's lewd! Don't do that *here*. (But its ok over at NSFW channels)",
                "Please go and be dirty somewhere else. (Like NSFW channel)",
                "Can i see your \"Lewds in public\" permit? I thought so. Off to NSFW channel with you~",
                "That's an UwU from me on that. (But it's OwO at NSFW channels~)",
                "But that's illegal! (But not on NSFW channels~)",
                "How about we take it over to NSFW channel?",
                "I'm not allowed to do that here! (But we can hang out over at NSFW channels ;3 )"));
        return messageList.get(rando.nextInt(messageList.size()));
    }

    public String getRandomDeniedMessage(Guild guild) {

        FurConfig config = settings.getSettings(guild);

        ArrayList<String> messageList = new ArrayList<>(Arrays.asList(
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
        return messageList.get(rando.nextInt(messageList.size()));
    }

    public String getRandomErrorMessage(Guild guild) {

        FurConfig config = settings.getSettings(guild);

        ArrayList<String> messageList = getGenericErrorMsg();

        if (config.isFurry()) {
            messageList.addAll(Arrays.asList(
                    "Im sowwy UwU",
                    "*Angry bat noises*",
                    "I got my tail stuck in the door..."));
        }
        if (config.isNSFW()) {
            messageList.add("That's what you get for lewding me!");
        }
        return messageList.get(rando.nextInt(messageList.size()));
    }
}
