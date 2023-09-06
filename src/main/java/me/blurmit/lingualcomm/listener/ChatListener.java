package me.blurmit.lingualcomm.listener;

import com.google.gson.JsonParser;
import me.blurmit.lingualcomm.LingualComm;
import me.blurmit.lingualcomm.translation.Language;
import me.blurmit.lingualcomm.translation.Translation;
import me.blurmit.lingualcomm.util.ChatUtil;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ChatListener implements Listener {

    private final LingualComm plugin;

    public ChatListener(LingualComm plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (event.isCancelled()) {
            return;
        }

        for (Map.Entry<UUID, Map<UUID, Language>> entry : plugin.getLanguageData().entrySet()) {
            UUID awaitingTranslation = entry.getKey();
            Player awaitingTranslationPlayer = plugin.getServer().getPlayer(awaitingTranslation);

            if (awaitingTranslationPlayer == null) {
                continue;
            }

            Map<UUID, Language> data = entry.getValue();
            for (Map.Entry<UUID, Language> dataEntry : data.entrySet()) {
                UUID target = dataEntry.getKey();

                if (!target.equals(player.getUniqueId())) {
                    continue;
                }

                event.getRecipients().remove(awaitingTranslationPlayer);

                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                    Language language = dataEntry.getValue();
                    String rawText = event.getMessage();
                    String text = Translation.create(rawText).setLanguage(language).send();

                    if (text == null) {
                        text = rawText;
                    } else {
                        text = new JsonParser().parse(text).getAsJsonObject().get("translations").getAsJsonArray().get(0).getAsJsonObject().get("text").getAsString();
                    }

                    String message = event.getFormat();
                    if (message.endsWith(rawText)) {
                        message = message.substring(0, message.length() - rawText.length());
                        message += text;
                    }

                    awaitingTranslationPlayer.sendMessage(message);
                });
            }
        }

        String format = event.getFormat();
        BaseComponent hoverFormat = new TextComponent(format);

        String translationHover = ChatUtil.getMessage("Translation-Hover");
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(translationHover).create());
        hoverFormat.setHoverEvent(hoverEvent);

        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/translatemessage " + player.getName() + " " + event.getMessage());
        hoverFormat.setClickEvent(clickEvent);

        Set<Player> recipients = new HashSet<>(event.getRecipients());
        event.getRecipients().clear();

        for (Player recipient : recipients) {
            recipient.spigot().sendMessage(hoverFormat);
        }
    }

}
