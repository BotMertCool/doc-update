package me.botmert.bot;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import lombok.Getter;

import me.botmert.bot.song.SongHandler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;

import javax.security.auth.login.LoginException;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

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
    

        
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    DiscordBot.getInstance().getSongHandler().findNewSongs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0L, 10_000L);
    }
    
    public static void main(String[] args) throws Exception {
        new DiscordBot();
    }
    
    
    
}
