package me.botmert.bot;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import lombok.Getter;

import me.botmert.bot.song.SongHandler;

import me.botmert.bot.song.SongTask;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.security.auth.login.LoginException;

import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Getter
public class DiscordBot {
    
    @Getter private static DiscordBot instance;
    
    private final JDA client;
    private final Config config;
    private final SongHandler songHandler;
    
    public DiscordBot() throws LoginException, InterruptedException, IOException {
        instance = this;
        
        System.setProperty("config.file", System.getProperty("config.file", "application.conf"));
        config = ConfigFactory.load();
        
        this.client = JDABuilder
                .createDefault(config.getString("bot.token"))
                .setStatus(OnlineStatus.ONLINE)
                .build().awaitReady();
        
        songHandler = new SongHandler();
    }
    
    public static void main(String[] args) throws Exception {
        new DiscordBot();
        
        ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(1);
        threadPool.scheduleAtFixedRate(new SongTask(), 60, 61, TimeUnit.SECONDS);
    }
    
}
