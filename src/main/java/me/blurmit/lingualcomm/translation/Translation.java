package me.blurmit.lingualcomm.translation;

import lombok.AllArgsConstructor;
import me.blurmit.lingualcomm.LingualComm;
import me.blurmit.lingualcomm.request.IRequest;
import me.blurmit.lingualcomm.request.Request;
import me.blurmit.lingualcomm.request.RequestType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@AllArgsConstructor(staticName = "create")
public class Translation implements IRequest {

    private static final String FREE_API_SERVER = "https://api-free.deepl.com/v2/translate";
    private static final String DEFAULT_API_KEY = JavaPlugin.getPlugin(LingualComm.class).getConfig().getString("API-Key");

    private final TranslationBuilder translationBuilder;

    public static TranslationBuilder create(String text) {
        return TranslationBuilder.create().setText(text).setAPIKey(DEFAULT_API_KEY);
    }

    public String send() {
        try {
            return Request.create(RequestType.POST)
                    .setURL(FREE_API_SERVER)
                    .setAuthorization("DeepL-Auth-Key " + translationBuilder.getAPIKey())
                    .addURLData("text", translationBuilder.getText())
                    .addURLData("target_lang", translationBuilder.getLanguage().getInternalName())
                    .send();
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to send translation request to API server", e);
            return translationBuilder.getText();
        }
    }

}
