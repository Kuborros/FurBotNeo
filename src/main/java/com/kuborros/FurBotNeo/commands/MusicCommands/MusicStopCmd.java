
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

@CommandInfo(
        name = "MusicStop",
        description = "Stops current track, and clears the queue."
)
@Author("Kuborros")
public class MusicStopCmd extends MusicCommand{
    
    public MusicStopCmd()
    {
        this.name = "stop";
        this.help = "Completely stops music playback";
        this.guildOnly = true;
        this.category = new Category("Music");          
}
    @Override
    public void doCommand(CommandEvent event){

        getTrackManager(guild).purgeStopQueue();
        forceSkipTrack(guild);
        event.getTextChannel().getManager().setTopic("Music stopped.").queue(); 
    }
}
