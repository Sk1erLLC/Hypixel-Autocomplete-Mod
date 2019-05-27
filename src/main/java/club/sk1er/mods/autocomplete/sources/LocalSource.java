package club.sk1er.mods.autocomplete.sources;

import net.minecraft.client.Minecraft;

import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class LocalSource extends AutocompleteSource {
    @Override
    public Set<String> get() {
        Collector<String, ?, Set<String>> stringSetCollector = Collectors.toSet();
        Set<String> collect = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().stream().map(networkPlayerInfo -> networkPlayerInfo.getGameProfile().getName()).collect(stringSetCollector);
        System.out.println(collect);
        return collect;
    }

    @Override
    public void refresh() {
    }
}
