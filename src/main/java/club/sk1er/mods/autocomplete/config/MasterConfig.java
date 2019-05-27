package club.sk1er.mods.autocomplete.config;

import club.sk1er.mods.autocomplete.JsonHolder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class MasterConfig {

    private FriendsConfig friendsConfig = new FriendsConfig();
    private GuildConfig guildConfig = new GuildConfig();
    private LocalConfig localConfig = new LocalConfig();

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

    private void load(JsonHolder data) {
        friendsConfig.load(data.optJsonObject("friends"));
        localConfig.load(data.optJsonObject("local"));
        guildConfig.load(data.optJsonObject("guild"));
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
    }

    public FriendsConfig getFriendsConfig() {
        return friendsConfig;
    }
}
