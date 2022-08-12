package me.botmert.bot.song;

import lombok.Data;

@Data
public class Song {
    
    private final String era;
    private final String name;
    private final String notes;
    private final String trackLength;
    private final String leakDate;
    private final String type;
    private final String available;
    private final String quality;
    private final String links;
    //private final SongType songType;
    
}
