/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.kuborros.FurBotNeo.net.apis.DanApi;
import com.kuborros.FurBotNeo.net.apis.WebmPostException;
import com.kuborros.FurBotNeo.utils.msg.EmbedSender;
import java.awt.Color;
import java.util.List;
import net.dv8tion.jda.core.Permission;
import org.json.JSONException;

/**
 *
 * @author Kuborros
 */
public class DanCmd extends Command{
   
    private Permission[] perms = {Permission.MESSAGE_EMBED_LINKS};
    
    public DanCmd(){
        this.name = "dan";
        this.help = "Searches for _pictures_ on DanBooru";
        this.arguments = "<2 Tags>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.cooldown = 5;
        this.botPermissions = perms;
        this.category = new Category("ImageBoards");         
    }
    
    @Override
    protected void execute(CommandEvent event) {
        DanApi api;
        List<String> result;
        EmbedSender emb = new EmbedSender(event);

        if (!event.getTextChannel().isNSFW()){
            event.replyWarning("This command works only on NSFW channels! (For obvious reasons)");
            return;
        }
        
         if (!event.getArgs().isEmpty()){
                api = new DanApi("https://danbooru.donmai.us/posts.json?tags=" + event.getArgs().replaceAll(" ", "+") + "&random=true&limit=1");
                try {
                result = api.getDanPic();
                } catch (JSONException e){
                    event.reply("No results found!");
                    return;
                } catch (WebmPostException e){
                    event.reply("Only results are webms!");                    
                    return;
                }
                emb.sendPicEmbed("DanBooru", result.get(0) , "Author: " + result.get(1) , Color.PINK);
                } else {
                api = new DanApi("https://danbooru.donmai.us/posts.json?random=true&limit=1");
                try {
                result = api.getDanPic();
                } catch (JSONException e){
                    event.reply("No results found!");                    
                    return;
                } catch (WebmPostException e){
                    event.reply("Only results are webms!");                    
                    return;
                }
                emb.sendPicEmbed("DanBooru", result.get(0) , "Author: " + result.get(1) , Color.PINK);
                }  
        
    }
}