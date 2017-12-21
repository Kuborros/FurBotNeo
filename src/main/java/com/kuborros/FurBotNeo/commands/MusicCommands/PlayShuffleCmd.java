/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;

/**
 *
 * @author Kuborros
 */
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
            event.replyError("Please include a valid source.");
        } else {
            loadTrack(input, event.getMember(), event.getMessage());

            getTrackManager(guild).shuffleQueue();

            if (getPlayer(guild).isPaused())
                getPlayer(guild).setPaused(false);
            }
    }
}