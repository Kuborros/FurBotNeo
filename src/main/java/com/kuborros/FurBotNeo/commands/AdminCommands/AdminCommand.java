package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

abstract class AdminCommand extends Command {
    @Override
    protected void execute(CommandEvent event) {
        doCommand(event);
    }

    protected abstract void doCommand(CommandEvent event);
}

