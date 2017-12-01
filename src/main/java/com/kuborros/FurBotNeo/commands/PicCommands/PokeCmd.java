/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import static com.kuborros.FurBotNeo.BotMain.db;
import com.kuborros.FurBotNeo.net.apis.PokemonApi;
import com.kuborros.FurBotNeo.net.apis.WebmPostException;
import com.kuborros.FurBotNeo.utils.msg.EmbedSender;
import java.awt.Color;
import net.dv8tion.jda.core.Permission;

/**
 *
 * @author Kuborros
 */
public class PokeCmd  extends Command{
   
    private Permission[] perms = {Permission.MESSAGE_EMBED_LINKS};
    
    public PokeCmd(){
        this.name = "poke";
        this.help = "Searches for _pictures_ on AGNPH";
        this.arguments = "<Tags>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.cooldown = 5;
        this.botPermissions = perms;
        this.category = new Category("ImageBoards"); 
        db.registerCommand(this.name);        
    }
    
    @Override
    protected void execute(CommandEvent event) {
        PokemonApi api;
        String result;
        EmbedSender emb = new EmbedSender(event);
        db.updateCommandStats(event.getAuthor().getId(), this.name);        

        if (!event.getTextChannel().isNSFW()){
            event.replyWarning("This command works only on NSFW channels! (For obvious reasons)");
            return;
        }
        
         if (!event.getArgs().isEmpty()){
                api = new PokemonApi("http://gallerhy.agn.ph/gallery/post/?search=" + event.getArgs().replaceAll(" ", "+") + "+order:random" + "&api=xml");
                try {
                result = api.PokeXml();
                } catch (IllegalArgumentException e){
                    e.printStackTrace();
                    event.reply("No results found!");
                    return;
                } catch (WebmPostException e){
                    event.reply("Only results are webms!");                    
                    return;
                }
                emb.sendPicEmbed("AGNPH", result , Color.PINK);
                } else {
                api = new PokemonApi("http://gallerhy.agn.ph/gallery/post/?api=xml");
                try {
                result = api.PokeXml();
                } catch (IllegalArgumentException e){
                    event.reply("No results found!");                    
                    return;
                } catch (WebmPostException e){
                    event.reply("Only results are webms!");                    
                    return;
                }
                emb.sendPicEmbed("AGNPH", result , Color.PINK);
                }  
        
    }

    
}
