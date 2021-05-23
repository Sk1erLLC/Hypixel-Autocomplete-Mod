package club.sk1er.mods.autocomplete;

import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.utils.GuiUtil;

public class CommandAutocomplete extends Command {
    public CommandAutocomplete() {
        super("hypixelautocomplete");
    }

    @DefaultHandler
    public void handle() {
        GuiUtil.open(new AutocompleteConfigGUI());
    }
}
