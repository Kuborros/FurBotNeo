
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

package com.kuborros.FurBotNeo.commands.GeneralCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.kuborros.FurBotNeo.utils.config.FurConfig;
import net.dv8tion.jda.api.entities.Message;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

@CommandInfo(
        name = "Joke",
        description = "Creates a joke about mentioned user."
)
@Author("Kuborros")
public class BadJokeCmd extends GeneralCommand {

    private final OkHttpClient client = new OkHttpClient();

    public BadJokeCmd() {
        this.name = "joke";
        this.help = "Makes a joke about mentioned user or anything you type in!";
        this.arguments = "@user | <anything>";
        this.guildOnly = true;
        this.category = new Command.Category("Basic");
    }

    @Override
    public void doCommand(CommandEvent event) {
        Message message = event.getMessage();

        FurConfig config = (FurConfig) event.getClient().getSettingsManager().getSettings(guild);
        assert config != null;
        boolean guildNSFW = config.isNSFW();

        Request request = new Request.Builder()
                .url("https://api.icndb.com/jokes/random")
                .header("User-Agent", "FurryBotNeo/1.0")
                .addHeader("Accept", "application/json")
                .build();


        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Received error response code: " + response);
            InputStream r = Objects.requireNonNull(response.body()).byteStream();

            StringBuilder str;
            try (Scanner scan = new Scanner(r)) {
                str = new StringBuilder();
                while (scan.hasNext()) {
                    str.append(scan.nextLine());
                }
            }
            JSONObject object = new JSONObject(str.toString());
            if (!"success".equals(object.getString("type"))) {
                LOG.error("Error while retrieving joke.");
                event.reply(errorResponseEmbed("Unable to obtain joke! ", "Api reports external failure."));
                return;
            }
            if (!guildNSFW) {
                JSONArray categories = object.getJSONObject("value").getJSONArray("categories");
                if (!categories.isEmpty()) { //Not-empty categories mean most likely "explicit" category tag. Better not post it on SFW server!
                    event.reply("*Squeak!* Indecent joke detected!");
                    return;
                }
            }

            String joke = object.getJSONObject("value").getString("joke");
            String remainder = event.getArgs();

            if (!message.getMentionedUsers().isEmpty()) {
                joke = joke.replaceAll("Chuck Norris", "<@" + message.getMentionedUsers().get(0).getId() + ">");
            } else if (!remainder.isEmpty()) {
                joke = joke.replaceAll("Chuck Norris", remainder);
            }

            joke = joke.replaceAll("&quot;", "\"");

            event.reply(joke);
        } catch (IOException | JSONException e) {
            LOG.error("Exception occurred while obtaining jokes: ", e);
            event.reply(errorResponseEmbed("Exception occurred while obtaining jokes:", e));
        }
    }  
}
