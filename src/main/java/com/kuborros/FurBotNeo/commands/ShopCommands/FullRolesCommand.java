package com.kuborros.FurBotNeo.commands.ShopCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FullRolesCommand extends ShopCommand {

    final EventWaiter waiter;

    public FullRolesCommand(EventWaiter waiter) {
        this.name = "myroles";
        this.help = "List all your owned roles.";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Command.Category("Shop");
        this.waiter = waiter;
    }

    @Override
    protected void doCommand(CommandEvent event) {

        Paginator.Builder builder = new Paginator.Builder();
        List<String> inv = inventory.getOwnedRoles();
        if (inv.isEmpty()) {
            EmbedBuilder eBuilder = new EmbedBuilder().setTitle("**Contents of your role backpack:**")
                    .setColor(Color.ORANGE)
                    .setDescription("You have nothing!");
            event.reply(eBuilder.build());
            return;
        }


        builder.allowTextInput(false)
                .setUsers(event.getAuthor())
                .waitOnSinglePage(false)
                .setColor(Color.ORANGE)
                .setItemsPerPage(10)
                .useNumberedItems(true)
                .setEventWaiter(waiter)
                .setText(String.format("**Contents of %s 's role backpack:**", event.getMember().getEffectiveName()))
                .setFinalAction(message -> message.clearReactions().queue())
                .setTimeout(5, TimeUnit.MINUTES);

        builder.addItems(getPrettyRoleNames(inv));
        builder.build().display(event.getTextChannel());

    }

    private String[] getPrettyRoleNames(List<String> items) {
        ArrayList<String> names = new ArrayList<>();

        String json = "";

        if (items.isEmpty()) return new String[]{""};

        try {
            json = new String(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("items.json")).readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error("Things went wrong while loading internal resource: ", e);
        }

        JSONObject iNames = new JSONObject(json).getJSONObject("roles");

        for (String item : items) {
            if (item.isBlank()) {
                names.add("Nothing!");
                break;
            }
            try {
                names.add(iNames.getJSONObject(item).getString("name"));
            } catch (JSONException e) {
                LOG.debug("Item not in items.json? {}", item);
                names.add("Broken role!");
            }
        }
        return names.toArray(new String[0]);
    }
}
