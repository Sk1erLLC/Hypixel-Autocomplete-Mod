package club.sk1er.mods.autocomplete;

import club.sk1er.mods.autocomplete.config.MasterConfig;
import club.sk1er.mods.autocomplete.sources.AutocompleteSources;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.modcore.api.ModCoreAPI;
import net.modcore.api.utils.Multithreading;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Mod(modid = AutocompleteMod.MODID, version = AutocompleteMod.VERSION)
public class AutocompleteMod {
    public static final String MODID = "hypixel_auto_complete";
    public static final String VERSION = "1.1";
    public static AutocompleteMod instance;
    private MasterConfig masterConfig;
    private boolean hypixel;
    private final Set<String> registeredCommands = new HashSet<>();

    public AutocompleteMod() {
        instance = this;
    }

    @EventHandler
    public void init(FMLPreInitializationEvent event) {
        masterConfig = new MasterConfig(event.getSuggestedConfigurationFile());
        Multithreading.runAsync(() -> {
            for (AutocompleteSources value : AutocompleteSources.values()) {
                value.refresh();
            }
        });
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ModCoreAPI.getCommandRegistry().registerCommand(new CommandAutocomplete());
    }

    public void ensure(String name, AutocompleteSources... sources) {
        if (registeredCommands.add(name)) {
            ClientCommandHandler.instance.registerCommand(new AutocompleteImpl(name, sources));
        }
    }

    public void delete(String name) {
        ICommand remove = ClientCommandHandler.instance.commandMap.remove(name);
        if (remove == null) {
            return;
        }
        ClientCommandHandler.instance.commandSet.remove(remove);
    }

    @SubscribeEvent
    public void onLoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        hypixel = !FMLClientHandler.instance().getClient().isSingleplayer()
                && (FMLClientHandler.instance().getClient().getCurrentServerData().serverIP.contains("hypixel.net") ||
                FMLClientHandler.instance().getClient().getCurrentServerData().serverName.equalsIgnoreCase("HYPIXEL"));
        if (hypixel) {
            HashMap<String, Integer> commands = masterConfig.getCommands();
            for (String s : commands.keySet()) {
                ensure(s, AutocompleteSources.values());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLogOutEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        try {
            hypixel = false;
            for (String registeredCommand : registeredCommands) {
                delete(registeredCommand);
            }
            registeredCommands.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MasterConfig getMasterConfig() {
        return masterConfig;
    }
}
