/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.Permission;

/**
 *
 * @author Kuborros
 */
public class MusicPauseCmd extends MusicCommand{
    
    public MusicPauseCmd()
    {
        this.name = "pause";
        this.help = "pauses playback";
        this.guildOnly = true;       
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
        this.category = new Category("Music");  
       
}
    @Override
    public void doCommand(CommandEvent event){
        if (getPlayer(guild).isPaused()) {
            getPlayer(guild).setPaused(false);
            event.reply(NOTE + "Player resumed.");
        } else {
            getPlayer(guild).setPaused(true);
            event.reply(NOTE + "Player paused.");
        }
    } 
}