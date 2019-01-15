package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static com.kuborros.FurBotNeo.BotMain.cfg;
import static com.kuborros.FurBotNeo.BotMain.db;

abstract class AdminCommand extends Command {

    static final Logger LOG = LoggerFactory.getLogger("AdminCommands");

    @Override
    protected void execute(CommandEvent event) {
        if (!event.getMember().isOwner() || !event.getMember().getUser().getId().equals(cfg.getOWNER_ID())) {
            try {
                if (db.getBanStatus(event.getMember().getUser().getId(), event.getGuild().getId())) {
                    event.reply("You are blocked from bot commands!");
                }
            } catch (SQLException e) {
                LOG.error("Error while contacting database: ", e);
            }
        }
        doCommand(event);
    }

    protected abstract void doCommand(CommandEvent event);
}
