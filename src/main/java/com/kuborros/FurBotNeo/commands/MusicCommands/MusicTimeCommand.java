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

package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.kuborros.FurBotNeo.utils.audio.RequesterInfo;
import net.dv8tion.jda.api.Permission;

@CommandInfo(
        name = "MusicSeek",
        description = "Skips to specified time in the track."
)
@Author("Kuborros")
public class MusicTimeCommand extends MusicCommand {

    public MusicTimeCommand() {
        this.name = "seek";
        this.arguments = "<time>";
        this.help = "Skips to specified time in song";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");
    }

    @Override
    public void doCommand(CommandEvent event) {

        //Person who requested the track can seek it to right time
        boolean isRequester = (event.getMember().getId().equals(getPlayer(guild).getPlayingTrack().getUserData(RequesterInfo.class).getId()));

        if (isDJ || isRequester) {
            if (isIdle(guild)) {
                event.reply(sendFailEmbed("No music is being played at the moment!", ""));
                return;
            }

            String val = event.getArgs().toUpperCase().trim();
            boolean min = false;
            if (val.endsWith("M")) {
                min = true;
                val = timeTrim(val);
            } else if (val.endsWith("S")) {
                val = timeTrim(val);
            } else {
                val = val.trim();
            }
            int seconds;
            try {
                seconds = (min ? 60 : 1) * Integer.parseInt(val);
                long milis = (seconds * 1000);
                long duration = getPlayer(guild).getPlayingTrack().getDuration();
                if (duration <= milis) {
                    event.reply(sendFailEmbed("This track is not long enough to skip that far!", "Track lenght is " + getTimestamp(duration)));
                    return;
                }
                getTrackManager(guild).skipToTime(milis);
                event.reply(sendGenericEmbed("Skipping to: " + seconds + "s!", "", ":fast_forward:"));
            } catch (NumberFormatException ex) {
                event.reply(sendFailEmbed("Heeey... That's not a valid number!", "Don't be silly like that."));
            }
        } else {
            event.reply(sendFailEmbed("Only DJs can change playback timestamp!", "With that power comes responsibility!"));
        }
    }

    private String timeTrim(String val) {
        return val.substring(0, val.length() - 1).trim();
    }
}
