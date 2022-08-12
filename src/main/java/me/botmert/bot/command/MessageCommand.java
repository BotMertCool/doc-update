package me.botmert.bot.command;

import me.botmert.bot.DiscordBot;
import me.botmert.bot.song.Song;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.*;

public class MessageCommand extends ListenerAdapter {
    
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getChannel().getId().equals("1005362754196090891")) {
            if(!event.getMessage().getStickers().size().equals(0) || !event.getMessage().getStickers().get(0).getId().equals("992257946136354876")) {
                event.getMessage().delete().queue();
            }
        }
        if (event.getMessage().getContentRaw().startsWith("!message")) {
            if (event.getAuthor().isBot()) return;
            if (event.getAuthor().getId().equals("348595558468026369")) {
                String message = event.getMessage().getContentRaw();
                StringBuilder stringBuilder = new StringBuilder();
    
                for (String s : message.split(" ")) {
                    if (!s.equals("!message")) {
                        stringBuilder.append(s.replace("@everyone", "@noone")).append(" ");
                    }
                }
    
                event.getMessage().delete().queue();
                event.getTextChannel().sendMessage(stringBuilder.toString()).queue();
            }

        }
    }
    
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("message")) {
            OptionMapping messageOption = event.getOption("song");
            String songName = messageOption.getAsString();
            //Message message = new Message.Interaction()
            
            event.reply("");
        }
    }
    
}

