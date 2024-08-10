package me.botmert.bot.search;

import me.botmert.bot.Constants;
import me.botmert.bot.DiscordBot;
import me.botmert.bot.sheet.Song;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SearchUtil {
    public static MessageEmbed getSearchEmbed(String keyword, Search search) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (search == null) {
            embedBuilder.setColor(Color.RED);
            embedBuilder.setTitle("Error");
            embedBuilder.setDescription("No results found for \"" + keyword + "\"");
        } else {
            Song song = search.getSongs().get(search.getCurrentPage());

            embedBuilder.setDescription("Last Updated: <t:" + DiscordBot.getInstance().getLastChecked() + ":t>");

            String extra = "";
            if (!song.getQuality().equals("Not Available"))
                extra = " - %s".formatted(song.getQuality());

            embedBuilder.addField("Availability", song.getAvailableLength() + extra, false);

            embedBuilder.addField("Era", song.getEra(), false);

            if (!song.getTrackLength().isEmpty())
                embedBuilder.addField("Track Length", song.getTrackLength(), false);

            if (!song.getLeakDate().isEmpty())
                embedBuilder.addField("Leak Date", song.getLeakDate(), false);

            if (!song.getFileDate().isEmpty())
                embedBuilder.addField("File Date", song.getFileDate(), false);

            embedBuilder.addField("Notes", song.getNotes(), false);

            if (!song.getLinks().isEmpty()) {
                int songs = song.getLinks().length();
                embedBuilder.addField("Link" + (songs > 1 ? "s" : ""), song.getLinks(), false);
            }


            switch (song.getAvailableLength()) {
                case "Full", "OG File" -> embedBuilder.setColor(Color.GREEN);
                case "Snippet", "Tagged"  -> embedBuilder.setColor(Color.YELLOW);
                case "Partial", "Beat Only" -> embedBuilder.setColor(Color.MAGENTA);
                default -> embedBuilder.setColor(Color.WHITE);
            }

            embedBuilder.setTitle(song.getName());
        }

        embedBuilder.setFooter("Made by: botmert");

        return embedBuilder.build();
    }

    public static List<Button> getSearchButtons(Search search) {
        List<Button> buttons = new ArrayList<>();

        if (search == null)
            return null;

        if (search.getSongs().isEmpty())
            return null;

        boolean isLastPage = search.getCurrentPage() + 1 >= search.getPages();
        boolean isFirstPage = search.getCurrentPage() == 0;

        buttons.add(Button.primary("search-startpage", "«").withDisabled(isFirstPage));
        buttons.add(Button.primary("search-lastpage", "<").withDisabled(isFirstPage));
        buttons.add(Button.secondary("search-currentpage", (search.getCurrentPage() + 1) + "/" + search.getPages()));
        buttons.add(Button.primary("search-nextpage", ">").withDisabled(isLastPage));
        buttons.add(Button.primary("search-endpage", "»").withDisabled(isLastPage));

        return buttons;
    }

}
