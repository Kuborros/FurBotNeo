package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.kuborros.FurBotNeo.utils.config.FurConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


abstract class PicCommand extends Command {

    private static final Logger LOG = LoggerFactory.getLogger("PicCommands");

    @Override
    protected void execute(CommandEvent event) {
        FurConfig config = (FurConfig) event.getClient().getSettingsManager().getSettings(event.getGuild());
        assert config != null;
        if (config.isNSFW()) {
            doCommand(event);
        } else LOG.debug("NSFW command ran on SFW server, ignoring");
    }

    protected abstract void doCommand(CommandEvent event);
}
