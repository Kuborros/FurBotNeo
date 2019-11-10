
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

@CommandInfo(
        name = "MusicPlayNext",
        description = "Same as MusicPlay, but track will be forced as next in queue."
)
@Author("Kuborros")
public class PlayNextCmd extends MusicCommand{
    
    public PlayNextCmd()
    {
        this.name = "playnext";
        this.arguments = "<title|URL>";
        this.help = "Add song to playlist and makes it the next song";
        this.guildOnly = true;        
        this.category = new Category("Music");  
}
    @Override
    public void doCommand(CommandEvent event){

        this.input = (input != null && input.startsWith("http")) ? input : "ytsearch: " + input;

        if (event.getArgs().isEmpty()) {
            event.replyWarning("Please include a valid source.");
        } else {
            loadTrackNext(input, event.getMember(), event.getMessage());      
    }
  } 
}
