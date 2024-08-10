package me.botmert.bot.commands;

import me.botmert.bot.DiscordBot;
import me.botmert.bot.search.Search;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;


public class SearchStatsCommand  extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (!command.equals("searchstats")) return;
        Runtime runtime = Runtime.getRuntime();

        long usedMemoryBytes = runtime.totalMemory() - runtime.freeMemory();
        long totalMemoryMB = Math.round(runtime.totalMemory() / (1024.0 * 1024.0));
        long usedMemoryMB = Math.round(usedMemoryBytes / (1024.0 * 1024.0));

        EmbedBuilder embedBuilder = getEmbedBuilder(usedMemoryMB, totalMemoryMB);

        event.replyEmbeds(embedBuilder.build()).queue();
    }

    private static @NotNull EmbedBuilder getEmbedBuilder(long usedMemoryMB, long totalMemoryMB) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Search Stats");
        embedBuilder.setColor(Color.GREEN);

        String description = """
            Active Searches: %s
            Last Updated: <t:%s:t>
            Memory: %sMB | Total: %sMB
            """.formatted(Search.getSearches().size(),
                DiscordBot.getInstance().getLastChecked(),
                usedMemoryMB,
                totalMemoryMB
            );

        embedBuilder.setDescription(description);

        embedBuilder.setFooter("Made by: botmert");
        return embedBuilder;
    }
}