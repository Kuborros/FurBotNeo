/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.kuborros.FurBotNeo.net.apis.PoniApi;
import com.kuborros.FurBotNeo.net.apis.WebmPostException;
import com.kuborros.FurBotNeo.utils.msg.EmbedSender;
import java.awt.Color;
import net.dv8tion.jda.core.Permission;
import org.json.JSONException;

/**
 *
 * @author Kuborros
 */
public class PoniCmd extends Command{
   
    private Permission[] perms = {Permission.MESSAGE_EMBED_LINKS};
    
    public PoniCmd(){
        this.name = "poni";
        this.help = "Pony stuff";
        this.arguments = "<Tags>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.cooldown = 5;
        this.botPermissions = perms;
        this.category = new Category("ImageBoards");         
    }
    
    @Override
    protected void execute(CommandEvent event) {
        PoniApi api;
        String result;
        EmbedSender emb = new EmbedSender(event);
        
         if (!event.getArgs().isEmpty()){
                api = new PoniApi("https://derpibooru.org/search.json?q=" + event.getArgs().replaceAll(" ", ",") + "&sf=random&filter_id=37432");
                try {
                result = api.getPoniPic();
                } catch (JSONException e){
                    e.printStackTrace();
                    event.reply("No results found!");
                    return;
                } catch (WebmPostException e){
                    event.reply("Only results are webms!");                    
                    return;
                }
                emb.sendPicEmbed("DerpiBooru", result , Color.PINK);
                } else {
                api = new PoniApi("https://derpibooru.org/search.json?q=*&sf=random&filter_id=37432");
                try {
                result = api.getPoniPic();
                } catch (JSONException e){
                    event.reply("No results found!");                    
                    return;
                } catch (WebmPostException e){
                    event.reply("Only results are webms!");                    
                    return;
                }
                emb.sendPicEmbed("DerpiBooru", result , Color.PINK);
                }  
        
    }
}