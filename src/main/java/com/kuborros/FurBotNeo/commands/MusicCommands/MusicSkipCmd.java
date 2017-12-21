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
public class MusicSkipCmd extends MusicCommand{
    
    public MusicSkipCmd()
    {
        this.name = "skip";
        this.help = "Skips song";
        this.guildOnly = true;
        this.category = new Category("Music");          
}
    @Override
    public void doCommand(CommandEvent event){
                forceSkipTrack(guild);        
    }
}