package club.sk1er.mods.autocomplete.sources;

import club.sk1er.mods.autocomplete.JsonHolder;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Set;

public abstract class AutocompleteSource {

    public abstract Set<String> get(String command);

    public JsonHolder fetch(String url) {
        url = url.replace(" ", "%20");
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 (Autocomplete Mod V1)");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            InputStream is = connection.getInputStream();
            return new JsonHolder(IOUtils.toString(is, Charset.defaultCharset()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObject object = new JsonObject();
        object.addProperty("success", false);
        object.addProperty("cause", "Exception");
        return new JsonHolder(object);
    }

    public abstract void refresh();
}
