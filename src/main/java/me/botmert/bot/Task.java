package me.botmert.bot;

import me.botmert.bot.search.Search;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
public class Task implements Runnable {
    @Override
    public void run() {
        DiscordBot.getInstance().getSheetHandler().downloadSheetsJson();

        DiscordBot.getInstance().getLogger().info("Clearing old searches.");
        Search.clearOldSearches();

        JDA jda = DiscordBot.getInstance().getClient();

        int totalMembers = 0;
        for (Guild guild : jda.getGuilds()) {
            totalMembers += guild.getMemberCount();
        }

        jda.getPresence().setActivity(Activity.watching(totalMembers + " members search for songs."));
    }

}
