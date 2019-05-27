package club.sk1er.mods.autocomplete;

import club.sk1er.mods.autocomplete.sources.AutocompleteSources;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AutocompleteImpl extends CommandBase {
    private AutocompleteSources[] sources;
    private String commandName;

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    public AutocompleteImpl(String commandName, AutocompleteSources... source) {
        this.sources = source;
        this.commandName = commandName;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return getListOfStringsMatchingLastWord(args, this.getListOfPlayerUsernames());
    }

    private String[] getListOfPlayerUsernames() {
        Set<String> set = new HashSet<>();
        for (AutocompleteSources autocompleteSource : sources) {
            set.addAll(autocompleteSource.get());
        }
        return set.toArray(new String[0]);
    }


    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + getCommandName() + " " + String.join(" ", args));
    }
}
