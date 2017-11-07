/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.jagrosh.jdautilities.waiter.EventWaiter;
import java.awt.Color;
import java.time.Instant;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kuborros
 */
public class VoteCommand extends Command{
    
private final EventWaiter waiter;
private Logger LOG = LoggerFactory.getLogger("VoteCommand");    

        
        public VoteCommand(EventWaiter waiter)         
    {
        this.name = "vote";
        this.waiter = waiter;
        this.help = "Shows info about specific user";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Command.Category("Moderation");
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
    }

    @Override
    protected void execute(CommandEvent event) {
        event.replySuccess("Alright! Let's set up your vote! Type in how long the vote should last!");
        waitForTime(event, event.getTextChannel());
    }
                  
    
    private void waitForTime(CommandEvent event, TextChannel tchan)
    {
        waiter.waitForEvent(GuildMessageReceivedEvent.class, 
                e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()), 
                e -> {
                    if(e.getMessage().getRawContent().equalsIgnoreCase("cancel"))
                    {
                        event.replyWarning("Alright, I guess we're not having a vote after all...");
                        event.getMessage().delete().queue();
                        e.getMessage().delete().queue();
                    }
                    else
                    {
                        String val = e.getMessage().getRawContent().toUpperCase().trim();
                        boolean min = false;
                        if(val.endsWith("M"))
                        {
                            min=true;
                            val=val.substring(0,val.length()-1).trim();
                        }
                        else if(val.endsWith("S"))
                        {
                            val=val.substring(0,val.length()-1).trim();
                        }
                        int seconds;
                        try {
                            seconds = (min?60:1)*Integer.parseInt(val);
                            if(seconds<10 || seconds>60*60*24*7)
                            {
                                event.replyWarning("Sorry! Votes need to be at least 10 seconds long, and can't be _too_ long.");
                                event.getMessage().delete().queue();
                                e.getMessage().delete().queue();
                                waitForTime(event, tchan);
                            }
                            else
                            {
                                event.replySuccess("This vote will last "+secondsToTime(seconds)+"! Now, what is it going to be about?");
                                event.getMessage().delete().queue();
                                e.getMessage().delete().queue();
                                waitForTopic(event, tchan, seconds);
                            }
                        } catch(NumberFormatException ex) {
                            event.replyWarning("Hm. I can't seem to get a number from that.");
                            e.getMessage().delete().queue();
                            waitForTime(event, tchan);
                        }
                    }
                }, 
                2, TimeUnit.MINUTES, () -> event.replyWarning("You took longer than 2 minutes to respond, "+event.getAuthor().getAsMention()+"!"));
}

      private void waitForTopic(CommandEvent event, TextChannel tchan, int seconds)
    {
        waiter.waitForEvent(GuildMessageReceivedEvent.class, 
                e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()), 
                e -> {
                    if(e.getMessage().getRawContent().equalsIgnoreCase("cancel"))
                    {
                        event.replyWarning("Alright, I guess we're not having a vote after all...");
                        e.getMessage().delete().queue();
                    }
                    else
                    {
                        String prize = e.getMessage().getRawContent();
                        if(prize.length()>500)
                        {
                            event.replyWarning("That topic is too long. Can you shorten it a bit?");
                            e.getMessage().delete().queue();
                            waitForTopic(event, tchan, seconds);
                        }
                        else
                        {
                            Instant now = Instant.now();
                            if(startVote(tchan, now, seconds, prize))
                            {
                                e.getMessage().delete().queue();
                            }
                            else
                            {
                                event.replyError("Uh oh. Something went wrong and I wasn't able to start the vote.");
                                e.getMessage().delete().queue();
                            }
                        }
                    }
                }, 
                2, TimeUnit.MINUTES, () -> event.replyWarning("You took longer than 2 minutes to respond, "+event.getAuthor().getAsMention()+"!"));
}
    
private boolean startVote(TextChannel channel, Instant now, int seconds, String prize){
            MessageEmbed msg = new EmbedBuilder().setTitle("**Vote**").setDescription(prize).setTimestamp(now).setColor(Color.BLUE).addField("", "Vote will end in: " + secondsToTime(seconds) + "!", false).build();
            channel.sendMessage(msg).queue(m -> {
            m.addReaction("\u2705").queue();
            m.addReaction("\u274E").queue(); 
            Timer timer = new Timer("VoteTimer");
            TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                String mID = m.getId();
                List<MessageReaction> ayy = channel.getMessageById(mID).complete().getReactions();
                int check = ayy.get(0).getCount() - 1;
                int cross = ayy.get(1).getCount() - 1;
                Color col = (check >= cross ? Color.GREEN : Color.RED);
                MessageEmbed msg = new EmbedBuilder().setTitle("**Vote results!**").setDescription(
                          "The results are: \n"
                        + "\u2705 :  **" + check + "**\n"
                        + "\u274E :  **" + cross + "**\n"
                ).addField("", "Vote's topic was: \"" + prize + "\" !", false).setColor(col).build();
                channel.sendMessage(msg).complete();
                channel.getMessageById(mID).complete().delete().complete();
                timer.cancel();
            }
        };

        timer.schedule(timerTask, TimeUnit.SECONDS.toMillis(seconds));
            });
        return true;    
}    
        
 static String secondsToTime(long timeseconds) {
        StringBuilder builder = new StringBuilder();
        int years = (int)(timeseconds / (60*60*24*365));
        if(years>0){
            builder.append("**").append(years).append("** years, ");
            timeseconds %= (60*60*24*365);
        }
        int weeks = (int)(timeseconds / (60*60*24*365));
        if(weeks>0){
            builder.append("**").append(weeks).append("** weeks, ");
            timeseconds %= (60*60*24*7);
        }
        int days = (int)(timeseconds / (60*60*24));
        if(days>0){
            builder.append("**").append(days).append("** days, ");
            timeseconds %= (60*60*24);
        }
        int hours = (int)(timeseconds / (60*60));
        if(hours>0){
            builder.append("**").append(hours).append("** hours, ");
            timeseconds %= (60*60);
        }
        int minutes = (int)(timeseconds / (60));
        if(minutes>0){
            builder.append("**").append(minutes).append("** minutes, ");
            timeseconds %= (60);
        }
        if(timeseconds>0)
            builder.append("**").append(timeseconds).append("** seconds");
        String str = builder.toString();
        if(str.endsWith(", "))
            str = str.substring(0,str.length()-2);
        if(str.isEmpty())
            str="**No time**";
        return str;
}        
}
