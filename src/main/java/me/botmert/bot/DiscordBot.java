package me.botmert.bot;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import lombok.Getter;

import me.botmert.bot.command.*;
import me.botmert.bot.sheet.SheetHandler;

import me.botmert.bot.sheet.SheetTask;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import javax.security.auth.login.LoginException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Getter
public class DiscordBot {
    
    @Getter private static DiscordBot instance;
    
    private final JDA client;
    private final Config config;
    private final SheetHandler songHandler;
    
    public DiscordBot() throws LoginException, InterruptedException {
        instance = this;
        
        System.setProperty("config.file", System.getProperty("config.file", "application.conf"));
        this.config = ConfigFactory.load();
        
        this.client = JDABuilder
                .createDefault(config.getString("bot.token"))
                .addEventListeners(
                        new SnippetCommand(),
                        new MessageCommand(),
                        new YandhiCommand(),
                        new StopCommand(),
                        new AbbContextMenu()
                )
                .setStatus(OnlineStatus.ONLINE)
                .build().awaitReady();
    
        List<CommandData> commandData = new ArrayList<>();
    
        OptionData option1 = new OptionData(OptionType.STRING, "song", "Name sensitive", true);
        OptionData option2 = new OptionData(OptionType.STRING, "message", "Message text", true);
        //commandData.add(Commands.slash("snippet", "Find snippets for a song in the tracker").addOptions(option1));
        commandData.add(Commands.slash("yandhidate", "Days until kanye drops yandhi"));
        commandData.add(Commands.slash("stop", "Exit the bot system process."));
        commandData.add(Commands.message("Undo abbreviation"));
    
        Guild guild = DiscordBot.getInstance().getClient().getGuildById(DiscordBot.getInstance().getConfig().getString("bot.guild-id"));
        if (guild != null) {
            guild.updateCommands().addCommands(commandData).queue();
        }
        
        this.songHandler = new SheetHandler();
    }
    
    public static void main(String[] args) throws Exception {
        new DiscordBot();
        
        ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(1);
        threadPool.scheduleAtFixedRate(new SheetTask(), 1, 90, TimeUnit.SECONDS);
        threadPool.scheduleAtFixedRate(new BotTask(), 1, 8, TimeUnit.MINUTES);
    }
    
    
    
}
