package club.sk1er.mods.autocomplete;

import net.modcore.api.commands.Command;
import net.modcore.api.commands.DefaultHandler;
import net.modcore.api.utils.GuiUtil;

public class CommandAutocomplete extends Command {
    public CommandAutocomplete() {
        super("hypixelautocomplete");
    }

    @DefaultHandler
    public void handle() {
        GuiUtil.open(new AutocompleteConfigGUI());
    }
}
