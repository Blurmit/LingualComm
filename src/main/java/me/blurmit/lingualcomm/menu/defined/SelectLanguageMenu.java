package me.blurmit.lingualcomm.menu.defined;

import com.google.gson.JsonParser;
import me.blurmit.lingualcomm.LingualComm;
import me.blurmit.lingualcomm.translation.Language;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import me.blurmit.lingualcomm.menu.Menu;
import me.blurmit.lingualcomm.menu.MenuButton;
import me.blurmit.lingualcomm.menu.MenuType;
import me.blurmit.lingualcomm.translation.Translation;
import me.blurmit.lingualcomm.util.ChatUtil;

import java.util.*;

public class SelectLanguageMenu extends Menu {

    private final LingualComm plugin;
    private final OfflinePlayer target;
    private String message;

    private final String languageSelectedMessage;
    private String translationMessage;

    public SelectLanguageMenu(LingualComm plugin, OfflinePlayer target) {
        this.plugin = plugin;
        this.target = target;
        this.languageSelectedMessage = ChatUtil.getMessage("Language-Selected");

        int skip = 10;
        for (Language lang : Language.values()) {
            int rawID = lang.ordinal();

            if (rawID != 0 && rawID % 7 == 0) {
                skip = skip + 2;
            }

            int langID = skip + rawID;
            addButton(new MenuButton(lang.getBanner()).setSlot(langID));
        }
    }

    public SelectLanguageMenu(LingualComm plugin, OfflinePlayer target, String message) {
        this.plugin = plugin;
        this.target = target;
        this.message = message;

        this.languageSelectedMessage = ChatUtil.getMessage("Language-Selected");
        this.translationMessage = ChatUtil.getMessage("Translation-Message");

        int skip = 10;
        for (Language lang : Language.values()) {
            int rawID = lang.ordinal();

            if (rawID != 0 && rawID % 7 == 0) {
                skip = skip + 2;
            }

            int langID = skip + rawID;
            addButton(new MenuButton(lang.getBanner()).setSlot(langID));
        }
    }

    @Override
    public void callButton(InventoryClickEvent event) {
        int slot = event.getSlot();
        Language language = Arrays.stream(Language.values()).filter(lang -> lang.getSlot() == slot).findFirst().orElse(null);

        if (language == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String name = Language.getFancyName(language);
        player.sendMessage(languageSelectedMessage.replace("{language}", name));
        player.closeInventory();

        if (message == null) {
            Map<UUID, Language> languageData = plugin.getLanguageData().get(player.getUniqueId());
            if (languageData == null) {
                languageData = new HashMap<>();
            }

            languageData.put(target.getUniqueId(), language);
            plugin.getLanguageData().put(player.getUniqueId(), languageData);
        } else {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                String message = Translation.create(this.message).setLanguage(language).send();
                message = new JsonParser().parse(message).getAsJsonObject().get("translations").getAsJsonArray().get(0).getAsJsonObject().get("text").getAsString();

                String format = translationMessage.replace("{message}", message).replace("{player-name}", target.getName());
                format = PlaceholderAPI.setPlaceholders(target, format);

                player.sendMessage(format);
            });
        }
    }

    @Override
    public String getName() {
        return "Select Language";
    }

    @Override
    public int getSlots() {
        return (int) (27 + (9 * (Math.floor(Language.values().length / 9f))));
    }

    @Override
    public MenuType getType() {
        return MenuType.FILLED;
    }

}
