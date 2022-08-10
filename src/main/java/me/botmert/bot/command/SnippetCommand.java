package me.botmert.bot.command;

import me.botmert.bot.DiscordBot;
import me.botmert.bot.song.Song;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.*;

public class SnippetCommand extends ListenerAdapter {
    
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("snippet")) {
            OptionMapping messageOption = event.getOption("song");
            String songName = messageOption.getAsString();
    
            List<Song> songSet = new ArrayList<>();
            
            EmbedBuilder embedMessage = new EmbedBuilder();
            
            if (songName.length() == 1)
                embedMessage.setColor(Color.RED).addField("WTF", "Please use more characters in your search!", false);
            /*
            for (Song song : DiscordBot.getInstance().getSongHandler().getSongs()) {
                if (song.getAvailable().equalsIgnoreCase("Snippet")) {
                    System.out.println(song.getAllNames());
                    for (String names : song.getAllNames()) {
                        if (names.toLowerCase().startsWith(songName.toLowerCase())) {
                            songSet.add(song);
                        }
                    }
                }
            }
            */
            List<MessageEmbed> embedList = new ArrayList<>();
            
            if (songSet.size() <= 2) {
                embedMessage.setColor(Color.RED).setDescription("The song \"" + songName + "\" doesn't have any snippets!").build();
            } else if (songSet.size() == 18) {
                embedMessage.setColor(Color.RED).setDescription("The song \"" + songName + "\" doesn't have any snippets!").build();
            } else {
                event.reply("Sending snippets:").queue();
                for (Song song : songSet) {
                    event.getTextChannel().sendMessageEmbeds(embedMessage.setColor(Color.GREEN).setDescription("**Name: **" + song.getName().split("\\n")[0] + "\n**Links: **" + song.getLinks()).build()).queue();
                }
            }
        }
    }

}
