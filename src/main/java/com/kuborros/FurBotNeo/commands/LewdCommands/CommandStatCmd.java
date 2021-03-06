
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

package com.kuborros.FurBotNeo.commands.LewdCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.kuborros.FurBotNeo.utils.config.FurConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.sql.SQLException;
import java.util.Map;

import static com.kuborros.FurBotNeo.BotMain.db;

@CommandInfo(
        name = "PicStats",
        description = "Prints list of times player used picture commands."
)
@Author("Kuborros")
public class CommandStatCmd extends LewdCommand {

    public CommandStatCmd() {
        this.name = "picstat";
        this.help = "Tells you how perverted someone is!";
        this.arguments = "@user";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Command.Category("Lewd");
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
    }

    @Override
    protected void doCommand(CommandEvent event) {
        StringBuilder builder = new StringBuilder();

        FurConfig config = (FurConfig) event.getClient().getSettingsManager().getSettings(guild);
        assert config != null;
        if (!config.isNSFW()) {
            LOG.info("NSFW command ran on SFW server, ignoring");
            return;
        }

        User user = event.getMessage().getMentionedUsers().isEmpty() ? event.getAuthor() : event.getMessage().getMentionedUsers().get(0);
        try {
            Map<String, String> map = db.getCommandStats(user.getId());
            builder.append("\n");
            map.forEach((k, v) -> builder.append("``").append(k).append("`` used **").append(v).append("** times\n\n"));
            event.getChannel().sendMessage(
                    new MessageBuilder().setEmbed(
                            new EmbedBuilder().setTitle(String.format("How many times %s nutted to:", user.getName()), null).setDescription(builder.toString()).setColor(Color.yellow).build()
                    ).build()).queue();
        } catch (SQLException e) {
            LOG.error("Error occurred while retrieving command stats: ", e);
            event.reply(errorResponseEmbed("Unable to load commands stats! That's **not** good.", e));
        }

    }

}
