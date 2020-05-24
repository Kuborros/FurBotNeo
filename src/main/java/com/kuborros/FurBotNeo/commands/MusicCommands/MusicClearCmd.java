package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

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
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");
    }

    @Override
    public void doCommand(CommandEvent event) {


        if (isIdle(guild)) {
            event.reply(sendFailEmbed("There is no playlist to clear!", "Duh."));
            return;
        }
        getTrackManager(guild).clearQueue();
        event.reply(sendGenericEmbed("Playlist cleared!", ""));
    }
}