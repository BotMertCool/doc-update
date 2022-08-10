package me.botmert.bot.song;

import lombok.Getter;
import me.botmert.bot.DiscordBot;

import me.botmert.bot.util.ImageUtil;
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

import java.util.List;
import java.util.stream.Collectors;

public class SongHandler {
    
    //private ArrayList<Song> oldSongs = new ArrayList<>();
    //private ArrayList<Song> missingSongs = new ArrayList<>();

    public Song getSong(ArrayList<Song> arrayList, String name) {
        for(Song song : arrayList) {
            if (song.getName().equals(name)) {
                return song;
            }
        }
        return null;
    }
    
    public String getOldDescription(JSONArray jsonArray, String name) {
        for (Object o : jsonArray) {
            JSONArray song = (JSONArray) o;
            if (song.size() >= 8) {
                String songName = (String) song.get(1);
                if (songName.split("\n")[0].equals(name))
                    return (String) song.get(2);
            }
        }
        return null;
    }
    
    public void findNewSongs() throws IOException {
        URL sheet = new URL("https://sheets.googleapis.com/v4/spreadsheets/1vW-nFbnR02F9BEnNPe5NBejHRGPt0QEGOYXLSePsC1k/values/Unreleased?key=" + DiscordBot.getInstance().getConfig().getString("google.api-key"));
        
        urlToFile(sheet, "sheetnew.json");
    
        Guild guild = DiscordBot.getInstance().getClient().getGuildById(DiscordBot.getInstance().getConfig().getString("bot.guild-id"));
        TextChannel textChannel;
        
        ArrayList<Song> songs = new ArrayList<>();
        
        if (guild != null) {
            textChannel = guild.getTextChannelById(DiscordBot.getInstance().getConfig().getString("bot.new-shit-id"));
        } else {
            System.out.println("GUILD NULL LOOOOL");
            textChannel = null;
        }
        
        try {
            JSONParser parser = new JSONParser();

            JSONObject sheetnew = (JSONObject) parser.parse(new FileReader("sheetnew.json"));
            JSONObject sheetold = (JSONObject) parser.parse(new FileReader("sheetold.json"));
    
            JSONArray oldValues = (JSONArray) sheetold.get("values");
            JSONArray newValues = (JSONArray) sheetnew.get("values");
    
            boolean changes = false;
    
            System.out.println("Running song find");
            
            for (Object newObject : newValues) {
                JSONArray newSong = (JSONArray) newObject;
                if (newSong.size() >= 8) {
                    String era = (String) newSong.get(0);
                    List<String> allNames = new ArrayList<>();
                    String namesRaw = (String) newSong.get(1);
                    String[] names = namesRaw.split("\n");
                    String songName = names[0];
                    String altNames = null;
                    String features = null;
                    
                    if (!songName.equals("")) {
                        if (!songName.startsWith("???"))
                            allNames.add(songName);
                    }
                    
                    for (String s : names) {
                        if (!s.startsWith("???")) {
                            if (s.startsWith("(prod.") || s.startsWith("(feat.") || s.startsWith("(ref.")) {
                                features = s;
                            } else if (s.startsWith("(")) {
                                allNames.addAll(Arrays.asList(s.replace("(", "").replace(")", "").split(", ")));
                                altNames = s.replace("(", "").replace(")", "");
                            }
                        }
                    }
                    
                    String description = (String) newSong.get(2);
                    String leakLength = (String) newSong.get(3);
                    String leakDate = (String) newSong.get(4);
                    String type = (String) newSong.get(5);
                    String available = (String) newSong.get(6);
                    String quality = (String) newSong.get(7);
                    String link;
    
                    if (newSong.size() == 9) {
                        link = (String) newSong.get(8);
                    } else {
                        link = null;
                    }
                    
                    //songs.add(new Song(era, songName, allNames, altNames, features, description, leakLength, leakDate, type, available, quality, link, false));
                    
                    if (!oldValues.contains(newSong)) {
                       changes = true;
                       boolean edit = false;
                       
                       for (Object oldObject : oldValues) {
                           JSONArray oldSong = (JSONArray) oldObject;
                           if (oldSong.size() >= 8) {
                               if (oldSong.get(1).equals(newSong.get(1))) {
                                   edit = true;
                               }
                           }
                       }
                       songs.add(new Song(era, songName, allNames, altNames, features, description, leakLength, leakDate, type, available, quality, link, edit));
                    }
                }
            }
            
            urlToFile(sheet, "sheetold.json");
            
            if (changes) {
                //textChannel.sendMessage("**NEW - " + missingSongs.size() + " new song" + (missingSongs.size() == 0 ? "" : "s") + " have been added to the tracker.** <@&1006043394851754024>").queue();
                for (Song song : songs) {
            
                    String songInfo = song.getName().replace("\\n", " ");
            
                    StringBuilder message = new StringBuilder();
            
                    EmbedBuilder embed = new EmbedBuilder();

                    if (song.getEdit()) {
                        message.append("The song \"**").append(song.getName()).append("**\" (").append(song.getAvailable()).append(") had a description change.");
                        message.append("\n\n** New Description: **").append(song.getDescription());
                        message.append("\n\n** Old Description: **").append(getOldDescription(oldValues, song.getName()));
    
    
                        embed.setColor(Color.getHSBColor(358, 100, 49))
                                .setDescription(message)
                                .setThumbnail(ImageUtil.getImage(song.getEra()))
                                .setAuthor("EDIT EDIT EDIT EDIT EDIT EDIT")
                                .build();
                    } else {
                        message.append("\n\n**Available: **").append(song.getAvailable());
                        message.append("\n\n**Song: **").append(songInfo);
                        message.append("\n\n**Era: **").append(song.getEra());
                        message.append("\n\n**Description: **").append(song.getDescription());
    
                        if (!song.getTrackLength().equals(""))
                            message.append("\n\n**Track Length: **").append(song.getTrackLength());
    
                        message.append("\n\n**Type: **").append(song.getType());
    
                        if (!song.getQuality().equals("Not Available"))
                            message.append("\n\n**Quality: **").append(song.getQuality());
    
                        if (song.getLinks() != null)
                            message.append("\n\n**Links: **\n").append(song.getLinks());
    
                        embed.setColor(Color.getHSBColor(358, 100, 49))
                                .setDescription(message)
                                .setThumbnail(ImageUtil.getImage(song.getEra()))
                                .setAuthor("NEW NEW NEW NEW NEW NEW");
                    }
            
                    if (textChannel != null) {
                        textChannel.sendMessageEmbeds(embed.build()).queue();
                    } else {
                        System.out.println("TEXT CHANNEL NULL LOOOOL");
                    }
                }
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
    
}