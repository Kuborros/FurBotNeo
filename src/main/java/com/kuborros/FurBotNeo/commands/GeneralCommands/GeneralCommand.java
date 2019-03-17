package com.kuborros.FurBotNeo.commands.GeneralCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static com.kuborros.FurBotNeo.BotMain.db;

abstract class GeneralCommand extends Command {

    static final Logger LOG = LoggerFactory.getLogger("MainCommands");
    @Override
    protected void execute(CommandEvent event) {
        try {
            if (db.getBanStatus(event.getMember().getUser().getId(), event.getGuild().getId())) {
                event.reply("You are blocked from bot commands!");
                return;
            }
        } catch (SQLException e) {
            LOG.error("Error while contacting database: ", e);
        }
        doCommand(event);
    }

    protected abstract void doCommand(CommandEvent event);
}
