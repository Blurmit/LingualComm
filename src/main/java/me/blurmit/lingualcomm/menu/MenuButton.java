package me.blurmit.lingualcomm.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MenuButton {

    private ItemStack item;
    private String name;
    private Material material;
    private List<String> lore;
    private int data;
    private int slot;
    private UUID uuid;

    public MenuButton(ItemStack item) {
        this.item = item;
        this.name = item.getItemMeta().getDisplayName();
        this.material = item.getType();
        this.lore = item.getItemMeta().getLore();
        this.slot = -1;
        this.data = item.getData().getData();
        this.uuid = UUID.randomUUID();
    }

    public MenuButton(String name) {
        this.name = name;
        this.material = Material.STONE;
        this.lore = new ArrayList<>();
        this.slot = 0;
        this.data = 0;
        this.uuid = UUID.randomUUID();
    }

    public MenuButton setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public Material getMaterial() {
        return this.material;
    }

    public MenuButton setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public MenuButton setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public int getSlot() {
        return this.slot;
    }

    public MenuButton setLore(String... lore) {
        for (int line = 0; line < lore.length; line++) {
            lore[line] = ChatColor.translateAlternateColorCodes('&', lore[line]);
        }

        this.lore = Arrays.asList(lore);
        return this;
    }

    public MenuButton setLore(List<String> lore) {
        if (lore == null) {
            return this;
        }

        this.lore = lore.stream().map(text -> ChatColor.translateAlternateColorCodes('&', text)).collect(Collectors.toList());
        return this;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public MenuButton setData(int data) {
        this.data = data;

        return this;
    }

    public int getData() {
        return data;
    }

    public UUID getID() {
        return this.uuid;
    }

    public ItemStack build() {
        ItemStack itemStack = item;
        if (itemStack == null) {
            itemStack = new ItemStack(material);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            if (name == null) {
                name = "";
            }

            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
        }

        MaterialData data = new MaterialData(Material.STAINED_GLASS_PANE);
        data.setData((byte) 7);
        itemStack.setData(data);

        return itemStack;
    }


}
