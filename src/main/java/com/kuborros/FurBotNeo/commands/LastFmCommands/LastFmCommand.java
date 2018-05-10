package com.kuborros.FurBotNeo.commands.LastFmCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.kuborros.FurBotNeo.BotMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.DateFormat;

import static com.kuborros.FurBotNeo.BotMain.db;


abstract class LastFmCommand extends Command {

    static final String key = BotMain.cfg.getLASTFM_KEY();
    private static final Logger LOG = LoggerFactory.getLogger("LastFMCommands");
    final DateFormat format = DateFormat.getDateInstance();

    @Override
    protected void execute(CommandEvent event) {
        try {
            if (db.getBanStatus(event.getMember().getUser().getId(), event.getGuild().getId())) {
                event.reply("You are blocked from bot commands!");
            }
        } catch (SQLException e) {
            LOG.error("Error while contacting database: ", e);
        }
        doCommand(event);
    }

    protected abstract void doCommand(CommandEvent event);
}
