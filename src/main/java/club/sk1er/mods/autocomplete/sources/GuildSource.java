package club.sk1er.mods.autocomplete.sources;

import club.sk1er.mods.autocomplete.JsonHolder;
import club.sk1er.mods.autocomplete.config.GuildConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;

import java.util.HashSet;
import java.util.Set;

public class GuildSource extends AutocompleteSource {

    private HashSet<String> set = new HashSet<>();
    private GuildConfig config;

    GuildSource(GuildConfig config) {
        this.config = config;
    }

    @Override
    public Set<String> get() {
        if (config == null)
            return new HashSet<>();
        if (!config.enabled)
            return new HashSet<>();
        return set;
    }

    @Override
    public void refresh() {
        JsonHolder fetch = fetch("https://api.sk1er.club/guild/player/" + Minecraft.getMinecraft().getSession().getPlayerID());
        HashSet<String> players = new HashSet<>();
        JsonArray jsonElements = fetch.optJsonObject("guild").optJSONArray("members");
        for (JsonElement jsonElement : jsonElements) {
            JsonHolder jsonHolder = new JsonHolder(jsonElement.getAsJsonObject());
            players.add(jsonHolder.optString("name"));
        }
        this.set = players;
    }
}
