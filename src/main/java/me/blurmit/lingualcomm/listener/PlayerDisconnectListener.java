package me.blurmit.lingualcomm.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import me.blurmit.lingualcomm.LingualComm;

public class PlayerDisconnectListener implements Listener {

    private final LingualComm plugin;

    public PlayerDisconnectListener(LingualComm plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getLanguageData().remove(event.getPlayer().getUniqueId());
    }

}
