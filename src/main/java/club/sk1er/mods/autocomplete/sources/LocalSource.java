package club.sk1er.mods.autocomplete.sources;

import club.sk1er.mods.autocomplete.AutocompleteMod;
import club.sk1er.mods.autocomplete.config.LocalConfig;
import net.minecraft.client.Minecraft;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class LocalSource extends AutocompleteSource {
    private final LocalConfig localConfig;

    public LocalSource(LocalConfig localConfig) {
        this.localConfig = localConfig;
    }

    @Override
    public Set<String> get(String command) {
        if (localConfig == null)
            return new HashSet<>();
        if (((AutocompleteMod.instance.getMasterConfig().getCommands().get(command) >> AutocompleteSources.LOCAL.ordinal()) & 1) != 1)
            return new HashSet<>();
        Collector<String, ?, Set<String>> stringSetCollector = Collectors.toSet();
        return Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().stream().map(networkPlayerInfo -> networkPlayerInfo.getGameProfile().getName()).collect(stringSetCollector);
    }

    @Override
    public void refresh() {
    }
}
