package me.botmert.bot.search;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.botmert.bot.Constants;
import me.botmert.bot.DiscordBot;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SearchListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getButton().getId() == null) return;

        String buttonId = event.getButton().getId();

        if (!buttonId.equals("search-lastpage")
                && !buttonId.equals("search-nextpage")
                && !buttonId.equals("search-startpage")
                && !buttonId.equals("search-currentpage")
                && !buttonId.equals("search-endpage")
        ) {
            return;
        }

        if (event.getMessage().getInteraction() == null) {
            event.reply("Error.").setEphemeral(true).queue();
            return;
        }

        Search search = Search.getSearchByInteractionId(event.getMessage().getInteraction().getId());

        if (search == null) {
            event.reply("This search has expired.").setEphemeral(true).queue();
            return;
        }

        if (!event.getMessage().getInteraction().getUser().getId().equals(event.getUser().getId())) {
            event.reply("This is not your search").setEphemeral(true).queue();
            return;
        }

        TextInput page = TextInput.create("page", "Page", TextInputStyle.SHORT)
            .setPlaceholder("Page to go to. Type delete to delete the search.")
            .setMinLength(1)
            .setValue("1")
            .setMaxLength(5)
            .build();


        Modal modal = Modal.create("page-select", "Page Selection")
            .addComponents(ActionRow.of(page))
            .build();

        switch (buttonId) {
            case "search-lastpage" -> search.setCurrentPage(search.getCurrentPage() - 1);
            case "search-nextpage" -> search.setCurrentPage(search.getCurrentPage() + 1);
            case "search-currentpage" -> {
                event.replyModal(modal).queue();
                return;
            }
            case "search-startpage" -> search.setCurrentPage(0);
            case "search-endpage" -> search.setCurrentPage(search.getPages() - 1);
        }

        List<Button> buttons = SearchUtil.getSearchButtons(search);
        MessageEmbed embed = SearchUtil.getSearchEmbed(search.getKeyword(), search);
        event.editMessageEmbeds(embed).setActionRow(buttons).queue();
    }

    public static String trimToMaxLength(String input, int maxLength) {
        if (input == null) {
            return null;
        }
        if (input.length() > maxLength) {
            return input.substring(0, maxLength);
        }
        return input;
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("page-select")) {
            String newPage = event.getValue("page").getAsString();

            if (event.getMessage() == null && event.getMessage().getInteraction() == null) {
                event.reply("Error.").setEphemeral(true).queue();
                return;
            }

            if (newPage.isEmpty()) {
                event.reply("Enter a number for page to go to.").setEphemeral(true).queue();
                return;
            }

            Search search = Search.getSearchByInteractionId(event.getMessage().getInteraction().getId());

            if (search == null) {
                event.reply("This search has expired.").setEphemeral(true).queue();
                return;
            }

            if (!event.getMessage().getInteraction().getUser().getId().equals(event.getUser().getId())) {
                event.reply("This is not your search").setEphemeral(true).queue();
                return;
            }

            int result;
            try {
                result = Integer.parseInt(newPage);
            } catch (NumberFormatException e) {
                event.reply("Enter a valid integer").setEphemeral(true).queue();
                return;
            }

            if (result <= 0) {
                event.reply("Enter a number above 0").setEphemeral(true).queue();
                return;
            }

            if (result > search.getPages()) {
                event.reply("Enter a number under " + search.getPages()).setEphemeral(true).queue();
                return;
            }

            search.setCurrentPage(result - 1);

            List<Button> buttons = SearchUtil.getSearchButtons(search);
            MessageEmbed embed = SearchUtil.getSearchEmbed(search.getKeyword(), search);
            event.editMessageEmbeds(embed).setActionRow(buttons).queue();

            event.reply("Page set to " + newPage).setEphemeral(true).queue();
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if (!event.getName().equals("search") || !event.getFocusedOption().getName().equals("keyword")) return;
        if (event.getFocusedOption().getValue().isEmpty()) return;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Map<String, String>> data = objectMapper.readValue(new File("sheets.json"), new TypeReference<>(){});

            String userInput = event.getFocusedOption().getValue();

            var ref = new Object() {
                int counter = 0;
            };

            List<Command.Choice> suggestions = data.stream()
                .filter(entry -> entry.get(Constants.NAME_KEY) != null)
                .map(entry -> trimToMaxLength(entry.get(Constants.NAME_KEY).split("\n")[0], 99))
                .filter(name -> cleanStartsWith(name, userInput))
                .filter(a -> ref.counter++ < 25)
                .map(name -> new Command.Choice(name, name))
                .toList();
            if (suggestions.isEmpty()) return;

            event.replyChoices(suggestions).queue();
        } catch (IOException e) {
            DiscordBot.getInstance().getLogger().error("Autocomplete Error", e);
        }

    }

    public static boolean cleanStartsWith(String text1, String text2) {
        if (text1 == null || text2 == null) return false;

        String[] specialChars = {"'", "’", ".", "-", "[", "]", "(", ")", "\"", "\n"};

        for (String specChar : specialChars) {
            text1 = text1.replace(specChar, "");
            text2 = text2.replace(specChar, "");
        }

        return text1.toLowerCase().startsWith(text2.toLowerCase());
    }

}