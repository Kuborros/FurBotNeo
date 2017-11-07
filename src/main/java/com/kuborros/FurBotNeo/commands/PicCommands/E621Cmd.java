

package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.kuborros.FurBotNeo.net.apis.E621Api;
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
public class E621Cmd extends Command{
   
    private Permission[] perms = {Permission.MESSAGE_EMBED_LINKS};
    
    public E621Cmd(){
        this.name = "e621";
        this.help = "Furry stuff";
        this.arguments = "<Tags>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.cooldown = 5;
        this.botPermissions = perms;
        this.category = new Category("ImageBoards");         
    }
    
    @Override
    protected void execute(CommandEvent event) {
        E621Api api;
        List<String> result;
        EmbedSender emb = new EmbedSender(event);
        
         if (!event.getArgs().isEmpty()){
                api = new E621Api("https://e621.net/post/index.json?tags=" + event.getArgs().replaceAll(" ", "+") + "+order:random+-type:webm" + "&limit=1");
                try {
                result = api.getFurryPic();
                } catch (JSONException e){
                    e.printStackTrace();
                    event.reply("No results found!");
                    return;
                } catch (WebmPostException e){
                    event.reply("Only results are webms!");                    
                    return;
                }
                emb.sendPicEmbed("E621", result.get(0) , "Author: " + result.get(1) , Color.PINK);
                } else {
                api = new E621Api("https://e621.net/post/index.json?tags=" + "rating:a+order:random+-type:webm" + "&limit=1");
                try {
                result = api.getFurryPic();
                } catch (JSONException e){
                    event.reply("No results found!");                    
                    return;
                } catch (WebmPostException e){
                    event.reply("Only results are webms!");                    
                    return;
                }
                emb.sendPicEmbed("E621", result.get(0) , "Author: " + result.get(1) , Color.PINK);
                }  
        
    }

}
