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
        
        getTrackManager(guild).purgeQueue();
        forceSkipTrack(guild);
        event.getTextChannel().getManager().setTopic("Music stopped.").queue(); 
    }
}
