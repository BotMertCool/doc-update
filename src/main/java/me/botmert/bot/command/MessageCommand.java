package me.botmert.bot.command;

import me.botmert.bot.DiscordBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageSticker;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageCommand extends ListenerAdapter {
    
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
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
                return;
            }
        
        }
        if (event.getMessage().getContentRaw().startsWith("!role")) {
            if (event.getAuthor().isBot()) return;
            if (event.getAuthor().getId().equals("348595558468026369")) {
                String message = event.getMessage().getContentRaw();
                
                if (message.split(" ").length == 2) {
                    
                    if (event.getGuild().getRoleById(message.split(" ")[1]) != null) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(message.split(" ")[1])).queue();
                    }

                    
                    
                }
                //event.getMessage().delete().queue();
                event.getTextChannel().sendMessage("ok.").queue();
                return;
            }
        
        }
        if (event.getChannel().getId().equals("1005362754196090891")) {
    
            if (event.getAuthor().isBot()) return;
            if (!(event.getMessage().getContentRaw().equals("")) || event.getMessage().getStickers().isEmpty()) {
                event.getMessage().delete().queue();
            } else if (!(event.getMessage().getStickers().size() == 1) && !event.getMessage().getStickers().get(0).getId().equals("992257946136354876")) {
                event.getMessage().delete().queue();
            }
            
            
            
            
        }
        
    }
}



