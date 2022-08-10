package me.botmert.bot;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import lombok.Getter;

import me.botmert.bot.command.MessageCommand;
import me.botmert.bot.command.SnippetCommand;
import me.botmert.bot.song.SongHandler;

import me.botmert.bot.song.SongTask;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import javax.security.auth.login.LoginException;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Getter
public class DiscordBot {
    
    @Getter private static DiscordBot instance;
    
    private final JDA client;
    private final Config config;
    private final SongHandler songHandler;
    
    public DiscordBot() throws LoginException, InterruptedException {
        instance = this;
        
        System.setProperty("config.file", System.getProperty("config.file", "application.conf"));
        config = ConfigFactory.load();
        
        this.client = JDABuilder
                .createDefault(config.getString("bot.token"))
                .addEventListeners(new SnippetCommand(), new MessageCommand())
                .setStatus(OnlineStatus.ONLINE)
                .build().awaitReady();
    
        List<CommandData> commandData = new ArrayList<>();
    
        OptionData option1 = new OptionData(OptionType.STRING, "song", "Name sensitive", true);
        OptionData option2 = new OptionData(OptionType.STRING, "message", "Message text", true);
        //commandData.add(Commands.slash("snippet", "Find snippets for a song in the tracker").addOptions(option1));
        //commandData.add(Commands.slash("message", "Lets BOTMERT speak through the bot").addOptions(option2));
    
        Guild guild = DiscordBot.getInstance().getClient().getGuildById(DiscordBot.getInstance().getConfig().getString("bot.guild-id"));
        if (guild != null) {
            guild.updateCommands().addCommands(commandData).queue();
        }
        
        songHandler = new SongHandler();
    }
    
    public static void main(String[] args) throws Exception {
        new DiscordBot();
        
        ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(1);
        threadPool.scheduleAtFixedRate(new SongTask(), 1, 90, TimeUnit.SECONDS);
        threadPool.scheduleAtFixedRate(new BotTask(), 1, 8, TimeUnit.MINUTES);
    }
    
    
    
}
