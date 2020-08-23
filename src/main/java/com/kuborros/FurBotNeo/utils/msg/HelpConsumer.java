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

package com.kuborros.FurBotNeo.utils.msg;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.kuborros.FurBotNeo.utils.config.FurConfig;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.kuborros.FurBotNeo.BotMain.cfg;

public class HelpConsumer implements Consumer<CommandEvent> {

    boolean isNSFW = true;
    boolean isShopOn = true;
    Guild guild;
    CommandClient client;

    @Override
    public void accept(CommandEvent event) {

        client = event.getClient();

        if (event.isFromType(ChannelType.TEXT)) {
            guild = event.getGuild();
            FurConfig config = (FurConfig) event.getClient().getSettingsManager().getSettings(guild);
            assert config != null;
            isNSFW = config.isNSFW();
            isShopOn = cfg.isShopEnabled();
        }

        List<Command> commands = client.getCommands();
        String prefix = event.getClient().getPrefix();

        StringBuilder builder = new StringBuilder("**" + event.getSelfUser().getName() + "** commands:\n");
        Command.Category category = null;
        for (Command command : commands) {
            if (!command.isHidden() && (!command.isOwnerCommand() || event.isOwner())) {
                String catName = "";
                if (!Objects.equals(category, command.getCategory())) {
                    category = command.getCategory();
                    catName = category == null ? "No Category" : category.getName();
                    //Do not show lewds on SFW server
                    if (!catName.contains("Lewd") || isNSFW) builder.append("\n\n  __").append(catName).append("__:\n");
                    //Point out that shop is disabled on this instance (but keep it shown as a sample of full features)
                    if (catName.contains("Shop") && !isShopOn)
                        builder.append("\n").append(client.getWarning()).append("**Shop is disabled by instance owner**\n");
                }
                //Not show lewds on SFW server
                if (!catName.contains("Lewd") || isNSFW) {
                    builder.append("\n`").append(prefix).append(prefix == null ? " " : "").append(command.getName())
                            .append(command.getArguments() == null ? "`" : " " + command.getArguments() + "`")
                            .append(" - ").append(command.getHelp());
                }
            }
        }

        User owner = event.getJDA().getUserById(cfg.getOwnerId());
        if (owner != null) {
            builder.append(" \n");
            builder.append("For additional help, contact **").append(owner.getName()).append("**#").append(owner.getDiscriminator());
        }
        event.replyInDm(builder.toString());
        if (event.isFromType(ChannelType.TEXT)) event.reactSuccess();
    }
}
