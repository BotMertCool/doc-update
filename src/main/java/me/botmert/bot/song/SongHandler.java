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
    
    public String getOldDescription(JSONArray songs, String name) {
        for (Object newObject : songs) {
            JSONArray oldSong = (JSONArray) newObject;
            if (oldSong.size() >= 8) {
                if (oldSong.get(1).equals(name)) {
                    return (String) oldSong.get(2);
                }
            }
        }
        return null;
    }
    
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
    
            System.out.println("Running song find");
            
            for (Object newObject : newValues) {
                JSONArray newSong = (JSONArray) newObject;
                if (newSong.size() >= 8) {
                    if (!oldValues.contains(newSong)) {
                        changes = true;
                        
                        String era = (String) newSong.get(0);
                        String name = (String) newSong.get(1);
                        String description = (String) newSong.get(2);
                        String leakLength = (String) newSong.get(3);
                        String leakDate = (String) newSong.get(4);
                        String type = (String) newSong.get(5);
                        String available = (String) newSong.get(6);
                        String quality = (String) newSong.get(7);
                        
                        if (newSong.size() == 9) {
                            String link = (String) newSong.get(8);
                            missingSongs.add(new Song(era, name, description, leakLength, leakDate, type, available, quality, link, false));
                        } else {
                            missingSongs.add(new Song(era, name, description, leakLength, leakDate, type, available, quality, null, false));
                        }
                    }
                    if (oldValues.contains(newSong)) {
                        String era = (String) newSong.get(0);
                        String name = (String) newSong.get(1);
                        String description = (String) newSong.get(2);
                        String leakLength = (String) newSong.get(3);
                        String leakDate = (String) newSong.get(4);
                        String type = (String) newSong.get(5);
                        String available = (String) newSong.get(6);
                        String quality = (String) newSong.get(7);
                        
                        if (description.equals(getOldDescription(oldValues, name))) {
                            return;
                        }
                        
                        description = description + ":" + getOldDescription(oldValues, name);
                        if (newSong.size() == 9) {
                            String link = (String) newSong.get(8);
                            missingSongs.add(new Song(era, name, description, leakLength, leakDate, type, available, quality, link, true));
                        } else {
                            missingSongs.add(new Song(era, name, description, leakLength, leakDate, type, available, quality, null, true));
                        }
                        
                        
                        changes = true;

                    }
                }
            }
    
            urlToFile(sheet, "sheetold.json");

            
            
            if (changes) {
                Guild guild = DiscordBot.getInstance().getClient().getGuildById(DiscordBot.getInstance().getConfig().getString("bot.guild-id"));
                if (guild != null) {
                    TextChannel textChannel = guild.getTextChannelById(DiscordBot.getInstance().getConfig().getString("bot.new-shit-id"));
                    if (textChannel != null) {
                        //textChannel.sendMessage("**NEW - " + missingSongs.size() + " new song" + (missingSongs.size() == 0 ? "" : "s") + " have been added to the tracker.** <@&1006043394851754024>").queue();
                        for (Song song : missingSongs) {
                            
                            String songInfo = song.getName().replace("\\n", " ");
    
                            StringBuilder message = new StringBuilder();
    
                            message.append("**").append(song.getAvailable()).append("**");
                            
                            message.append("Song: ").append(songInfo);
                            message.append("\nEra: ").append(song.getEra());
                            message.append("\nDescription: ").append(song.getDescription());
                            
                            if (!song.getTrackLength().equals(""))
                                message.append("\nTrack Length: ").append(song.getTrackLength());
                            
                            message.append("\nType: ").append(song.getType());
                            
                            if (!song.getQuality().equals("Not Available"))
                                message.append("\nQuality: ").append(song.getQuality());
                            
                            if (song.getLinks() != null)
                                message.append("\n\n**Links: **\n").append(song.getLinks());

                            String thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/994117957905039360/the-fucking.png";
                            
                            if (song.getEra().equalsIgnoreCase("Before The College Dropout"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006272352813842482/Untitled.jpg";
    
                            if (song.getEra().equalsIgnoreCase("The College Dropout"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006272710147584060/Untitled_1.jpg";
    
                            if (song.getEra().equalsIgnoreCase("Late Registration"))
                                    thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006273171307102208/Untitled_2.jpg";
                            
                            if (song.getEra().equalsIgnoreCase("Graduation"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006273414278955018/Untitled_3.jpg";
    
                            if (song.getEra().equalsIgnoreCase("808s & Heartbreak"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006273570697134170/Untitled_4.jpg";
    
                            if (song.getEra().equalsIgnoreCase("Good Ass Job"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006273817951350916/pkUVIus9cneNZJ-Z_f6AFh-R0y0Sl3loapk1tRyFC7Foqv6oSPQJLElmkJWBlcn39A4QfKoGG5ZCUWQpdEkC-ifzaKgDuO6Lt-7IxmyNIxgKYRf72zp9mN2Jzq3KLKK2ZfW973Z4i8RDRNUnmb9vDcDpja05XdlYjzyC8WJlJvNWyBxDOf7_ptHns2048.png";
    
                            if (song.getEra().equalsIgnoreCase("My Beautiful Dark Twisted Fantasy"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006273936415277167/Untitled.png";
    
                            if (song.getEra().equalsIgnoreCase("Watch The Throne"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006274083295592500/Untitled_5.jpg";
    
                            if (song.getEra().equalsIgnoreCase("Cruel Summer"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006274258755928154/Untitled_1.png";
    
                            if (song.getEra().equalsIgnoreCase("Thank God For Drugs"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006274427811545139/Yeezus_Cover_3.jpg";
    
                            if (song.getEra().equalsIgnoreCase("Yeezus"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006274596904910908/Untitled_6.jpg";
    
                            if (song.getEra().equalsIgnoreCase("Cruel Winter [V1]"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006274744020123648/j0ungaljb5i41.png";
                            
                            if (song.getEra().equalsIgnoreCase("Yeezus II"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006274883346509834/Yeezus_2_Cover_5.jpg";
    
                            if (song.getEra().equalsIgnoreCase("So Help Me God"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006275091371409589/Untitled_2.png";
    
                            if (song.getEra().equalsIgnoreCase("SWISH"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006275248351613069/ezgif-frame-001_4.png";
    
                            if (song.getEra().equalsIgnoreCase("The Life Of Pablo"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006275379981459527/Untitled_7.jpg";
    
                            if (song.getEra().equalsIgnoreCase("TurboGrafx16"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006275512827658391/TG16_Cover.png";
    
                            if (song.getEra().equalsIgnoreCase("Cruel Winter [V2]"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006275512827658391/TG16_Cover.png";
    
                            if (song.getEra().equalsIgnoreCase("LOVE EVERYONE"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006275799764185088/Cover.png";
    
                            if (song.getEra().equalsIgnoreCase("ye"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006276036159340665/Untitled_8.jpg";
    
                            if (song.getEra().equalsIgnoreCase("KIDS SEE GHOSTS"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006276145349673080/Untitled_9.jpg";
    
                            if (song.getEra().equalsIgnoreCase("Good Ass Job (2018)"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006276330440097813/unknown_1.png";
                            
                            if (song.getEra().equalsIgnoreCase("Yandhi [V1]"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006276501571907805/Untitled_10.jpg";
    
                            if (song.getEra().equalsIgnoreCase("Yandhi [V2]"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006276501571907805/Untitled_10.jpg";
    
                            if (song.getEra().equalsIgnoreCase("JESUS IS KING"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006276892229378149/Untitled_12.jpg";
    
                            if (song.getEra().equalsIgnoreCase("God's Country"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006277062341951588/74ziGUWsWgZdow2lQf7_f4zXoaWUbYm7AYVfkua3xIAeBkYo4lv2LyuCm-1uYzoHXFRrFsVFwYZdHu7SEgn1UqCmz_yt7LZ9yjuzXvIQs0pLtNaMJcjWEMyJp7JlD0UV2EK9GOgtqy93o9ZYw__o6CX4LAs2048.png";
                            
                            if (song.getEra().equalsIgnoreCase("JESUS IS KING II"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006277167300227162/OeUNMqoi_Z8jTzRLMHRlEeGpj-Poz2AowHTxtAO5nk0jKj6KKT4nf9Zl-OJrh2wQPBsdnL-O0O-ZlLi6DIpoNYB9qW9O65KibZhN92LJ0xIiGrjw2X15uGsxRmfY417nDDvhQp4UTvDiI6yFn4W4-x90G3naMxrm8d54f3Y6wEaQw0IMUx5Shplfs2048.png";
                            
                            if (song.getEra().equalsIgnoreCase("DONDA [V1]"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006277357901992076/Cover.jpg";
                            
                            if (song.getEra().equalsIgnoreCase("Donda [V2]"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006277564463063070/uGpySjxT6WRXPuwXEpUJKfV9O3JBpE3hRps9wzEXdG5I7Jyp4qgB5zpp7If9Yp8ZfgFQlsN8_NUdjVRR1jnQM5c0R4MxP_mVw0kb5ycTywvC2Mgdogrt6WOpspE_w3qCnvTOr6oHvKGed9F3EUPoKszWx7SPAs2048.jpg";
    
                            if (song.getEra().equalsIgnoreCase("Donda [V3]"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006277929728229486/Untitled_3.png";
    
                            if (song.getEra().equalsIgnoreCase("Donda 2"))
                                thumbnail = "https://cdn.discordapp.com/attachments/992306005532758098/1006278319412625408/qiePsn17ag8xrkF7a9LA9XaotS-UGwddmBaOzRE2G5vk5yANLUeYjwNywBtdjjrfZVc289f5EWyUONqB9uTFKdKYve-zGgjQuwu5j_dlXCmVOEfTH0u8rUfQw0imCpEzsnafHOpJeVkPrL4JfqB5bcwPxQs2048.jpg";
    
                            MessageEmbed embed;
    
                            if (song.getEdit()) {
                                embed = new EmbedBuilder()
                                        .setColor(Color.getHSBColor(358, 100, 49))
                                        .setDescription(newEditString(song))
                                        .setThumbnail(thumbnail)
                                        .setAuthor("NEW EDIT")
                                        .build();
                            } else {
                                embed = new EmbedBuilder()
                                        .setColor(Color.getHSBColor(358, 100, 49))
                                        .setDescription(newSongString(song))
                                        .setThumbnail(thumbnail)
                                        .setAuthor("NEW SONG")
                                        .build();
                            }

                            textChannel.sendMessageEmbeds(embed).queue();
                        }
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
    
    public String newEditString(Song song) {
        String songInfo = song.getName().replace("\\n", " ");
        
        StringBuilder message = new StringBuilder();
    
        message.append("**").append(song.getAvailable()).append("**");
    
        message.append("\nEra: ").append(song.getEra());
    
        message.append("Song: ").append(songInfo);
        
        String[] descriptions = song.getDescription().split(":");

        message.append("\n\n**New description: **").append(descriptions[0]);
    
        message.append("\n\n**Old description: **").append(descriptions[1]);
        

        if (song.getLinks() != null)
            message.append("\n\n**Links: **\n").append(song.getLinks());
        
        return message.toString();
    }
    
    public String newSongString(Song song) {
        String songInfo = song.getName().replace("\\n", " ");
    
        StringBuilder message = new StringBuilder();
    
        message.append("**").append(song.getAvailable()).append("**");
    
        message.append("\nEra: ").append(song.getEra());
        
        message.append("Song: ").append(songInfo);
        
        message.append("\nDescription: ").append(song.getDescription());
    
        if (!song.getTrackLength().equals(""))
            message.append("\nTrack Length: ").append(song.getTrackLength());
    
        message.append("\nType: ").append(song.getType());
    
        if (!song.getQuality().equals("Not Available"))
            message.append("\nQuality: ").append(song.getQuality());
    
        if (song.getLinks() != null)
            message.append("\n\n**Links: **\n").append(song.getLinks());
        
        return message.toString();
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
