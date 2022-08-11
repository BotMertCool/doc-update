package me.botmert.bot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

public class TheEndCommand extends ListenerAdapter {
    
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        
        if (command.equals("theend")) {
            Random random = new Random();
            if (random.nextBoolean()) {
                if (random.nextBoolean()) {
                    event.reply("There is " + random.nextInt(10000) + " years until were all dead :DDD").queue();
                } else {
                    event.reply("There is " + random.nextInt(100000) + " days until were all dead :DDD").queue();
                }
            } else {
                if (random.nextBoolean()) {
                    event.reply("There is " + random.nextInt(24) + " hours until were all dead :DDD").queue();
                } else {
                    if (random.nextBoolean()) {
                        event.reply("There is " + random.nextInt(60) + " minutes until were all dead :DDD").queue();
                    } else {
                        event.reply("There is " + random.nextInt(60) + " seconds until were all dead :DDD").queue();
                    }
                    
                }
            }

        }
    }
    
}

