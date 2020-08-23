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

import static com.kuborros.FurBotNeo.BotMain.randomResponse;

@CommandInfo(
        name = "Shutdown",
        description = "Performs graceful shutdown of the bot."
)
@Author("Kuborros")
public class ShutdownCommand extends AdminCommand {

    public ShutdownCommand() {
        this.name = "shutdown";
        this.help = "Safely shuts off the bot";
        this.guildOnly = false;
        this.ownerCommand = true;
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
        this.hidden = true;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        event.reply(randomResponse.getRandomShutdownMessage());

        if (event.getJDA().getShardManager() != null) {
            event.getJDA().getShardManager().shutdown();
        } else event.getJDA().shutdown();

        LOG.info("Bot shutting down");
        System.exit(0);
    }

}