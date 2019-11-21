package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.SQLException;

import static com.kuborros.FurBotNeo.BotMain.*;

abstract class AdminCommand extends Command {

    static final Logger LOG = LoggerFactory.getLogger("AdminCommands");

    Guild guild;
    private CommandClient client;

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

    private MessageEmbed errorResponseEmbed(String ex) {
        String random = randomResponse.getRandomErrorMessage(guild);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(client.getError() + "Internal error has occured! ")
                .setDescription(random)
                .addField("Error details: ", "`` " + ex + " ``", false)
                .setColor(Color.RED);
        return builder.build();
    }

    @Override
    protected void execute(CommandEvent event) {
        guild = event.getGuild();
        client = event.getClient();
        if (event.getChannelType() == ChannelType.TEXT) {
            if (!event.getMember().isOwner() || !event.getMember().getId().equals(cfg.getOWNER_ID())) {
                try {
                    if (db.getBanStatus(event.getMember().getId(), guild.getId())) {
                        event.reply(bannedResponseEmbed());
                        return;
                    }
                } catch (SQLException e) {
                    LOG.error("Error while contacting database: ", e);
                }
            }
        }
        doCommand(event);
    }

    protected abstract void doCommand(CommandEvent event);
}

