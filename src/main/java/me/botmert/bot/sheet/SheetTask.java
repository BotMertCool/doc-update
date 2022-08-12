package me.botmert.bot.sheet;

import me.botmert.bot.DiscordBot;

public class SheetTask implements Runnable {
    public void run() {
        try {
            DiscordBot.getInstance().getSongHandler().findNewEdit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
