package com.kuborros.FurBotNeo.commands.ShopCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.kuborros.FurBotNeo.utils.store.MemberInventory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

import static com.kuborros.FurBotNeo.BotMain.*;

public abstract class ShopCommand extends Command {

    static final Logger LOG = LoggerFactory.getLogger("ShopCommands");
    protected MemberInventory inventory;
    Guild guild;
    private CommandClient client;

    static final String OKAY = "\u2705";
    static final String NO = "\u274C";

    private MessageEmbed bannedResponseEmbed() {
        String random = randomResponse.getRandomDeniedMessage(guild);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("You are blocked from bot commands!")
                .setDescription(random)
                .setColor(Color.ORANGE);
        return builder.build();
    }

    MessageEmbed errorResponseEmbed(String message, Exception exception) {
        return errorResponseEmbed(message, exception.getLocalizedMessage());
    }

    MessageEmbed errorResponseEmbed(String message, String ex) {
        String random = randomResponse.getRandomErrorMessage(guild);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(client.getError() + message)
                .setDescription(random)
                .addField("Error details: ", "`` " + ex + " ``", false)
                .setColor(Color.RED);
        return builder.build();
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!cfg.isShopEnabled()) {
            LOG.info("Shop disabled by instance owner, ignoring.");
            return;
        }

        if (event.getAuthor().isBot()) return;

        guild = event.getGuild();
        client = event.getClient();
        //Shop commands themselves do not award tokens, they do however need inventory
        inventory = inventoryCache.getInventory(event.getMember().getId(), guild.getId());
        if (inventory.isBanned()) {
            event.reply(bannedResponseEmbed());
            return;
        }
        doCommand(event);
    }

    protected abstract void doCommand(CommandEvent event);
}
