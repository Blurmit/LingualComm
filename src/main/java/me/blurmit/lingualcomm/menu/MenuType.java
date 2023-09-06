package me.blurmit.lingualcomm.menu;

import me.blurmit.lingualcomm.LingualComm;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public enum MenuType {

    EMPTY {
        @Override
        public Inventory getInventory(Menu menu) {
            Inventory gui = Bukkit.createInventory(null, menu.getSlots(), ChatColor.translateAlternateColorCodes('&', menu.getName()));

            for (MenuButton button : menu.getButtons()) {
                if (button.getSlot() == -1) {
                    gui.addItem(button.build());
                    button.setSlot(gui.first(button.build()));
                    continue;
                }

                gui.setItem(button.getSlot(), button.build());
            }

            return gui;
        }
    },
    BORDERED_POLES {
        @Override
        public Inventory getInventory(Menu menu) {
            Inventory gui = Bukkit.createInventory(null, menu.getSlots(), ChatColor.translateAlternateColorCodes('&', menu.getName()));
            for (int slot : plugin.getMenuManager().getGuiPoles(gui)) {
                gui.setItem(slot, fillerItem);
            }

            for (MenuButton button : menu.getButtons()) {
                if (button == null) {
                    continue;
                }

                if (button.getSlot() == -1) {
                    gui.addItem(button.build());
                    button.setSlot(gui.first(button.build()));
                    continue;
                }

                gui.setItem(button.getSlot(), button.build());
            }

            return gui;
        }
    },
    FILLED {
        @Override
        public Inventory getInventory(Menu menu) {
            Inventory gui = Bukkit.createInventory(null, menu.getSlots(), ChatColor.translateAlternateColorCodes('&', menu.getName()));

            for (int i = 0; i < menu.getSlots(); i++) {
                gui.setItem(i, fillerItem);
            }

            for (MenuButton button : menu.getButtons()) {
                gui.setItem(button.getSlot(), button.build());
            }

            return gui;
        }
    },
    PAGED {
        @Override
        public Inventory getInventory(Menu menu) {
            Inventory gui = Bukkit.createInventory(null, menu.getSlots(), ChatColor.translateAlternateColorCodes('&', menu.getName()));
            plugin.getMenuManager().getGuiBorder(gui).forEach(slot -> gui.setItem(slot, fillerItem));

            for (MenuButton button : menu.getButtons()) {
                if (button.getSlot() == -1) {
                    gui.addItem(button.build());
                    button.setSlot(gui.first(button.build()));
                    continue;
                }

                gui.setItem(button.getSlot(), button.build());
            }

            if (menu.isFull(gui)) {
                menu.addButton(nextPageItem);
                gui.setItem(nextPageItem.getSlot(), nextPageItem.build());
            }

            if (menu.getPage() != 0) {
                menu.addButton(previousPageItem);
                gui.setItem(previousPageItem.getSlot(), previousPageItem.build());
            }

            return gui;
        }
    };

    final LingualComm plugin;
    final MenuButton nextPageItem;
    final MenuButton previousPageItem;
    final ItemStack fillerItem;

    MenuType() {
        plugin = JavaPlugin.getPlugin(LingualComm.class);

        fillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15);
        ItemMeta meta = fillerItem.getItemMeta();
        meta.setDisplayName("");
        fillerItem.setItemMeta(meta);

        nextPageItem = new MenuButton(ChatColor.GREEN + "Next Page")
                .setMaterial(Material.ARROW)
                .setLore(ChatColor.GRAY + "Click to go to the next page.")
                .setSlot(53);

        previousPageItem = new MenuButton(ChatColor.GREEN + "Previous Page")
                .setMaterial(Material.ARROW)
                .setLore(ChatColor.GRAY + "Click to go to the previous page.")
                .setSlot(45);
    }

    public ItemStack getFillerItem() {
        return fillerItem;
    }

    public abstract Inventory getInventory(Menu menu);

}
