
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

@CommandInfo(
        name = "MusicVolume",
        description = "Sets playback volume per-guild (values over 150 can cause distortion)."
)
@Author("Kuborros")
public class MusicVolumeCmd extends MusicCommand{
    
    public MusicVolumeCmd()
    {
        this.name = "volume";
        this.arguments = "<Volume>";
        this.help = "Sets the playback volume";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");  
}
    @Override
    public void doCommand(CommandEvent event){
        if (event.getArgs().isEmpty()) {
            event.replyWarning("Please set valid volume! <0-1000>.");
        } else {
            int vol = 100;
            try {
                vol = Integer.decode(event.getArgs());
                }
            catch (NumberFormatException e) {
                event.replyWarning("Please set valid volume! <0-1000>.");
            }
            setVolume(guild,vol);
            event.reply(String.format("Volume set to: %d", getPlayer(guild).getVolume()));
            }
    }
}