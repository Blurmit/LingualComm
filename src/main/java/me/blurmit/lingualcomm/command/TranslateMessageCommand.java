package me.blurmit.lingualcomm.command;

import me.blurmit.lingualcomm.LingualComm;
import me.blurmit.lingualcomm.menu.Menu;
import me.blurmit.lingualcomm.menu.defined.SelectLanguageMenu;
import me.blurmit.lingualcomm.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TranslateMessageCommand implements CommandExecutor {

    private final LingualComm plugin;

    private final String noPermissionMessage;
    private final String playerNotFoundMessage;

    public TranslateMessageCommand(LingualComm plugin) {
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

        if (!player.hasPermission("lingualcomm.command.translatemessage")) {
            player.sendMessage(noPermissionMessage);
            return false;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Correct usage: /translatemessage <player> <message>");
            return false;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(playerNotFoundMessage);
            return false;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Menu menu = new SelectLanguageMenu(plugin, target, message);

        plugin.getMenuManager().displayMenu(player, menu);
        return true;
    }

}
