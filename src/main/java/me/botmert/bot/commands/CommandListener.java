package me.botmert.bot.commands;

import me.botmert.bot.DiscordBot;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandListener extends ListenerAdapter {
    private final String[] enabledServers = {
        "993584205130903552",
        "1162132641299046581",
        "1030220615950532668",
    };

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        boolean isEnabled = false;
        for (String enabledServer : enabledServers) {
            if (event.getGuild().getId().equals(enabledServer)) isEnabled = true;
        }
        if (!isEnabled) return;

        List<CommandData> commands = new ArrayList<>();

        commands.add(Commands.slash("search", "Search through songs on the tracker.").addOptions(
            new OptionData(OptionType.STRING, "keyword", "Keywords of your search.", true, true)
        ));

        commands.add(Commands.slash("searchstats", "See current stats of the search command."));

        DiscordBot.getInstance().getLogger().info("Initializing commands on guild \"{}\"", event.getGuild().getName());

        for (CommandData command : commands) {
            DiscordBot.getInstance().getLogger().info("Command \"{}\" has been initialized", command.getName());
            event.getGuild().upsertCommand(command).queue();
        }
    }
}
