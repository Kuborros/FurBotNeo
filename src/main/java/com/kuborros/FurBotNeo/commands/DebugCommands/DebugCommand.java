package com.kuborros.FurBotNeo.commands.DebugCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.kuborros.FurBotNeo.utils.store.MemberInventory;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.kuborros.FurBotNeo.BotMain.cfg;
import static com.kuborros.FurBotNeo.BotMain.inventoryCache;

abstract class DebugCommand extends Command {

    static final Logger LOG = LoggerFactory.getLogger("DebugCommands");

    protected Guild guild;
    protected MemberInventory inventory;

    //Purely testing commands, as such most of the usual fancy stuff gets skipped here.
    @Override
    protected void execute(CommandEvent event) {
        if (event.getAuthor().isBot() || !cfg.isDebugMode()) return;
        guild = event.getGuild();
        inventory = inventoryCache.getInventory(event.getMember().getId(), guild.getId());
        doCommand(event);
    }

    protected abstract void doCommand(CommandEvent event);
}

