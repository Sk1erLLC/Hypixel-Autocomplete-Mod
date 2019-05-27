package club.sk1er.mods.autocomplete.config;

import club.sk1er.mods.autocomplete.JsonHolder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MasterConfig {

    private FriendsConfig friendsConfig = new FriendsConfig();
    private GuildConfig guildConfig = new GuildConfig();
    private LocalConfig localConfig = new LocalConfig();
    private boolean enabled = true;
    private HashMap<String, Integer> commands = new HashMap<>();

    public MasterConfig(File suggestedConfigurationFile) {
        JsonHolder data = null;
        if (suggestedConfigurationFile.exists()) {
            try {
                data = new JsonHolder(FileUtils.readFileToString(suggestedConfigurationFile));
            } catch (Exception ignored) {

            }
        }
        if (data == null) {
            data = new JsonHolder();
            commands.put("msg", 0b111);
            commands.put("tell", 0b111);
            commands.put("w", 0b111);
            commands.put("whisper", 0b111);
            commands.put("party", 0b111);
            commands.put("p", 0b111);
        }
        load(data);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            JsonHolder data1 = new JsonHolder();
            save(data1);
            try {
                FileUtils.write(suggestedConfigurationFile, data1.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    public HashMap<String, Integer> getCommands() {
        return commands;
    }

    private void load(JsonHolder data) {
        friendsConfig.load(data.optJsonObject("friends"));
        localConfig.load(data.optJsonObject("local"));
        guildConfig.load(data.optJsonObject("guild"));
        enabled = data.optBoolean("enabled", true);
        JsonHolder commands = data.optJsonObject("commands");
        for (String key : commands.getKeys()) {
            this.commands.put(key, commands.optInt(key));
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private void save(JsonHolder data) {
        JsonHolder friends = new JsonHolder();
        JsonHolder guild = new JsonHolder();
        JsonHolder local = new JsonHolder();
        friendsConfig.save(friends);
        localConfig.save(local);
        guildConfig.save(guild);
        data.put("friends", friends);
        data.put("guild", guild);
        data.put("local", local);
        data.put("enabled", enabled);
        JsonHolder commands = new JsonHolder();
        for (String s : this.commands.keySet()) {
            commands.put(s, this.commands.get(s));
        }
        data.put("commands", commands);
    }

    public GuildConfig getGuildConfig() {
        return guildConfig;
    }

    public LocalConfig getLocalConfig() {
        return localConfig;
    }

    public FriendsConfig getFriendsConfig() {
        return friendsConfig;
    }
}
