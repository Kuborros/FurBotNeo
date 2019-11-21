package com.kuborros.FurBotNeo.commands.PicCommands;

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


abstract class PicCommand extends Command {

    private static final Logger LOG = LoggerFactory.getLogger("PicCommands");

    private static CommandClient client;
    private Guild guild;

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
        builder.setTitle(client.getError() + "Something went wrong!")
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
        if (config.isNSFW()) {
            doCommand(event);
        } else LOG.info("Image commands disabled by server owner, ignoring.");
    }

    protected abstract void doCommand(CommandEvent event);
}
