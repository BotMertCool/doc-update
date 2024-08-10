package me.botmert.bot;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.Getter;
import lombok.Setter;
import me.botmert.bot.commands.CommandListener;
import me.botmert.bot.commands.SearchCommand;
import me.botmert.bot.commands.SearchStatsCommand;
import me.botmert.bot.search.SearchListener;
import me.botmert.bot.sheet.SheetHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Getter
public class DiscordBot {

    @Getter private static DiscordBot instance;

    private final JDA client;
    private final Logger logger;
    private final Config config;
    private final SheetHandler sheetHandler;
    @Setter private long lastChecked;

    public DiscordBot() {
        instance = this;
        logger = LoggerFactory.getLogger(DiscordBot.class);

        System.setProperty("config.file", System.getProperty("config.file", "application.conf"));
        this.config = ConfigFactory.load();
        logger.info("Loaded config...");

        sheetHandler = new SheetHandler();

        logger.info("Bot is starting...");
        try {
            this.client = JDABuilder
                .createDefault(
                    config.getString("bot.token"),
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGES
                )
                .addEventListeners(
                    new SearchCommand(),
                    new SearchStatsCommand(),
                    new SearchListener(),
                    new CommandListener()
                )
                .setStatus(OnlineStatus.ONLINE)
                .build()
                .awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.info("Bot started!");
    }

    public static void main(String[] args) throws Exception {
        new DiscordBot();

        ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(1);
        threadPool.scheduleAtFixedRate(new Task(), 0, 5, TimeUnit.MINUTES);
    }
}
