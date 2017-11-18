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
public class PlayCommand extends MusicCommand{
    
    public PlayCommand()
    {
        this.name = "play";
        this.arguments = "<title|URL|subcommand>";
        this.help = "Adds the song to current music queue";
        this.guildOnly = true;
        this.category = new Category("Music");  
}
    @Override
    public void doCommand(CommandEvent event){

        this.input = (input != null && input.startsWith("http")) ? input : "ytsearch: " + input;

        if (event.getArgs().isEmpty()) {
            event.replyError("Please include a valid source.");
        } else {
            loadTrack(input, event.getMember(), event.getMessage());
            if (getPlayer(guild).isPaused())
                getPlayer(guild).setPaused(false);
             }        
    }
    
}
