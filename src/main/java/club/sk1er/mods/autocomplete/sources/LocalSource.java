package club.sk1er.mods.autocomplete.sources;

import net.minecraft.client.Minecraft;

import java.util.Set;
import java.util.stream.Collectors;

public class LocalSource extends AutocompleteSource {
    @Override
    public Set<String> get() {
        return Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().stream().filter(networkPlayerInfo -> networkPlayerInfo.getGameProfile().getId().toString().charAt(12) == 4).map(networkPlayerInfo -> networkPlayerInfo.getGameProfile().getName()).collect(Collectors.toSet());
    }
}
