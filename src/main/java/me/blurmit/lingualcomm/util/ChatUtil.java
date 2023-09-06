package me.blurmit.lingualcomm.util;

import me.blurmit.lingualcomm.LingualComm;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatUtil {

    private static final LingualComm plugin = JavaPlugin.getPlugin(LingualComm.class);
    private static final FileConfiguration config = plugin.getConfig();

    public static String getRawMessage(String key) {
        return config.getString("Messages." + key);
    }

    public static String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&', getRawMessage(key));
    }

}
