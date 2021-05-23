package club.sk1er.mods.autocomplete.config;

import club.sk1er.mods.autocomplete.HypixelRank;
import gg.essential.api.utils.JsonHolder;

import java.util.EnumMap;

public class FriendsConfig extends AutocompleteConfig {

    private final EnumMap<HypixelRank, Boolean> states = new EnumMap<>(HypixelRank.class);

    public boolean get(HypixelRank rank) {
        return states.getOrDefault(rank, true);
    }

    @Override
    public void load(JsonHolder data) {
        super.load(data);
        JsonHolder ranks = data.optJSONObject("ranks");
        for (String key : ranks.getKeys()) {
            states.put(HypixelRank.parse(key), ranks.optBoolean(key));
        }
    }

    @Override
    public void save(JsonHolder data) {
        super.save(data);
        JsonHolder jsonHolder = new JsonHolder();
        for (HypixelRank hypixelRank : states.keySet()) {
            jsonHolder.put(hypixelRank.toString(), states.get(hypixelRank));
        }
        data.put("ranks", jsonHolder);
    }

    public void toggle(HypixelRank value) {
        states.put(value, !get(value));
    }
}
