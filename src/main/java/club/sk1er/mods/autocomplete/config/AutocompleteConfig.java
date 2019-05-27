package club.sk1er.mods.autocomplete.config;

import club.sk1er.mods.autocomplete.JsonHolder;

public abstract class AutocompleteConfig {
    public boolean enabled;

    public void load(JsonHolder data) {
        this.enabled = data.optBoolean("enabled", true);
    }

    public void save(JsonHolder data) {
        data.put("enabled", enabled);
    }
}
