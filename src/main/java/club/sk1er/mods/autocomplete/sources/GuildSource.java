package club.sk1er.mods.autocomplete.sources;

import club.sk1er.mods.autocomplete.AutocompleteMod;
import club.sk1er.mods.autocomplete.config.GuildConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import gg.essential.api.utils.JsonHolder;
import net.minecraft.client.Minecraft;

import java.util.HashSet;
import java.util.Set;

public class GuildSource extends AutocompleteSource {

    private HashSet<String> set = new HashSet<>();
    private final GuildConfig config;

    GuildSource(GuildConfig config) {
        this.config = config;
    }

    @Override
    public Set<String> get(String command) {
        if (config == null)
            return new HashSet<>();
        if (((AutocompleteMod.instance.getMasterConfig().getCommands().get(command) >> AutocompleteSources.GUILD.ordinal()) & 1) != 1)
            return new HashSet<>();
        return set;
    }

    @Override
    public void refresh() {
        JsonHolder fetch = fetch("https://api.sk1er.club/guild/player/" + Minecraft.getMinecraft().getSession().getPlayerID());
        HashSet<String> players = new HashSet<>();
        JsonArray jsonElements = fetch.optJSONObject("guild").optJSONArray("members");
        for (JsonElement jsonElement : jsonElements) {
            JsonHolder jsonHolder = new JsonHolder(jsonElement.getAsJsonObject());
            players.add(jsonHolder.optString("name"));
        }
        this.set = players;
    }
}
