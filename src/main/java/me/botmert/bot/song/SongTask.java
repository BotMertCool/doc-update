package me.botmert.bot.song;

import me.botmert.bot.DiscordBot;

public class SongTask implements Runnable {
    public void run() {
        try {
            DiscordBot.getInstance().getSongHandler().findNewSongs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
