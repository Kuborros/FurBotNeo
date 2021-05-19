/*
 * Copyright © 2020 Kuborros (kuborros@users.noreply.github.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kuborros.FurBotNeo.commands.ShopCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.kuborros.FurBotNeo.utils.menus.SelectionTitleDialog;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;

@CommandInfo(
        name = "role",
        description = "Sets your active role."
)
@Author("Kuborros")
public class SetRoleCommand extends ShopCommand {

    final EventWaiter waiter;
    ArrayList<String> roles;
    HashMap<String, String> roleInfo;
    Member sender;

    public SetRoleCommand(EventWaiter waiter) {
        this.name = "role";
        this.help = "Select your role color!";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.MANAGE_ROLES};
        this.category = new Command.Category("Shop");
        this.waiter = waiter;
    }

    @Override
    protected void doCommand(CommandEvent event) {

        SelectionTitleDialog.Builder builder = new SelectionTitleDialog.Builder();
        roles = inventory.getOwnedRoles();

        if (roles.isEmpty()) {
            EmbedBuilder eBuilder = new EmbedBuilder().setTitle("**Pick your new role:**")
                    .setColor(Color.ORANGE)
                    .setDescription("Actually, you can't since you don't own any!");
            event.reply(eBuilder.build());
            return;
        }

        roleInfo = getRoles();
        sender = event.getMember();

        builder.setUsers(event.getAuthor())
                .setColor(Color.ORANGE)
                .setDefaultEnds("", "")
                .setSelectedEnds("**", "**")
                .setEventWaiter(waiter)
                .setChoices(roleInfo.keySet().toArray(new String[0]))
                .setTitle(String.format("**Pick your new role, %s:**", event.getMember().getEffectiveName()))
                .setSelectionConsumer(this::setSelectedRole)
                .setCanceled(message -> message.clearReactions().queue())
                .useLooping(true)
                .setTimeout(5, TimeUnit.MINUTES);

        builder.build().display(event.getTextChannel());

    }

    private void setSelectedRole(Message message, int selection) {
        String selectedRole = roleInfo.keySet().toArray(new String[0])[selection - 1];
        Guild guild = sender.getGuild();
        roleInfo.remove("Bot DJ");
        //Take previous roles away, if member has one.
        for (String role : roleInfo.keySet()) {
            try {
                //As a positive (i hope) side effect will always regenerate all roles that exist in users inventory but not in guild.
                guild.removeRoleFromMember(sender, findGuildRole(guild, role)).complete();
            } catch (Exception e) {
                LOG.error("Ohno.", e);
            }
        }
        //Give member their new role.
        try {
            guild.addRoleToMember(sender, findGuildRole(guild, selectedRole)).complete();
            inventoryCache.setInventory(inventory.setCurrentRole(roles.get(selection-1)));
            EmbedBuilder builder = new EmbedBuilder()
                    .setDescription("Enjoy your new color~");
            message.editMessage(builder.build()).complete();
            message.clearReactions().complete();
        } catch (Exception e) {
            LOG.error("Ohno.", e);
        }

    }

    private HashMap<String, String> getRoles() {
        HashMap<String, String> roleInfo = new HashMap<>();

        String json = "";

        try {
            json = Files.readString(Path.of("items.json"));
        } catch (Exception e) {
            LOG.error("Things went wrong while loading internal resource: ", e);
        }

        JSONObject iNames = new JSONObject(json).getJSONObject("roles");

        for (String item : roles) {
            try {
                roleInfo.put(iNames.getJSONObject(item).getString("name"), iNames.getJSONObject(item).getString("role_color"));
            } catch (JSONException e) {
                LOG.debug("Item not in items.json? {}", item);
                //Hide our mess-up with "default" role
                //Should never happen, but that prevents possible later issues.
                roleInfo.put("Default Role", "#808080");
            }
        }
        return roleInfo;
    }

    private Role findGuildRole(Guild guild, String name) {
        for (Role role : guild.getRoles()) {
            if (role.getName().equals(name)) return role;
        }
        //If role is not found, try to create it.
        return createNewRole(guild, name);
    }

    private Role createNewRole(Guild guild, String name) {
        return guild.createRole().setName(name).setColor(Color.decode(roleInfo.get(name))).complete();
    }

}
