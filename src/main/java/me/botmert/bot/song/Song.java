package me.botmert.bot.song;

import lombok.Getter;

@Getter
public class Song {
    
    private final String era;
    private final String name;
    private final String description;
    private final String type;
    
    Song(String era, String name, String description, String type) {
        this.era = era;
        this.name = name;
        this.description = description;
        this.type = type;
    }
    
}
