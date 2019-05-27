package club.sk1er.mods.autocomplete.sources;

import club.sk1er.mods.autocomplete.config.LocalConfig;
import net.minecraft.client.Minecraft;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class LocalSource extends AutocompleteSource {
    private LocalConfig localConfig;

    public LocalSource(LocalConfig localConfig) {
        this.localConfig = localConfig;
    }

    @Override
    public Set<String> get() {
        if (localConfig == null || !localConfig.enabled)
            return new HashSet<>();
        Collector<String, ?, Set<String>> stringSetCollector = Collectors.toSet();
        Set<String> collect = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().stream().map(networkPlayerInfo -> networkPlayerInfo.getGameProfile().getName()).collect(stringSetCollector);
        System.out.println(collect);
        return collect;
    }

    @Override
    public void refresh() {
    }
}
