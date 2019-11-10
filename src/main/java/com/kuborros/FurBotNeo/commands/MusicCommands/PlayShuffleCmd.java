
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

@CommandInfo(
        name = "MusicPlayShuffle",
        description = "Same as MusicPlay, but queue gets randomised."
)
@Author("Kuborros")
public class PlayShuffleCmd extends MusicCommand{
    
    public PlayShuffleCmd()
    {
        this.name = "playshuffle";
        this.arguments = "<title|URL>";
        this.help = "Adds song to playlist then shuffles it";
        this.guildOnly = true;        
        this.category = new Category("Music");  
}
    @Override
    public void doCommand(CommandEvent event){

        this.input = (input != null && input.startsWith("http")) ? input : "ytsearch: " + input;

        if (event.getArgs().isEmpty()) {
            event.replyWarning("Please include a valid source.");
        } else {
            loadTrack(input, event.getMember(), event.getMessage());

            getTrackManager(guild).shuffleQueue();

            if (getPlayer(guild).isPaused())
                getPlayer(guild).setPaused(false);
            }
    }
}