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
public class MusicResetCmd extends MusicCommand{
    
    public MusicResetCmd()
    {
        this.name = "mreset";
        this.help = "resets the player";
        this.ownerCommand = true;
        this.guildOnly = true;        
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
        this.category = new Category("Music");          
}
    @Override
    public void doCommand(CommandEvent event){
        reset(guild);
        event.reply("\uD83D\uDD04 Resetting the music player..");
        event.getTextChannel().getManager().setTopic("Player restarted.").queue();   
    }
}