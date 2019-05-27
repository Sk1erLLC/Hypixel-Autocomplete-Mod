package club.sk1er.mods.autocomplete;

import club.sk1er.mods.autocomplete.sources.AutocompleteSource;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.List;
import java.util.Set;

public class AutocompleteImpl extends CommandBase {
    private AutocompleteSource source;
    private String commandName;

    public AutocompleteImpl(AutocompleteSource source, String commandName) {
        this.source = source;
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
        Set<String> strings = source.get();
        return strings.toArray(new String[strings.size()]);
    }


    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + getCommandName() + " " + String.join(" ", args));
    }
}
