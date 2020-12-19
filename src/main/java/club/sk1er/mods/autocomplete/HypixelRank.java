package club.sk1er.mods.autocomplete;

public enum HypixelRank {
    ADMIN("Admin"),
    MODERATOR("Mod"),
    HELPER("Helper"),
    YOUTUBER("YouTuber"),
    MVP_PLUS_PLUS("MVP++"),
    MVP_PLUS("MVP+"),
    MVP("MVP"),
    VIP_PLUS("VIP+"),
    VIP("VIP"),
    NONE("NONE"),
    ERROR("Unidentified");


    private final String displayName;

    HypixelRank(String displayName) {
        this.displayName = displayName;
    }

    public static HypixelRank parse(String in) {
        try {
            return valueOf(in);
        } catch (Throwable t) {
            return ERROR;
        }
    }

    public String getDisplayName() {
        return displayName;
    }

}
