package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


abstract class PicCommand extends Command {

    static final Logger LOG = LoggerFactory.getLogger("PicCommands");
    @Override
    protected void execute(CommandEvent event) {
        doCommand(event);
    }

    protected abstract void doCommand(CommandEvent event);
}
