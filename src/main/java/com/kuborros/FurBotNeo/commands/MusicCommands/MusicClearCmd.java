package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

@CommandInfo(
        name = "MusicClear",
        description = "Clears the play queue."
)
@Author("Kuborros")
public class MusicClearCmd extends MusicCommand {

    public MusicClearCmd() {
        this.name = "clear";
        this.help = "Clear the playlist";
        this.guildOnly = true;
        this.category = new Category("Music");
    }

    @Override
    public void doCommand(CommandEvent event) {
        if (isIdle(guild)) {
            event.reply("There is no playlist to clear!");
            return;
        }
        getTrackManager(guild).clearQueue();
        event.reply(NOTE + "Cleared queue!");
    }
}