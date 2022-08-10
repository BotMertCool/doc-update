package me.botmert.bot.song;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class Song {
    
    private final String era;
    private final String name;
    private final List<String> allNames;
    private final String altNames;
    private final String features;
    private final String description;
    private final String trackLength;
    private final String leakDate;
    private final String type;
    private final String available;
    private final String quality;
    private final String links;
    private final boolean edit;
    
    public boolean getEdit() {
        return this.edit;
    }

}
