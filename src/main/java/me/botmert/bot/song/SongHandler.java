package me.botmert.bot.song;

import me.botmert.bot.DiscordBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class SongHandler {

    Set<Song> missingSongs = new HashSet<>();
    
    public void findNewSongs() throws IOException {
        URL sheet = new URL("https://sheets.googleapis.com/v4/spreadsheets/1vW-nFbnR02F9BEnNPe5NBejHRGPt0QEGOYXLSePsC1k/values/Unreleased?key=" + DiscordBot.getInstance().getConfig().getString("google.api-key"));
        
        urlToFile(sheet, "sheetnew.json");
        try {
            JSONParser parser = new JSONParser();

            JSONObject sheetnew = (JSONObject) parser.parse(new FileReader("sheetnew.json"));
            JSONObject sheetold = (JSONObject) parser.parse(new FileReader("sheetold.json"));
    
            JSONArray oldValues = (JSONArray) sheetold.get("values");
            JSONArray newValues = (JSONArray) sheetnew.get("values");
    
            boolean changes = false;
    
            for (Object newObject : newValues) {
                JSONArray newSong = (JSONArray) newObject;
                if (newSong.size() == 9) {
                    if (!oldValues.contains(newSong)) {
                        String era = (String) newSong.get(0);
                        String name = (String) newSong.get(1);
                        String description = (String) newSong.get(2);
                        String type = (String) newSong.get(6);
    
                        if (type.equals("Rumored") || type.equals("Confirmed")) return;
    
                        changes = true;
    
                        missingSongs.add(new Song(era, name, description, type));
                    }
                }
            }
    
            urlToFile(sheet, "sheetold.json");
            
            if (changes) {
                Guild guild = DiscordBot.getInstance().getClient().getGuildById(DiscordBot.getInstance().getConfig().getString("bot.guild-id"));
                if (guild != null) {
                    TextChannel textChannel = guild.getTextChannelById(DiscordBot.getInstance().getConfig().getString("bot.new-shit-id"));
                    if (textChannel != null) {
                        textChannel.sendMessage("**NEW - " + missingSongs.size() + " new song" + (missingSongs.size() == 0 ? "" : "s") + " have been added to the tracker.** <@&1006043394851754024>").queue();
    
                        Set<MessageEmbed> embedList = new HashSet<>();
    
                        for (Song song : missingSongs) {
                            String[] songInfo = song.getName().split("\\n");
                            String name = songInfo[0];
        
                            embedList.add(new EmbedBuilder().setColor(Color.RED).setAuthor(song.getType()).setDescription("Name: " + name + "\nEra: " + song.getEra() + "\nDescription: " + song.getDescription()).build());
        
                        }
                        textChannel.sendMessageEmbeds(embedList).queue();
                        return;
                    }
                    System.out.println("TEXT CHANNEL IS NULL");
                    return;
                }
                System.out.println("GUILD IS NULL");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    
    public void urlToFile(URL url, String fileName) throws IOException {
        try (InputStream in = url.openStream())
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(reader.lines().collect(Collectors.joining(System.lineSeparator())));
    
            writer.close();
        }
    }
    
    public SongHandler() throws IOException {


        findNewSongs();

    }
    
}
