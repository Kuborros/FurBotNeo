
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

@CommandInfo(
        name = "MusicSkip",
        description = "Skips currently played track."
)
@Author("Kuborros")
public class MusicSkipCmd extends MusicCommand{
    
    public MusicSkipCmd()
    {
        this.name = "skip";
        this.help = "Skips currently played track";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");          
}
    @Override
    public void doCommand(CommandEvent event){


        if (skipTrack(guild)) {
            event.reply(sendGenericEmbed("Skipped track!", "", ":fast_forward:"));
        }
    }
}