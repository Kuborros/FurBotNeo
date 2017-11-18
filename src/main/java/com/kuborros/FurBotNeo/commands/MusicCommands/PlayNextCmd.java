/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

/**
 *
 * @author Kuborros
 */
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
            event.replyError("Please include a valid source.");
        } else {
            loadTrackNext(input, event.getMember(), event.getMessage());      
    }
  } 
}
