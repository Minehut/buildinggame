package com.gmail.stefvanschiedev.buildinggame.utils.guis.moboptions.mobs.horse;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.gmail.stefvanschiedev.buildinggame.Main;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

/**
 * A menu for changing the color of a horse
 *
 * @since 5.3.0
 */
class HorseColorSelectionMenu extends Gui {

    /**
     * {@inheritDoc}
     */
    HorseColorSelectionMenu(Horse horse) {
        super(Main.getInstance(), 1, ChatColor.GREEN + "Change the horse color");

        OutlinePane pane = new OutlinePane(1, 0, 8, 1);

        //black
        ItemStack black = new Wool(DyeColor.BLACK).toItemStack(1);
        ItemMeta blackMeta = black.getItemMeta();
        blackMeta.setDisplayName(ChatColor.GREEN + "Black");
        black.setItemMeta(blackMeta);

        pane.addItem(new GuiItem(black, event -> {
            horse.setColor(Horse.Color.BLACK);

            event.setCancelled(true);
        }));

        //brown
        ItemStack brown = new Wool(DyeColor.BROWN).toItemStack(1);
        ItemMeta brownMeta = brown.getItemMeta();
        brownMeta.setDisplayName(ChatColor.GREEN + "Brown");
        brown.setItemMeta(brownMeta);

        pane.addItem(new GuiItem(brown, event -> {
            horse.setColor(Horse.Color.BROWN);

            event.setCancelled(true);
        }));

        //chestnut
        ItemStack chestnut = new ItemStack(Material.RED_NETHER_BRICKS);
        ItemMeta chestnutMeta = chestnut.getItemMeta();
        chestnutMeta.setDisplayName(ChatColor.GREEN + "Chestnut");
        chestnut.setItemMeta(chestnutMeta);

        pane.addItem(new GuiItem(chestnut, event -> {
            horse.setColor(Horse.Color.CHESTNUT);

            event.setCancelled(true);
        }));

        //creamy
        ItemStack creamy = new ItemStack(Material.SANDSTONE);
        ItemMeta creamyMeta = creamy.getItemMeta();
        creamyMeta.setDisplayName(ChatColor.GREEN + "Creamy");
        creamy.setItemMeta(creamyMeta);

        pane.addItem(new GuiItem(creamy, event -> {
            horse.setColor(Horse.Color.CREAMY);

            event.setCancelled(true);
        }));

        //dark brown
        ItemStack darkBrown = new ItemStack(Material.NETHER_BRICK);
        ItemMeta darkBrownMeta = darkBrown.getItemMeta();
        darkBrownMeta.setDisplayName(ChatColor.GREEN + "Dark brown");
        darkBrown.setItemMeta(darkBrownMeta);

        pane.addItem(new GuiItem(darkBrown, event -> {
            horse.setColor(Horse.Color.DARK_BROWN);

            event.setCancelled(true);
        }));

        //gray
        ItemStack gray = new Wool(DyeColor.GRAY).toItemStack(1);
        ItemMeta grayMeta = gray.getItemMeta();
        grayMeta.setDisplayName(ChatColor.GREEN + "Gray");
        gray.setItemMeta(grayMeta);

        pane.addItem(new GuiItem(gray, event -> {
            horse.setColor(Horse.Color.GRAY);

            event.setCancelled(true);
        }));

        //white
        ItemStack white = new Wool(DyeColor.WHITE).toItemStack(1);
        ItemMeta whiteMeta = white.getItemMeta();
        whiteMeta.setDisplayName(ChatColor.GREEN + "White");
        white.setItemMeta(whiteMeta);

        pane.addItem(new GuiItem(white, event -> {
            horse.setColor(Horse.Color.WHITE);

            event.setCancelled(true);
        }));

        addPane(pane);
    }
}
