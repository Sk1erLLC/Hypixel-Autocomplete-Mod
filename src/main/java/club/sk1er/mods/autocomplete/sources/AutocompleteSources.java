package club.sk1er.mods.autocomplete.sources;

import club.sk1er.mods.autocomplete.AutocompleteMod;

import java.util.Set;

public enum AutocompleteSources {

    LOCAL(new LocalSource()),
    FRIENDS(new FriendsSource(AutocompleteMod.instance.getMasterConfig().getFriendsConfig())),
    GUILD(new GuildSource());


    private AutocompleteSource source;

    AutocompleteSources(AutocompleteSource source) {
        this.source = source;
    }

    public Set<String> get() {
        return source.get();
    }
}
