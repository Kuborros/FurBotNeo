package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.kuborros.FurBotNeo.utils.store.MemberInventory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;
import static com.kuborros.FurBotNeo.BotMain.randomResponse;

abstract class AdminCommand extends Command {

    static final Logger LOG = LoggerFactory.getLogger("AdminCommands");

    protected Guild guild;
    protected CommandClient client;

    private MessageEmbed bannedResponseEmbed() {
        String random = randomResponse.getRandomDeniedMessage(guild);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("You are blocked from bot commands!")
                .setDescription(random)
                .setColor(Color.ORANGE);
        return builder.build();
    }

    MessageEmbed errorResponseEmbed(Exception exception) {
        return errorResponseEmbed(exception.getLocalizedMessage());
    }

    MessageEmbed errorResponseEmbed(String ex) {
        String random = randomResponse.getRandomErrorMessage(guild);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(client.getError() + "Internal error has occurred! ")
                .setDescription(random)
                .addField("Error details: ", "`` " + ex + " ``", false)
                .setColor(Color.RED);
        return builder.build();
    }


    //Being service commands meant for admins, these do not award tokens for use.
    @Override
    protected void execute(CommandEvent event) {
        guild = event.getGuild();
        client = event.getClient();
        if (event.getChannelType() == ChannelType.TEXT) {
            MemberInventory inventory = inventoryCache.getInventory(event.getMember().getId(), guild.getId());
            //Make sure we allow bot owner to do commands anyways
            if (inventory.isBanned() && !event.isOwner()) {
                event.reply(bannedResponseEmbed());
                return;
            }
        }
        doCommand(event);
    }

    protected abstract void doCommand(CommandEvent event);
}

