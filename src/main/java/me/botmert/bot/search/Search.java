package me.botmert.bot.search;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import me.botmert.bot.Constants;
import me.botmert.bot.DiscordBot;
import me.botmert.bot.sheet.Song;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class Search {
    @Getter private static final List<Search> searches = new ArrayList<>();

    private final String keyword;
    private final List<Song> songs;
    private final int pages;
    private int currentPage = 0;
    private final String InteractionId;
    private final long searchTimeStamp;

    public static Search getSongSearch(String keyword, String interactionId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Song> songs = new ArrayList<>();
            List<JsonNode> jsonData = objectMapper.readValue(new File("sheets.json"), new TypeReference<List<JsonNode>>(){});

            for (JsonNode song : jsonData) {
                if (song.size() < 8) continue;

                String name = song.get(Constants.NAME_KEY).asText();
                String nameClean = name.replace("\n", " ");

                String era = song.get(Constants.ERA_KEY).asText();

                if (!cleanAndCompare(nameClean, keyword) && !cleanAndCompare(era, keyword)) continue;

                String notes = song.get(Constants.NOTES_KEY).asText();
                String trackLength = song.get(Constants.TRACK_LENGTH_KEY).asText();
                String leakDate = song.get(Constants.LEAK_DATE_KEY).asText();
                String fileDate = song.get(Constants.FILE_DATE_KEY).asText();
                String availableLength = song.get(Constants.AVAILABLE_LENGTH_KEY).asText();
                String quality = song.get(Constants.QUALITY_KEY).asText();
                String links = "None";

                if (song.size() == 9)
                    links = song.get(Constants.LINKS_KEY).asText();

                if (nameClean.length() > 252)
                    nameClean = nameClean.substring(0, 252) + "...";

                if (notes.length() > 1015)
                    notes = notes.substring(0, 1015) + "...";

                songs.add(new Song(era, name, nameClean, notes, trackLength, leakDate, fileDate, availableLength, quality, links));
            }

            if (!songs.isEmpty()) {
                Search search = new Search(keyword, songs, songs.size(), interactionId, System.currentTimeMillis());
                searches.add(search);
                return search;
            }
        } catch (Exception e) {
            DiscordBot.getInstance().getLogger().error("Error searching songs", e);
        }

        return null;
    }

    public static void clearOldSearches() {
        final int ONE_HOUR = 60 * 60 * 1000;
        Search.getSearches().removeIf(s -> System.currentTimeMillis() - s.getSearchTimeStamp() >= ONE_HOUR);
    }

    public static Search getSearchByInteractionId(String interactionId) {
        for (Search search : searches) {
            if (search.getInteractionId().equals(interactionId)) {
                return search;
            }
        }
        return null;
    }

    public static boolean cleanAndCompare(String text1, String text2) {
        String[] specialChars = {"'", "’", ".", "-", "[", "]", "(", ")", "\""};

        for (String specChar : specialChars) {
            text1 = text1.replace(specChar, "");
            text2 = text2.replace(specChar, "");
        }

        return text1.toLowerCase().contains(text2.toLowerCase());
    }
}