package me.botmert.bot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class YandhiCommand extends ListenerAdapter {
    
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("yandhidate")) {
            Date date = new Date();
            int year=date.getYear();
            long epoch = new Date(year, 8, 30).getTime();
            Instant start = Instant.ofEpochMilli(epoch);
            Instant now = Instant.now();
            long diff = Duration.between(start,now).toDays();
            event.reply("Yandhi releases in " + Math.abs(diff) + " days! get ready for some MUSSIIIIIICCCC!!!!").queue();
        }
    }
    
}

