package me.botmert.bot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

public class MessageCommand extends ListenerAdapter {
    
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equals(":boiiooing:")) {
            System.out.println("nice");
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

