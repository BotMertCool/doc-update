package me.botmert.bot.song;

import lombok.Getter;

@Getter
public class Song {
    
    private final String era;
    private final String name;
    private final String description;
    private final String trackLength;
    private final String leakDate;
    private final String type;
    private final String available;
    private final String quality;
    private final String links;
    private final Boolean edit;
    
    public Song(String era,
                String name,
                String description,
                String runTime,
                String leakedOn,
                String type,
                String available,
                String quality,
                String links,
                Boolean edit) {
        this.era = era;
        this.name = name;
        this.description = description;
        this.trackLength = runTime;
        this.leakDate = leakedOn;
        this.type = type;
        this.available = available;
        this.quality = quality;
        this.links = links;
        this.edit = edit;
    }
}
