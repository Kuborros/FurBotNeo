package com.kuborros.FurBotNeo.commands.ShopCommands;


import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.kuborros.FurBotNeo.utils.menus.SelectionTitleDialog;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@CommandInfo(
        name = "UseItem",
        description = "Use one of your owned items."
)
@Author("Kuborros")
public class UseItemCommand extends ShopCommand {

    final EventWaiter waiter;
    ArrayList<String> items;
    HashMap<String, String> itemInfo;
    Member sender;

    public UseItemCommand(EventWaiter waiter) {
        this.name = "item"; //?????
        this.help = "Use one of your owned items.";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.MANAGE_ROLES};
        this.category = new Command.Category("Shop");
        this.waiter = waiter;
    }

    @Override
    protected void doCommand(CommandEvent event) {

        SelectionTitleDialog.Builder builder = new SelectionTitleDialog.Builder();
        items = inventory.getOwnedRoles();

        if (items.isEmpty()) {
            EmbedBuilder eBuilder = new EmbedBuilder().setTitle("**Pick an item you want to use:**")
                    .setColor(Color.ORANGE)
                    .setDescription("Actually, you can't since you don't own any!");
            event.reply(eBuilder.build());
            return;
        }

        itemInfo = getRoles();
        sender = event.getMember();

        builder.setUsers(event.getAuthor())
                .setColor(Color.ORANGE)
                .setDefaultEnds("", "")
                .setSelectedEnds("**", "**")
                .setEventWaiter(waiter)
                .setChoices(itemInfo.keySet().toArray(new String[0]))
                .setTitle(String.format("**Pick an item you want to use, %s:**", event.getMember().getEffectiveName()))
                .setSelectionConsumer(this::useItem)
                .setCanceled(message -> message.clearReactions().queue())
                .useLooping(true)
                .setTimeout(5, TimeUnit.MINUTES);

        builder.build().display(event.getTextChannel());

    }

    @SuppressWarnings("EmptyMethod")
    private void useItem(Message message, int selection) {
        //TODO: Decide what items are actually supposed to do!
    }

    private HashMap<String, String> getRoles() {
        HashMap<String, String> itemInfo = new HashMap<>();

        String json = "";

        try {
            json = new String(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("items.json")).readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error("Things went wrong while loading internal resource: ", e);
        }

        JSONObject iNames = new JSONObject(json).getJSONObject("items");

        for (String item : items) {
            try {
                itemInfo.put(iNames.getJSONObject(item).getString("name"), iNames.getJSONObject(item).getString("pic_url"));
            } catch (JSONException e) {
                LOG.debug("Item not in items.json? {}", item);
                //Hide our mess-up with "default" item
                //Should never happen, but that prevents possible later issues.
                itemInfo.put("Default item", "");
            }
        }
        return itemInfo;
    }
}