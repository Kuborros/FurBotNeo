
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

package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Properties;

@CommandInfo(
        name = "Stats",
        description = "Prints several statistics about the bot."
)
@Author("Kuborros")
public class StatsCommand extends AdminCommand {

    private final OffsetDateTime start = OffsetDateTime.now();
    private static final Properties versionInfo = new Properties();

    public StatsCommand() {
        this.name = "stats";
        this.help = "Shows some statistics on the bot";
        this.ownerCommand = true;
        this.guildOnly = false;
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
        this.hidden = true;
    }

    @Override
    protected void doCommand(CommandEvent event) {

        String version = "0";
        try {
            versionInfo.load(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("version.info")));
            version = versionInfo.getProperty("version");
        } catch (IOException e) {
            LOG.warn("IO error occurred while reading version.info", e);
        }

        boolean sharded = event.getJDA().getShardManager() != null;

        long totalMb = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long usedMb = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);

        StringBuilder builder = new StringBuilder();

        builder.append("**").append(event.getSelfUser().getName()).append("** statistics:")
                .append("\nRelease:  ").append(version)
                .append("\nLast Startup: ").append(start.format(DateTimeFormatter.RFC_1123_DATE_TIME))
                .append("\nMemory: ").append(usedMb).append("Mb / ").append(totalMb).append("Mb")
                .append("\nResponse Total: ").append(event.getJDA().getResponseTotal());

        if (sharded) {
            ShardManager shardman = event.getJDA().getShardManager();
            builder.append("\nGuilds: ").append(shardman.getGuildCache().size())
                    .append("\nAverage ping: ").append(shardman.getAverageGatewayPing())
                    .append("\nTotal shards: ").append(shardman.getShardsRunning());
        } else {
            builder.append("\nGuilds: ").append(event.getJDA().getGuildCache().size())
                    .append("\nCurrent ping: ").append(event.getJDA().getGatewayPing());
        }

        event.reply(builder.toString());
    }

}
