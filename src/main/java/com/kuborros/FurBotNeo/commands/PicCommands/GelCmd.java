/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.kuborros.FurBotNeo.net.apis.GelEngine;
import com.kuborros.FurBotNeo.net.apis.WebmPostException;
import com.kuborros.FurBotNeo.utils.msg.EmbedSender;
import java.awt.Color;
import net.dv8tion.jda.core.Permission;
import org.json.JSONException;

/**
 *
 * @author Kuborros
 */
public class GelCmd extends Command{
   
    private Permission[] perms = {Permission.MESSAGE_EMBED_LINKS};
    
    public GelCmd(){
        this.name = "gel";
        this.help = "Searches for _pictures_ on GelBooru";
        this.arguments = "<Tags>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.cooldown = 5;
        this.botPermissions = perms;
        this.category = new Category("ImageBoards");         
    }
    
    @Override
    protected void execute(CommandEvent event) {
        GelEngine api;
        String result;
        EmbedSender emb = new EmbedSender(event);

        if (!event.getTextChannel().isNSFW()){
            event.replyWarning("This command works only on NSFW channels! (For obvious reasons)");
            return;
        }
        
         if (!event.getArgs().isEmpty()){
                api = new GelEngine("https://gelbooru.com/index.php?page=dapi&s=post&q=index&tags=" + event.getArgs().replaceAll(" ", "+") + "&limit=100");
                try {
                result = api.getGelPic();
                } catch (JSONException e){
                    event.reply("No results found!");
                    return;
                } catch (WebmPostException e){
                    event.reply("Only results are webms!");                    
                    return;
                }
                emb.sendPicEmbed("GelBooru", result , Color.PINK);
                } else {
                api = new GelEngine("https://gelbooru.com/index.php?page=dapi&s=post&q=index&limit=100");
                try {
                result = api.getGelPic();
                } catch (JSONException e){
                    event.reply("No results found!");                    
                    return;
                } catch (WebmPostException e){
                    event.reply("Only results are webms!");                    
                    return;
                }
                emb.sendPicEmbed("GelBooru", result , Color.PINK);
                }  
        
    }

    
}
