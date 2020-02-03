package com.kuborros.FurBotNeo.commands.LewdCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.kuborros.FurBotNeo.utils.config.FurConfig;
import com.kuborros.FurBotNeo.utils.store.MemberInventory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;
import static com.kuborros.FurBotNeo.BotMain.randomResponse;


abstract class LewdCommand extends Command {

    protected static final Logger LOG = LoggerFactory.getLogger("LewdCommands");

    private static CommandClient client;
    protected Guild guild;

    protected boolean isFurry;

    private MessageEmbed bannedResponseEmbed() {
        String random = randomResponse.getRandomDeniedMessage(guild);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("You are blocked from bot commands!")
                .setDescription(random)
                .setColor(Color.ORANGE);
        return builder.build();
    }

    MessageEmbed errorResponseEmbed(String message, Exception exception) {
        return errorResponseEmbed(message, exception.getLocalizedMessage());
    }

    MessageEmbed errorResponseEmbed(Exception exception) {
        return errorResponseEmbed("Something went wrong!", exception.getLocalizedMessage());
    }


    private MessageEmbed errorResponseEmbed(String message, String ex) {
        String random = randomResponse.getRandomErrorMessage(guild);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(client.getError() + message)
                .setDescription(random)
                .addField("Error details: ", "`` " + ex + " ``", false)
                .setColor(Color.RED);
        return builder.build();
    }

    @Override
    protected void execute(CommandEvent event) {
        guild = event.getGuild();
        client = event.getClient();
        FurConfig config = (FurConfig) event.getClient().getSettingsManager().getSettings(guild);
        assert config != null;
        MemberInventory inventory = inventoryCache.getInventory(event.getMember().getId(), guild.getId());
        if (inventory.isBanned()) {
            event.reply(bannedResponseEmbed());
            return;
        }

        if (!config.isNSFW()) return;

        isFurry = config.isFurry();

        if (event.getTextChannel().isNSFW()) {
            //Token award per command use. Inventories are not likely to be used in these commands, so they are not kept around
            //Should be tweaked later
            inventoryCache.setInventory(inventory.addTokens(10));
            doCommand(event);
        }
        //No tokens if command is used on sfw channel
        else event.replyWarning(randomResponse.getRandomNotNSFWMessage());
    }

    protected abstract void doCommand(CommandEvent event);
}
