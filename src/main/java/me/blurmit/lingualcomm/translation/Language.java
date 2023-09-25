package me.blurmit.lingualcomm.translation;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.Arrays;
import java.util.Collections;

public enum Language {

    ENGLISH("EN", ChatColor.RED, new Pattern(DyeColor.WHITE, PatternType.STRIPE_SMALL), new Pattern(DyeColor.BLUE, PatternType.SQUARE_TOP_LEFT)),
    SPANISH("ES", ChatColor.YELLOW, new Pattern(DyeColor.BROWN, PatternType.MOJANG), new Pattern(DyeColor.BROWN, PatternType.FLOWER), new Pattern(DyeColor.YELLOW, PatternType.HALF_HORIZONTAL_MIRROR), new Pattern(DyeColor.RED, PatternType.STRIPE_LEFT), new Pattern(DyeColor.RED, PatternType.STRIPE_RIGHT)),
    FRENCH("FR", ChatColor.WHITE, new Pattern(DyeColor.BLUE, PatternType.STRIPE_TOP), new Pattern(DyeColor.RED, PatternType.STRIPE_BOTTOM)),
    GERMAN("DE", ChatColor.RED, new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT), new Pattern(DyeColor.YELLOW, PatternType.STRIPE_RIGHT)),
    ITALIAN("IT", ChatColor.WHITE, new Pattern(DyeColor.GREEN, PatternType.STRIPE_TOP), new Pattern(DyeColor.GREEN, PatternType.STRIPE_TOP), new Pattern(DyeColor.RED, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.RED, PatternType.STRIPE_BOTTOM)),
    RUSSIAN("RU", ChatColor.WHITE, new Pattern(DyeColor.RED, PatternType.STRIPE_LEFT), new Pattern(DyeColor.RED, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_CENTER), new Pattern(DyeColor.BLACK, PatternType.STRIPE_CENTER), new Pattern(DyeColor.BLACK, PatternType.STRIPE_CENTER)),
    PORTUGUESE("PT", ChatColor.GREEN, new Pattern(DyeColor.YELLOW, PatternType.RHOMBUS_MIDDLE), new Pattern(DyeColor.BLUE, PatternType.CIRCLE_MIDDLE)),
    POLISH("PL", ChatColor.WHITE, new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL_MIRROR), new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL)),
    CHINESE("ZH", ChatColor.RED, new Pattern(DyeColor.YELLOW, PatternType.SQUARE_BOTTOM_LEFT), new Pattern(DyeColor.RED, PatternType.STRIPE_SMALL), new Pattern(DyeColor.RED, PatternType.BORDER), new Pattern(DyeColor.RED, PatternType.BRICKS)),
    SWEDISH("SV", ChatColor.BLUE, new Pattern(DyeColor.YELLOW, PatternType.STRAIGHT_CROSS), new Pattern(DyeColor.YELLOW, PatternType.STRIPE_CENTER)),
    SLOVENIAN("SL", ChatColor.WHITE, new Pattern(DyeColor.BLUE, PatternType.BRICKS), new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.WHITE, PatternType.BORDER), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.RED, PatternType.HALF_VERTICAL_MIRROR), new Pattern(DyeColor.BLUE, PatternType.STRIPE_CENTER)),
    DUTCH("NL", ChatColor.WHITE, new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.RED, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLUE, PatternType.STRIPE_BOTTOM)),
    TURKISH("TR", ChatColor.RED, new Pattern(DyeColor.WHITE, PatternType.CIRCLE_MIDDLE), new Pattern(DyeColor.RED, PatternType.FLOWER), new Pattern(DyeColor.RED, PatternType.STRIPE_RIGHT)),
    JAPANESE("JP", ChatColor.WHITE, new Pattern(DyeColor.RED, PatternType.CIRCLE_MIDDLE), new Pattern(DyeColor.WHITE, PatternType.BORDER));

    private final String internalName;
    private final ChatColor color;
    private final ItemStack banner;
    private BannerMeta meta;

    Language(String internalName, ChatColor color, Pattern... patterns) {
        this.internalName = internalName;
        this.color = color;
        this.banner = new ItemStack(Material.BANNER);
        this.meta = (BannerMeta) banner.getItemMeta();

        meta.setBaseColor(DyeColor.valueOf(color.name()));
        meta.setPatterns(Arrays.asList(patterns));

        String name = getFancyName(this);
        meta.setDisplayName(color + name);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "Click to translate to " + name));

        banner.setItemMeta(meta);
    }

    public String getInternalName() {
        return internalName;
    }

    public ChatColor getColor() {
        return color;
    }

    public ItemStack getBanner() {
        return banner;
    }

    public static String getFancyName(Language language) {
        String name = language.toString().toLowerCase();
        char[] letters = name.toCharArray();
        letters[0] = Character.toUpperCase(letters[0]);

        return String.valueOf(letters);
    }

}
