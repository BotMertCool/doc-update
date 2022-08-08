package me.botmert.bot.song;

import me.botmert.bot.DiscordBot;

import java.io.IOException;
import java.util.TimerTask;

public class SongTask extends TimerTask {
    @Override
    public void run() {
        try {
            DiscordBot.getInstance().getSongHandler().findNewSongs();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
