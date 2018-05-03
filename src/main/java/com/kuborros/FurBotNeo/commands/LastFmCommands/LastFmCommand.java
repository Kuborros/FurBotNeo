package com.kuborros.FurBotNeo.commands.LastFmCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.kuborros.FurBotNeo.BotMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;


abstract class LastFmCommand extends Command {

    static final Logger LOG = LoggerFactory.getLogger("LastFm");
    static String key = BotMain.cfg.getLASTFM_KEY();
    DateFormat format;

    LastFmCommand() {
        format = DateFormat.getDateInstance();
    }

    @Override
    protected void execute(CommandEvent event) {
        doCommand(event);
    }

    protected abstract void doCommand(CommandEvent event);
}
