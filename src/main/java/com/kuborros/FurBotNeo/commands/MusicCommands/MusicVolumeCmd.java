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
public class MusicVolumeCmd extends MusicCommand{
    
    public MusicVolumeCmd()
    {
        this.name = "volume";
        this.arguments = "<Volume>";
        this.help = "Sets the playback volume";
        this.guildOnly = true;
        this.hidden = true;
        this.category = new Category("Music");  
}
    @Override
    public void doCommand(CommandEvent event){
        if (event.getArgs().isEmpty()) {
            event.replyError("Please set valid volume! <0-1000>.");
        } else {
            int vol = 100;
            try {
                vol = Integer.decode(event.getArgs());
                }
            catch (NumberFormatException e) {
                event.replyError("Please set valid volume! <0-1000>.");
            }
            setVolume(guild,vol);
            event.reply(String.format("Volume set to: %d", getPlayer(guild).getVolume()));
            }
    }
}