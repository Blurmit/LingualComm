package me.blurmit.lingualcomm.command;

import me.blurmit.lingualcomm.LingualComm;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.blurmit.lingualcomm.menu.Menu;
import me.blurmit.lingualcomm.menu.defined.SelectLanguageMenu;
import me.blurmit.lingualcomm.util.ChatUtil;

public class TranslateCommand implements CommandExecutor {

    private final LingualComm plugin;

    private final String noPermissionMessage;
    private final String playerNotFoundMessage;

    public TranslateCommand(LingualComm plugin) {
        this.plugin = plugin;

        this.noPermissionMessage = ChatUtil.getMessage("No-Permission");
        this.playerNotFoundMessage = ChatUtil.getMessage("Player-Not-Found");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("lingualcomm.command.translate")) {
            player.sendMessage(noPermissionMessage);
            return false;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Correct usage: /translate <player>");
            return false;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(playerNotFoundMessage);
            return false;
        }

        Menu menu = new SelectLanguageMenu(plugin, target);
        plugin.getMenuManager().displayMenu(player, menu);
        return true;
    }

}
