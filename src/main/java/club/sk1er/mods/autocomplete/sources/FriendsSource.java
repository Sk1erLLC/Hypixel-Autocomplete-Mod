package club.sk1er.mods.autocomplete.sources;

import club.sk1er.mods.autocomplete.AutocompleteMod;
import club.sk1er.mods.autocomplete.HypixelRank;
import club.sk1er.mods.autocomplete.config.FriendsConfig;
import gg.essential.api.utils.JsonHolder;
import net.minecraft.client.Minecraft;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FriendsSource extends AutocompleteSource {

    private Set<FriendElement> data;
    private final FriendsConfig config;

    FriendsSource(FriendsConfig config) {
        this.config = config;
    }

    @Override
    public Set<String> get(String command) {
        if (data == null || config == null)
            return new HashSet<>();
        if (((AutocompleteMod.instance.getMasterConfig().getCommands().get(command) >> AutocompleteSources.FRIENDS.ordinal()) & 1) != 1 )
            return new HashSet<>();
        return data.stream().filter(friendElement -> config.get(friendElement.rank)).map(FriendElement::getName).collect(Collectors.toSet());
    }

    @Override
    public void refresh() {
        JsonHolder fetch = fetch("https://api.sk1er.club/friends/" + Minecraft.getMinecraft().getSession().getPlayerID());
        HashSet<FriendElement> friendElements = new HashSet<>();
        for (String key : fetch.getKeys()) {
            JsonHolder jsonHolder = fetch.optJSONObject(key);
            friendElements.add(new FriendElement(jsonHolder.optString("name"), HypixelRank.parse(jsonHolder.optString("rank"))));
        }
        this.data = friendElements;
    }

    static class FriendElement {

        private final String name;
        private final HypixelRank rank;

        public FriendElement(String name, HypixelRank rank) {
            this.name = name;
            this.rank = rank;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FriendElement that = (FriendElement) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        public String getName() {
            return name;
        }
    }
}
