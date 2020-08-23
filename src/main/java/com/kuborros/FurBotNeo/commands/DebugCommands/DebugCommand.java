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

package com.kuborros.FurBotNeo.commands.DebugCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.kuborros.FurBotNeo.utils.store.MemberInventory;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.kuborros.FurBotNeo.BotMain.cfg;
import static com.kuborros.FurBotNeo.BotMain.inventoryCache;

abstract class DebugCommand extends Command {

    static final Logger LOG = LoggerFactory.getLogger("DebugCommands");

    protected Guild guild;
    protected MemberInventory inventory;

    //Purely testing commands, as such most of the usual fancy stuff gets skipped here.
    @Override
    protected void execute(CommandEvent event) {
        if (event.getAuthor().isBot() || !cfg.isDebugMode()) return;
        guild = event.getGuild();
        inventory = inventoryCache.getInventory(event.getMember().getId(), guild.getId());
        doCommand(event);
    }

    protected abstract void doCommand(CommandEvent event);
}

