package me.botmert.bot.sheet;

import me.botmert.bot.DiscordBot;

import me.botmert.bot.song.Song;
import me.botmert.bot.song.SongType;
import me.botmert.bot.util.ImageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.List;
import java.util.stream.Collectors;

public class SheetHandler {
    
    private ArrayList<Song> lastSongs = new ArrayList<>();
    
    public Song getOldSong(String text) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        
        JSONArray sheetold = (JSONArray) parser.parse(new FileReader("sheetold.json"));
    
        for (Object newSongs : sheetold) {
            JSONObject newSong = (JSONObject) newSongs;
        
            if (newSong.size() >= 8) {

                String era = (String) newSong.get("Era");
                String name = (String) newSong.get("Name\n(Check out the Tracker website!)");
                name = name.replace("\n", " ");
                String notes = (String) newSong.get("Notes\n(Join the Discord for updates and suggestions)");
                String trackLength = (String) newSong.get("Track Length");
                String leakDate = (String) newSong.get("Leak\nDate");
                String type = (String) newSong.get("Type");
                String available = (String) newSong.get("Available");
                String quality = (String) newSong.get("Quality");
                String links = null;
                
                if (newSong.size() == 9) {
                    links = (String) newSong.get("Link(s)");
                }
                
                if (text.equals(name)) {
                    return new Song(era, name, notes, trackLength, leakDate, type, available, quality, links);
                } else if (text.equals(notes)) {
                    return new Song(era, name, notes, trackLength, leakDate, type, available, quality, links);
                }

            }
        }
        return null;
    }
    
    public void findNewEdit() throws IOException {
        URL sheet = new URL("http://localhost:4000/1vW-nFbnR02F9BEnNPe5NBejHRGPt0QEGOYXLSePsC1k/0");
        
        urlToFile(sheet, "sheetnew.json");
    
        Guild guild = DiscordBot.getInstance().getClient().getGuildById(DiscordBot.getInstance().getConfig().getString("bot.guild-id"));
        TextChannel newShitChannel;
        TextChannel logsChannel;
        
        if (guild != null) {
            newShitChannel = guild.getTextChannelById(DiscordBot.getInstance().getConfig().getString("bot.new-shit-id"));
            logsChannel = guild.getTextChannelById(DiscordBot.getInstance().getConfig().getString("bot.logs-id"));
        } else {
            System.out.println("GUILD NULL LOOOOL");
            newShitChannel = null;
            logsChannel = null;
        }
        
        try {
            if (logsChannel != null) {
    
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();

                logsChannel.sendMessageEmbeds(new EmbedBuilder().setColor(Color.GREEN).setDescription("Running edit finder. | " + formatter.format(date) + " EST").build()).queue();
            } else {
                System.out.println("TEXT CHANNEL NULL LOOOOL");
            }
    
            ArrayList<Song> currentSongs = new ArrayList<>();
            
            JSONParser parser = new JSONParser();

            JSONArray sheetnew = (JSONArray) parser.parse(new FileReader("sheetnew.json"));
            JSONArray sheetold = (JSONArray) parser.parse(new FileReader("sheetold.json"));
            
            boolean songChange = false;
            boolean eraChange = false;
    
            System.out.println("Running song find");
            
            for (Object newSongs : sheetnew) {
                JSONObject newSong = (JSONObject) newSongs;
                
                if (newSong.size() >= 8) {
                    String era = (String) newSong.get("Era");
                    String name = (String) newSong.get("Name\n(Check out the Tracker website!)");
                    name = name.replace("\n", " ");
                    String notes = (String) newSong.get("Notes\n(Join the Discord for updates and suggestions)");
                    String trackLength = (String) newSong.get("Track Length");
                    String leakDate = (String) newSong.get("Leak\nDate");
                    String type = (String) newSong.get("Type");
                    String available = (String) newSong.get("Available");
                    String quality = (String) newSong.get("Quality");
                    String links = null;
                    
                    if (newSong.size() == 9) {
                        links = (String) newSong.get("Link(s)");
                    }
                    
                    currentSongs.add(new Song(era, name, notes, trackLength, leakDate, type, available, quality, links));
                }
            }
            /*
            if (eraChange) {
                //textChannel.sendMessage("**NEW - " + missingSongs.size() + " new song" + (missingSongs.size() == 0 ? "" : "s") + " have been added to the tracker.** <@&1006043394851754024>").queue();
                for (Era era : eras) {
                    StringBuilder message = new StringBuilder();
                    EmbedBuilder embed = new EmbedBuilder();
    
                    if (era.getEditType() == EraEditType.NEW) {
                        message.append("\n**Leaks: **").append(era.getLeaks().replace("\n", ", "));
                        message.append("\n**Name: **").append(era.getName());
                        message.append("\n**Date: **").append(era.getDate());
                        message.append("\n**Description: **").append(era.getDescription());
    
                        embed.setColor(Color.getHSBColor(358, 100, 49))
                                .setDescription(message)
                                .setThumbnail(ImageUtil.getImage(era.getName()))
                                .setAuthor("NEW NEW NEW NEW NEW NEW");
                    } else if (era.getEditType() == EraEditType.DESCRIPTION) {
                        message.append("The era \"**").append(era.getName()).append("**\" had a description change.");
                        message.append("\n\n** New Description: **").append(era.getDescription());
                        message.append("\n\n** Old Description: **").append(getOldSong(era.getName(), 2));
                        
                        embed.setColor(Color.getHSBColor(358, 100, 49))
                                .setDescription(message)
                                .setThumbnail(ImageUtil.getImage(era.getName()))
                                .setAuthor("EDIT EDIT EDIT EDIT EDIT EDIT")
                                .build();
                    } else if (era.getEditType() == EraEditType.DATE) {
                        message.append("The era \"**").append(era.getName()).append("**\" had a date change.");
                        message.append("\n\n** New Date: **").append(era.getDate());
                        message.append("\n\n** Old Date: **").append(getOldSong(era.getName(),  4));
                
                        embed.setColor(Color.getHSBColor(358, 100, 49))
                                .setDescription(message)
                                .setThumbnail(ImageUtil.getImage(era.getName()))
                                .setAuthor("EDIT EDIT EDIT EDIT EDIT EDIT")
                                .build();
                    } else {
                        System.out.println("error");
                    }
            
                    if (textChannel != null) {
                        textChannel.sendMessageEmbeds(embed.build()).queue();
                    } else {
                        System.out.println("TEXT CHANNEL NULL LOOOOL");
                    }
                }
            }
            
             */
            
            for (Song song : currentSongs) {
                
                StringBuilder message = new StringBuilder();
    
                EmbedBuilder embed = new EmbedBuilder();
                
                //System.out.println(song.getName());
                
                if (song.getName().startsWith("???") || song.getName().startsWith("Unknown") || song.getName().contains("???") || song.getName().contains("Unknown") ||  song.getName().contains("Collaborations]"))
                    continue;
                
    
                if (getOldSong(song.getName()) == null) {
                    if (getOldSong(song.getNotes()) != null) {
                        message.append("The song \"**").append(song.getName()).append("\"** (").append(song.getAvailable()).append(") had a name change.");
                        message.append("\n\n** New Name: **").append(song.getName());
                        message.append("\n\n** Old Name: **").append(getOldSong(song.getNotes()).getName());
    
                        embed.setColor(Color.getHSBColor(358, 100, 49))
                                .setDescription(message)
                                .setThumbnail(ImageUtil.getImage(song.getEra()))
                                .setAuthor("EDIT EDIT EDIT EDIT EDIT EDIT")
                                .build();
    
                        if (newShitChannel != null) {
                            if (!(embed.isEmpty())) {
                                newShitChannel.sendMessageEmbeds(embed.build()).queue();
                            }
                        } else {
                            System.out.println("TEXT CHANNEL NULL LOOOOL");
                        }
                        
                        continue;
                    }
                    //if (lastSongs.isEmpty()) return;
                    message.append("\n\n**Available: **").append(song.getAvailable());
                    message.append("\n\n**Song: **").append(song.getName());
                    message.append("\n\n**Era: **").append(song.getEra());
                    message.append("\n\n**Description: **").append(song.getNotes());
    
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
    
                    if (newShitChannel != null) {
                        if (!(embed.isEmpty())) {
                            newShitChannel.sendMessageEmbeds(embed.build()).queue();
                        }
                    } else {
                        System.out.println("TEXT CHANNEL NULL LOOOOL");
                    }
                    continue;
                }
                
                Song oldSong = getOldSong(song.getName());
                
                if (!(oldSong.getName().equals(song.getName()))) {
                    message.append("The song \"**").append(song.getName()).append("**\" (").append(song.getAvailable()).append(") had a name change.");
                    message.append("\n\n** New Name: **").append(song.getEra());
                    message.append("\n\n** Old Name: **").append(oldSong.getEra());
        
                    embed.setColor(Color.getHSBColor(358, 100, 49))
                            .setDescription(message)
                            .setThumbnail(ImageUtil.getImage(song.getEra()))
                            .setAuthor("EDIT EDIT EDIT EDIT EDIT EDIT")
                            .build();
                } else if (!(oldSong.getEra().equals(song.getEra()))) {
                    message.append("The song \"**").append(song.getName()).append("**\" (").append(song.getAvailable()).append(") had an era change.");
                    message.append("\n\n** New Era: **").append(song.getEra());
                    message.append("\n\n** Old Era: **").append(oldSong.getEra());
    
                    embed.setColor(Color.getHSBColor(358, 100, 49))
                            .setDescription(message)
                            .setThumbnail(ImageUtil.getImage(song.getEra()))
                            .setAuthor("EDIT EDIT EDIT EDIT EDIT EDIT")
                            .build();
                } else if (!(oldSong.getNotes().equals(song.getNotes()))) {
                    message.append("The song \"**").append(song.getName()).append("**\" (").append(song.getAvailable()).append(") had a notes change.");
                    message.append("\n\n** New Notes: **").append(song.getNotes());
    
                    if (!oldSong.getNotes().equals("")) {
                        message.append("\n\n** Old Notes: **").append(oldSong.getNotes());
                    }
                    
                    embed.setColor(Color.getHSBColor(358, 100, 49))
                            .setDescription(message)
                            .setThumbnail(ImageUtil.getImage(song.getEra()))
                            .setAuthor("EDIT EDIT EDIT EDIT EDIT EDIT")
                            .build();
                } else if (!(oldSong.getTrackLength().equals(song.getTrackLength()))) {
                    message.append("The song \"**").append(song.getName()).append("**\" (").append(song.getAvailable()).append(") had a track length change.");
                    message.append("\n\n** New Track Length: **").append(song.getTrackLength());
                    
                    if (!oldSong.getTrackLength().equals("")) {
                        message.append("\n\n** Old Track Length: **").append(oldSong.getTrackLength());
                    }
                    
                    embed.setColor(Color.getHSBColor(358, 100, 49))
                            .setDescription(message)
                            .setThumbnail(ImageUtil.getImage(song.getEra()))
                            .setAuthor("EDIT EDIT EDIT EDIT EDIT EDIT")
                            .build();
                } else if (!(oldSong.getLeakDate().equals(song.getLeakDate()))) {
                    message.append("The song \"**").append(song.getName()).append("**\" (").append(song.getAvailable()).append(") had a leak date change.");
                    message.append("\n\n** New Leak Date: **").append(song.getLeakDate());
                    if (!oldSong.getLeakDate().equals("")) {
                        message.append("\n\n** Old Leak Date: **").append(oldSong.getLeakDate());
                    }
        
                    embed.setColor(Color.getHSBColor(358, 100, 49))
                            .setDescription(message)
                            .setThumbnail(ImageUtil.getImage(song.getEra()))
                            .setAuthor("EDIT EDIT EDIT EDIT EDIT EDIT")
                            .build();
                } else if (!(oldSong.getType().equals(song.getType()))) {
                    message.append("The song \"**").append(song.getName()).append("**\" (").append(song.getAvailable()).append(") had a type change.");
                    message.append("\n\n** New Type: **").append(song.getType());
                    message.append("\n\n** Old Type: **").append(oldSong.getType());
        
                    embed.setColor(Color.getHSBColor(358, 100, 49))
                            .setDescription(message)
                            .setThumbnail(ImageUtil.getImage(song.getEra()))
                            .setAuthor("EDIT EDIT EDIT EDIT EDIT EDIT")
                            .build();
                } else if (!(oldSong.getAvailable().equals(song.getAvailable()))) {
                    message.append("The song \"**").append(song.getName()).append("**\" had an availability change.");
                    message.append("\n\n** New Availability : **").append(song.getAvailable());
                    message.append("\n\n** Old Availability: **").append(oldSong.getAvailable());
        
                    embed.setColor(Color.getHSBColor(358, 100, 49))
                            .setDescription(message)
                            .setThumbnail(ImageUtil.getImage(song.getEra()))
                            .setAuthor("EDIT EDIT EDIT EDIT EDIT EDIT")
                            .build();
                } else if (!(oldSong.getQuality().equals(song.getQuality()))) {
                    message.append("The song \"**").append(song.getName()).append("**\" (").append(song.getAvailable()).append(") had a quality change.");
                    message.append("\n\n** New Quality: **").append(song.getQuality());
                    message.append("\n\n** Old Quality: **").append(oldSong.getQuality());
        
                    embed.setColor(Color.getHSBColor(358, 100, 49))
                            .setDescription(message)
                            .setThumbnail(ImageUtil.getImage(song.getEra()))
                            .setAuthor("EDIT EDIT EDIT EDIT EDIT EDIT")
                            .build();
                }
                
                if (newShitChannel != null) {
                    if (!(embed.isEmpty())) {
                        newShitChannel.sendMessageEmbeds(embed.build()).queue();
                    }
                } else {
                    System.out.println("TEXT CHANNEL NULL LOOOOL");
                }
            }

            lastSongs = currentSongs;
            textToFile("sheetnew.json", "sheetold.json");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    
    public void stop() throws IOException {
        URL sheet = new URL("http://localhost:4000/1vW-nFbnR02F9BEnNPe5NBejHRGPt0QEGOYXLSePsC1k/0");
    
        urlToFile(sheet, "sheetold.json");
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
    
    public void textToFile(String oldFile, String newFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(oldFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
        writer.write(reader.lines().collect(Collectors.joining(System.lineSeparator())));
    
        writer.close();
    }
    
}