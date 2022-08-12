package me.botmert.bot.command;

import javafx.concurrent.Task;
import me.botmert.bot.DiscordBot;
import me.botmert.bot.sheet.SheetTask;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StopCommand extends ListenerAdapter {
    
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
    
        Guild guild = DiscordBot.getInstance().getClient().getGuildById(DiscordBot.getInstance().getConfig().getString("bot.guild-id"));
        TextChannel logsChannel;
    
        if (guild != null) {
            logsChannel = guild.getTextChannelById(DiscordBot.getInstance().getConfig().getString("bot.logs-id"));
        } else {
            System.out.println("GUILD NULL LOOOOL");
            logsChannel = null;
        }
        
        if (command.equals("stop")) {
            if (logsChannel == null) {
                System.out.println("TEXT CHANNEL NULL LOOOOL");
                return;
            }
            
            if (event.getUser().getId().equals("348595558468026369")){
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                event.reply("Stopping...").queue();
                logsChannel.sendMessageEmbeds(new EmbedBuilder().setColor(Color.RED).setDescription("Stopping bot. | " + formatter.format(date) + " EST").build()).queue();
                
                ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(1);
                threadPool.schedule(new SheetTask(), 5, TimeUnit.SECONDS);
            } else {
                event.reply("You do not have permissions to use this command.").queue();
            }

        }
    }
    
    public class StopTask implements Runnable {
        public void run() {
            try {
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
