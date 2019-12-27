
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

@CommandInfo(
        name = "MusicReset",
        description = "Completely restarts playback system of guild."
)
@Author("Kuborros")
public class MusicResetCmd extends MusicCommand{
    
    public MusicResetCmd()
    {
        this.name = "mreset";
        this.help = "Performs a full player reset";
        this.ownerCommand = true;
        this.guildOnly = true;        
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");          
}
    @Override
    public void doCommand(CommandEvent event){
        reset(guild);
        event.reply(sendGenericEmbed("Resetting the music player!", "", "\uD83D\uDD04"));
        event.getTextChannel().getManager().setTopic("Music player has been restarted.").queue();
    }
}