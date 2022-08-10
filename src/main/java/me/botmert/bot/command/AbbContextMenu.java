package me.botmert.bot.command;

import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class AbbContextMenu extends ListenerAdapter {
    @Override
    public void onMessageContextInteraction(MessageContextInteractionEvent event) {
        if (event.getName().equals("Undo abbreviation")) {
            try {
                JSONParser parser = new JSONParser();
                JSONArray abbjson = (JSONArray) parser.parse(new FileReader("abb.json"));
                
                String words = event.getTarget().getContentRaw();
                StringBuilder stringBuilder = new StringBuilder();
                int i = 0;
                
                for (Object abb : abbjson) {
                    JSONObject name = (JSONObject) abb;
                    String word = (String) name.get("word");
                    String abbreviation = (String) name.get("abbreviation");
                    

                    if (words.toLowerCase().contains(" " + word) || words.toLowerCase().equalsIgnoreCase(word) || words.split(" ")[0].equalsIgnoreCase(word)) {
                        words = words.toLowerCase().replace(word, abbreviation);
                        i++;
                        //stringBuilder.append("Word: ").append(word).append("Abbreviation: ").append(abbreviation).append("\n");
                    }
                }
                
                event.reply(event.getTarget().getAuthor().getName() + " said: " + words.toLowerCase()).queue();
            } catch (ParseException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
