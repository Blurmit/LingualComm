package me.blurmit.lingualcomm;

import lombok.Getter;
import me.blurmit.lingualcomm.listener.PlayerDisconnectListener;
import org.bukkit.plugin.java.JavaPlugin;
import me.blurmit.lingualcomm.command.TranslateCommand;
import me.blurmit.lingualcomm.command.TranslateMessageCommand;
import me.blurmit.lingualcomm.listener.ChatListener;
import me.blurmit.lingualcomm.menu.MenuManager;
import me.blurmit.lingualcomm.translation.Language;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class LingualComm extends JavaPlugin {

    @Getter
    private Map<UUID, Map<UUID, Language>> languageData;

    @Getter
    private MenuManager menuManager;

    @Override
    public void onEnable() {
        getLogger().info("Loading configuration...");
        saveDefaultConfig();
        languageData = new HashMap<>();

        getLogger().info("Loading menus...");
        menuManager = new MenuManager(this);

        getLogger().info("Loading commands...");
        getCommand("translate").setExecutor(new TranslateCommand(this));
        getCommand("translatemessage").setExecutor(new TranslateMessageCommand(this));

        getLogger().info("Registering listeners...");
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDisconnectListener(this), this);

        getLogger().info("LingualComm has been successfully loaded and enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Destroying manager instances...");
        menuManager = null;

        getLogger().info("Saving language data...");
        languageData = null;
    }

}
