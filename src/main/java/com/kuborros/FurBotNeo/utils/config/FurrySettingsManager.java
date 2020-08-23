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

package com.kuborros.FurBotNeo.utils.config;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.kuborros.FurBotNeo.BotMain.db;


public class FurrySettingsManager implements GuildSettingsManager<FurConfig> {

    private static final Logger LOG = LoggerFactory.getLogger(FurrySettingsManager.class);
    private static final FurConfig defaultConfig = new FurConfig();

    private static final Map<String, FurConfig> confCache = new HashMap<>();

    private static FurConfig updateFromDb(Guild guild) {
        try {
            return db.getGuildConfig(guild);
        } catch (SQLException e) {
            LOG.error("Exception occurred while updating guild configuration, falling back to default: ", e);
            return defaultConfig;
        }
    }

    @Nonnull //Unlike GuildSettingsManager we *always* return configuration object, never null.
    @Override
    public FurConfig getSettings(Guild guild) {
        if (confCache.containsKey(guild.getId())) {
            if (db.guildNeedsUpdate(guild)) {
                FurConfig conf = updateFromDb(guild);
                confCache.remove(guild.getId());
                confCache.put(guild.getId(), conf);
                return conf;
            } else {
                return confCache.get(guild.getId());
            }

        } else {
            FurConfig conf = updateFromDb(guild);
            confCache.put(guild.getId(), conf);
            return conf;
        }

    }

    @Override
    public void init() {

    }

    @Override
    public void shutdown() {
        db.close();
    }

}
