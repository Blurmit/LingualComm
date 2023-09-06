package me.blurmit.lingualcomm.menu;

import me.blurmit.lingualcomm.LingualComm;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class MenuManager implements Listener {

    private final LingualComm plugin;
    private final Map<UUID, Menu> menus;

    public MenuManager(LingualComm plugin) {
        this.plugin = plugin;
        this.menus = new HashMap<>();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void displayMenu(HumanEntity player, Menu menu) {
        menus.remove(player.getUniqueId());

        Inventory inventory = menu.getInventory();

        player.openInventory(inventory);
        menu.setInventory(inventory);

        menus.put(player.getUniqueId(), menu);
    }

    public Set<Integer> getGuiBorder(Inventory menu) {
        Set<Integer> slots = new HashSet<>();
        AtomicInteger slot = new AtomicInteger(-1);

        IntStream.range(0, menu.getSize() / 9).forEach(row -> IntStream.range(0, 9).forEach(column -> {
            slot.incrementAndGet();

            if (row != 0 && row != ((menu.getSize() / 9) - 1) && column != 0 && column != 8) {
                return;
            }

            slots.add(slot.get());
        }));

        return slots;
    }

    public Set<Integer> getGuiPoles(Inventory menu) {
        Set<Integer> slots = new HashSet<>();
        AtomicInteger slot = new AtomicInteger(-1);

        IntStream.range(0, menu.getSize() / 9).forEach(row -> IntStream.range(0, 9).forEach(column -> {
            slot.incrementAndGet();

            if (row != 0 && row != ((menu.getSize() / 9) - 1)) {
                return;
            }

            slots.add(slot.get());
        }));

        return slots;
    }

    public Map<UUID, Menu> getMenus() {
        return menus;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isInventory(event)) {
            return;
        }

        Menu menu = menus.get(event.getWhoClicked().getUniqueId());
        event.setCancelled(!menu.isModifiable());
        callButton(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!isInventory(event)) {
            return;
        }

        Menu menu = menus.get(event.getWhoClicked().getUniqueId());
        event.setCancelled(!menu.isModifiable());
        menu.onInventoryDrag(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (isInventory(event.getInventory(), player.getUniqueId())) {
            menus.get(player.getUniqueId()).onInventoryClose(event);

            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                menus.remove(player.getUniqueId());
            }, 1L);
        }
    }

    private void callButton(InventoryClickEvent event) {
        Set<Menu> menusCopy = new HashSet<>(menus.values());

        menusCopy.stream().filter(menu -> menu.getInventory().equals(event.getInventory())).forEach(menu -> {
            for (MenuButton button : menu.getButtons()) {
                if (button.getSlot() == event.getRawSlot()) {
                    menu.callButton(event);
                }
            }

            menu.onInventoryClick(event);
        });
    }

    private boolean isInventory(InventoryInteractEvent event) {
        if (!menus.containsKey(event.getWhoClicked().getUniqueId())) {
            return false;
        }

        return menus.get(event.getWhoClicked().getUniqueId()).getInventory().equals(event.getInventory());
    }

    private boolean isInventory(Inventory inventory, UUID clicker) {
        if (!menus.containsKey(clicker)) {
            return false;
        }

        return menus.get(clicker).getInventory().equals(inventory);
    }

}
