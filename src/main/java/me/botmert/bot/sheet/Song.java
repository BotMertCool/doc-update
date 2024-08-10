package me.botmert.bot.sheet;

import lombok.Data;

import java.util.List;

@Data
public class Song {
    private final String era;
    private final String rawName;
    private final String name;
    private final String notes;
    private final String trackLength;
    private final String leakDate;
    private final String fileDate;
    private final String availableLength;
    private final String quality;
    private final String links;
}
