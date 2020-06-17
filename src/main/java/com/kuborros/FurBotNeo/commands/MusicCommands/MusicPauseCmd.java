
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

@CommandInfo(
        name = "MusicPause",
        description = "Pauses and unpauses current track."
)
@Author("Kuborros")
public class MusicPauseCmd extends MusicCommand{
    
    public MusicPauseCmd()
    {
        this.name = "pause";
        this.aliases = new String[]{"resume"};
        this.help = "Pauses music playback";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");

    }

    @Override
    public void doCommand(CommandEvent event) {
        if (isDJ) {
            if (getPlayer(guild).isPaused()) {
                getPlayer(guild).setPaused(false);
                event.reply(sendGenericEmbed("Player resumed!", ""));
            } else {
                getPlayer(guild).setPaused(true);
                event.reply(sendGenericEmbed("Player paused!", "(Please don't forget to unpause it later!)"));
            }
        } else {
            event.reply(sendFailEmbed("Only DJs can pause the tracks!", "That's just a bit too much power for everyone to hold~"));
        }
    }

}