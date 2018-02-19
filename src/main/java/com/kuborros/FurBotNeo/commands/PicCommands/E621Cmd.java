

package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Slideshow;
import com.kuborros.FurBotNeo.net.apis.E621Api;
import com.kuborros.FurBotNeo.net.apis.NoImgException;
import net.dv8tion.jda.core.Permission;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.kuborros.FurBotNeo.BotMain.db;

/**
 *
 * @author Kuborros
 */
public class E621Cmd extends Command{

    private final EventWaiter waiter;
    
    public E621Cmd(EventWaiter waiter){
        this.name = "e621";
        this.help = "Searches for _pictures_ on E621";
        this.arguments = "<Tags>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.cooldown = 5;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.MANAGE_EMOTES};
        this.category = new Category("ImageBoards");
        this.waiter = waiter;
        db.registerCommand(this.name);

    }
    
    @Override
    protected void execute(CommandEvent event) {
        E621Api api;
        List<String> result;
        Slideshow.Builder builder = new Slideshow.Builder();
        db.updateCommandStats(event.getAuthor().getId(), this.name);

        if (!event.getTextChannel().isNSFW()){
            event.replyWarning("This command works only on NSFW channels! (For obvious reasons)");
            return;
        }

        builder.allowTextInput(false)
                .setBulkSkipNumber(5)
                .waitOnSinglePage(false)
                .setColor(Color.PINK)
                .setEventWaiter(waiter)
                .setText("")
                .setDescription("E621")
                .setFinalAction(message -> message.clearReactions().queue())
                .setTimeout(5, TimeUnit.MINUTES);


        if (!event.getArgs().isEmpty()){
            api = new E621Api("https://e621.net/post/index.json?tags=" + event.getArgs().replaceAll(" ", "+") + "+order:random&limit=20");
         } else {
            api = new E621Api("https://e621.net/post/index.json?tags=" + "rating:a+order:random+-flash&limit=20");
         }
                try {
                result = api.getFurryPic();
                builder.setUrls(result.toArray(new String[result.size()]));
                } catch (NoImgException e) {
                    event.reply("No results found!");
                    return;
                } catch (IOException e){
                    event.reply(e.getLocalizedMessage());
                    return;
                }
                builder.build().display(event.getTextChannel());
    }

}
