
package com.kuborros.FurBotNeo.commands.GeneralCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.entities.Message;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

@CommandInfo(
        name = "Joke",
        description = "Pulls a Chuck Norris joke and he permits himself to be replaced with mentioned user"
)
@Author("Kuborros")
public class BadJokeCmd extends GeneralCommand {

    private final OkHttpClient client = new OkHttpClient();
    
    public BadJokeCmd()
    {
        this.name = "joke";
        this.help = "Makes a joke about mentioned user or anything you type in!";
        this.arguments = "@user | <anything>";
        this.guildOnly = true;        
        this.category = new Command.Category("Basic"); 
}
    @Override
    public void doCommand(CommandEvent event) {
            Message message = event.getMessage();

        Request request = new Request.Builder()
                .url("http://api.icndb.com/jokes/random")
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
