package com.kuborros.FurBotNeo.commands.LewdCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.kuborros.FurBotNeo.utils.config.FurConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.SQLException;

import static com.kuborros.FurBotNeo.BotMain.db;
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
        try {
            if (db.getBanStatus(event.getMember().getId(), guild.getId())) {
                event.reply(bannedResponseEmbed());
                return;
            }
        } catch (SQLException e) {
            LOG.error("Error while contacting database: ", e);
        }


        if (!config.isNSFW()) return;

        isFurry = config.isFurry();

        if (event.getTextChannel().isNSFW()) doCommand(event);
        else event.replyWarning(randomResponse.getRandomNotNSFWMessage());
    }

    protected abstract void doCommand(CommandEvent event);
}
