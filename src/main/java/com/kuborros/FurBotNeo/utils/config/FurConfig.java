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

import com.jagrosh.jdautilities.command.GuildSettingsProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FurConfig implements GuildSettingsProvider {

    private final List<String> prefixes = new ArrayList<>(5);
    private final String botName;
    private final boolean welcomeMsg;
    private final boolean isFurry;
    private final boolean isNSFW;
    private final String audioChannel;


    //Fallback configuration
    FurConfig() {
        this.botName = "FurryBot";
        this.audioChannel = "0";
        this.isFurry = true;
        this.welcomeMsg = false;
        this.isNSFW = false;
    }

    FurConfig(String botName, boolean welcomeMsg, boolean isFurry, boolean isNSFW, String prefix, String audioChannel) {
        this.botName = botName;
        this.prefixes.add(prefix);
        this.audioChannel = audioChannel;
        this.isFurry = isFurry;
        this.welcomeMsg = welcomeMsg;
        this.isNSFW = isNSFW;

    }

    @Nullable
    @Override
    public Collection<String> getPrefixes() {
        return prefixes.isEmpty() ? null : prefixes;
    }

    public String getBotName() {
        return botName;
    }

    public boolean isWelcomeMsg() {
        return welcomeMsg;
    }

    public boolean isFurry() {
        return isFurry;
    }

    public boolean isNSFW() {
        return isNSFW;
    }

    public String getAudioChannel() {
        return audioChannel;
    }
}

