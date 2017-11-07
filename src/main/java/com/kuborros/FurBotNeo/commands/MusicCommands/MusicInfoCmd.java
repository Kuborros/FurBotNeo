/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import java.awt.Color;
import net.dv8tion.jda.core.EmbedBuilder;

/**
 *
 * @author Kuborros
 */
public class MusicInfoCmd extends MusicCommand{
    
    public MusicInfoCmd()
    {
        this.name = "music";
        this.aliases = new String[]{"current","musicinfo"};
        this.help = "info about playback";
        this.guildOnly = true;        
        this.category = new Category("Music"); 
}
    @Override
    public void doCommand(CommandEvent event){

        if (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null) {
            event.getTextChannel().sendMessage(NOTE + "No music currently playing!").queue(); 
        } else {
            AudioTrack track = getPlayer(guild).getPlayingTrack();
            AudioTrackInfo info = track.getInfo();
            EmbedBuilder eb = new EmbedBuilder();
                            eb
                                    .setColor(Color.orange)
                                    .setDescription(":musical_note:   **Current Track Info**")
                                    .addField(":cd:  Title", info.title, false)
                                    .addField(":stopwatch:  Duration", "`[ " + getTimestamp(track.getPosition()) + " / " + getTimestamp(track.getInfo().length) + " ]`", false)
                                    .addField(":microphone:  Channel / Author", info.author, false);
            event.reply(eb.build());
            }
    }
        
}
