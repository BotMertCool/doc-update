package me.botmert.bot.commands;

import me.botmert.bot.search.Search;
import me.botmert.bot.search.SearchUtil;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (!command.equals("search")) return;

        OptionMapping keywordOption = event.getOption("keyword");
        if (keywordOption == null) return;

        Search search = Search.getSongSearch(keywordOption.getAsString(), event.getInteraction().getId());

        MessageEmbed embed = SearchUtil.getSearchEmbed(keywordOption.getAsString(), search);

        // This is weird I check if search is null
        // inside the getSearchEmbed and here.

        if (search == null) {
            event.replyEmbeds(embed).queue();
            return;
        }

        List<Button> buttons = SearchUtil.getSearchButtons(search);
        event.replyEmbeds(embed).addActionRow(buttons).queue();
    }

}