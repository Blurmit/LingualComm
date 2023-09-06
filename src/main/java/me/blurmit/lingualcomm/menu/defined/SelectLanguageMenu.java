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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

        for (Language lang : Language.values()) {
            addButton(new MenuButton(lang.getBanner()).setSlot(10 + lang.ordinal()));
        }
    }

    public SelectLanguageMenu(LingualComm plugin, OfflinePlayer target, String message) {
        this.plugin = plugin;
        this.target = target;
        this.message = message;

        this.languageSelectedMessage = ChatUtil.getMessage("Language-Selected");
        this.translationMessage = ChatUtil.getMessage("Translation-Message");

        for (Language lang : Language.values()) {
            addButton(new MenuButton(lang.getBanner()).setSlot(10 + lang.ordinal()));
        }
    }

    @Override
    public void callButton(InventoryClickEvent event) {
        int slot = event.getSlot();
        int index = slot - 10;
        Language language = Language.values()[index];

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
        return 27;
    }

    @Override
    public MenuType getType() {
        return MenuType.FILLED;
    }

}