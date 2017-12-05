/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.jagrosh.jdautilities.menu.Slideshow;
import com.jagrosh.jdautilities.waiter.EventWaiter;
import com.kuborros.FurBotNeo.net.apis.Dan2Api;
import com.kuborros.FurBotNeo.utils.msg.EmbedSender;
import net.dv8tion.jda.core.Permission;
import org.json.JSONException;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.kuborros.FurBotNeo.BotMain.db;

/**
 *
 * @author Kuborros
 */
public class Dan2Cmd extends Command {

    private Permission[] perms = {Permission.MESSAGE_EMBED_LINKS};
    private EventWaiter waiter;

    public Dan2Cmd(EventWaiter waiter) {
        this.name = "dan2";
        this.help = "Searches for _pictures_ on DanBooru";
        this.arguments = "<2 Tags>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.cooldown = 5;
        this.botPermissions = perms;
        this.category = new Category("ImageBoards");
        this.hidden = true;
        this.waiter = waiter;
        db.registerCommand(this.name);
    }

    @Override
    protected void execute(CommandEvent event) {
        Slideshow.Builder builder = new Slideshow.Builder();
        Dan2Api api;
        List<String> result;
        String[] arr;
        EmbedSender emb = new EmbedSender(event);
        db.updateCommandStats(event.getAuthor().getId(), this.name);

        if (!event.getTextChannel().isNSFW()) {
            event.replyWarning("This command works only on NSFW channels! (For obvious reasons)");
            return;
        }

        List<String> tags = Arrays.asList(event.getArgs().split(" "));
        if (tags.size() > 2) {
            tags = tags.subList(0, 2);
        }

        builder.allowTextInput(false);
        builder.setBulkSkipNumber(5);
        builder.waitOnSinglePage(false);
        builder.setColor(Color.PINK);
        builder.setEventWaiter(waiter);
        builder.setText("");
        builder.setDescription("Danbooru");
        


        if (!event.getArgs().isEmpty()) {
            api = new Dan2Api("https://danbooru.donmai.us/posts.json?tags=" + String.join("+", tags) + "&random=true");
            event.reply("Processing...");
            try {
                result = api.getDanPic();
                builder.setUrls(arr = result.toArray(new String[result.size()]));
            } catch (JSONException e) {
                event.reply("No results found!");
                return;
            } catch (IOException e) {
                event.replyError(e.getLocalizedMessage());
            }
            Slideshow show = builder.build();
            show.display(event.getTextChannel());
        } else {
            api = new Dan2Api("https://danbooru.donmai.us/posts.json?random=true");
            try {
                result = api.getDanPic();
                builder.setUrls(arr = result.toArray(new String[result.size()]));
            } catch (JSONException e) {
                event.reply("No results found!");
                return;
            } catch (IOException e) {
                event.replyError(e.getLocalizedMessage());
            }
            Slideshow show = builder.build();
            show.display(event.getTextChannel());
        }
    }
}