package me.botmert.bot;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import lombok.Getter;

@Getter
public class Settings {
    
    private final Config config;
    
    public Settings() {
        System.setProperty("config.file", System.getProperty("config.file", "application.conf"));
        config = ConfigFactory.load();
    }
    
}
