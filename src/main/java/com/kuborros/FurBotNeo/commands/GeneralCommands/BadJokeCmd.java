/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.GeneralCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 *
 * @author Kuborros
 */
public class BadJokeCmd extends GeneralCommand {

    private final Logger LOG = LoggerFactory.getLogger("CommandExec");
    
    public BadJokeCmd()
    {
        this.name = "joke";
        this.help = "Makes a joke about mentioned user or anything you type in!";
        this.arguments = "@user | <anything>";
        this.guildOnly = true;        
        this.category = new Command.Category("Basic"); 
}
    @Override
    public void doCommand(CommandEvent event) {
            Message message = event.getMessage();   
            TextChannel chat = event.getTextChannel();
           
            try {                
            URL u = new URL("http://api.icndb.com/jokes/random");
            URLConnection UC = u.openConnection();
            UC.setRequestProperty ( "User-agent", "DiscordBot/1.0");
            InputStream r = UC.getInputStream();

                StringBuilder str;
            try (Scanner scan = new Scanner(r)) {
                str = new StringBuilder();
                while (scan.hasNext()) {
                    str.append(scan.nextLine());
                }
            }
                JSONObject object = new JSONObject(str.toString());
                if (!"success".equals(object.getString("type"))) {
                    LOG.error("Joke code fucked up");
                }
            
                String joke = object.getJSONObject("value").getString("joke");
                String remainder = message.getContentDisplay().replaceFirst("!joke ", "");
            
                if(message.getMentionedUsers().size() > 0){
                    joke = joke.replaceAll("Chuck Norris", "<@"+message.getMentionedUsers().get(0).getId()+">");
                } else if (remainder.length() > 0){
                    joke = joke.replaceAll("Chuck Norris", remainder);
                }
            
                joke = joke.replaceAll("&quot;", "\"");
            
                chat.sendMessage(joke).queue();
            }
            catch (IOException | JSONException e) {     
                e.printStackTrace();
            }    
    }  
}
