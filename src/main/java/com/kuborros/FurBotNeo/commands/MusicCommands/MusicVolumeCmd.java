
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

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
        this.hidden = true;
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