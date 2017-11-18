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
public class MusicShuffleCmd extends MusicCommand{
    
    public MusicShuffleCmd()
    {
        this.name = "shuffle";
        this.help = "Shuffles the playlist";
        this.guildOnly = true;        
        this.category = new Category("Music");         
}
    @Override
    public void doCommand(CommandEvent event){
        if (isIdle(guild, event)) return;
        
        getTrackManager(guild).shuffleQueue();
        event.getTextChannel().sendMessage(NOTE + "Shuffled queue.  :twisted_rightwards_arrows: ").queue();
    }
}