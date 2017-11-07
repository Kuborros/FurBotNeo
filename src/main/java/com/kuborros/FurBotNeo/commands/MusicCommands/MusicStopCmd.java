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
public class MusicStopCmd extends MusicCommand{
    
    public MusicStopCmd()
    {
        this.name = "stop";
        this.help = "stops the provided song";
        this.guildOnly = true;
        this.category = new Category("Music");          
}
    @Override
    public void doCommand(CommandEvent event){
        
        getTrackManager(guild).purgeQueue();
        forceSkipTrack(guild);
        guild.getAudioManager().closeAudioConnection();
        event.getTextChannel().getManager().setTopic("Music stopped.").queue(); 
    }
}
