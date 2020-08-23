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

import net.dv8tion.jda.api.entities.Guild;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class SisterDetect {

    private static final Properties versionInfo = new Properties();
    private static String version, buildDate, arch, os;

    public SisterDetect() {

        version = "0";
        buildDate = "0";
        arch = System.getProperty("os.arch");
        os = System.getProperty("os.type");
        try {
            versionInfo.load(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("version.info")));
            version = versionInfo.getProperty("version");
            buildDate = versionInfo.getProperty("build.date");
        } catch (IOException ignored) {
        }
    }

    public String prepareDetectionMsg(Guild guild) {


        return null;
    }


}

