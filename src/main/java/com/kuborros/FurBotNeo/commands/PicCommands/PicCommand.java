package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.kuborros.FurBotNeo.utils.config.FurConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static com.kuborros.FurBotNeo.BotMain.db;


abstract class PicCommand extends Command {

    private static final Logger LOG = LoggerFactory.getLogger("PicCommands");

    @Override
    protected void execute(CommandEvent event) {
        FurConfig config = (FurConfig) event.getClient().getSettingsManager().getSettings(event.getGuild());
        assert config != null;
        try {
            if (db.getBanStatus(event.getMember().getId(), event.getGuild().getId())) {
                event.reply("You are blocked from bot commands!");
                return;
            }
        } catch (SQLException e) {
            LOG.error("Error while contacting database: ", e);
        }
        if (config.isNSFW()) {
            doCommand(event);
        } else LOG.info("NSFW command ran on SFW server, ignoring");
    }

    protected abstract void doCommand(CommandEvent event);
}
