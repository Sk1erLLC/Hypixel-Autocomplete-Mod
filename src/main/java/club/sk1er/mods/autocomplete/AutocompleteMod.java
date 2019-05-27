package club.sk1er.mods.autocomplete;

import club.sk1er.mods.autocomplete.config.MasterConfig;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = AutocompleteMod.MODID, version = AutocompleteMod.VERSION)
public class AutocompleteMod {
    public static final String MODID = "hypixel_auto_complete";
    public static final String VERSION = "1.0";
    public static AutocompleteMod instance;
    private MasterConfig masterConfig;

    public AutocompleteMod() {
        instance = this;
    }

    @EventHandler
    public void init(FMLPreInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new CommandAutocomplete());
        masterConfig = new MasterConfig(event.getSuggestedConfigurationFile());
    }

    public MasterConfig getMasterConfig() {
        return masterConfig;
    }
}
