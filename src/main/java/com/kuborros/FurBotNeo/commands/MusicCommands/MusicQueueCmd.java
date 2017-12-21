/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.kuborros.FurBotNeo.utils.audio.AudioInfo;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Kuborros
 */
public class MusicQueueCmd extends MusicCommand{
    
    public MusicQueueCmd()
    {
        this.name = "queue";
        this.aliases = new String[]{"playlist"};
        this.help = "Shows current playlist";
        this.category = new Category("Music");         
}
    @Override
    public void doCommand(CommandEvent event){
     
        EmbedBuilder eb = new EmbedBuilder();    

        if (!hasPlayer(guild) || getTrackManager(guild).getQueuedTracks().isEmpty()) {
            event.reply(NOTE + "The queue is currently empty!");
        } else {

            int SideNumbInput = 1;
            if (!event.getArgs().isEmpty())
                SideNumbInput = Integer.parseInt(event.getArgs());
            
            StringBuilder sb = new StringBuilder();
            Set<AudioInfo> queue = getTrackManager(guild).getQueuedTracks();
            ArrayList<String> tracks = new ArrayList<>();
            List<String> tracksSublist;
            queue.forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

            if (tracks.size() > 20)
                tracksSublist = tracks.subList((SideNumbInput-1)*20, (SideNumbInput-1)*20+20);
            else
                tracksSublist = tracks;

            tracksSublist.forEach(sb::append);
            int sideNumbAll = tracks.size() >= 20 ? tracks.size() / 20 : 1;
            int sideNumb = SideNumbInput;

            eb.setColor(Color.GREEN).setDescription(
                    NOTE + "**QUEUE**\n\n" +
                    "*[" + queue.size() + " Tracks | Side " + sideNumb + "/" + sideNumbAll + "]*\n\n" +
                    sb
            );

            event.reply(eb.build());

        }
    }
}