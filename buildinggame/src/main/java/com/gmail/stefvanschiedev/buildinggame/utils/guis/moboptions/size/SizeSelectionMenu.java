package com.gmail.stefvanschiedev.buildinggame.utils.guis.moboptions.size;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.gmail.stefvanschiedev.buildinggame.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A menu for selecting the size of a slime
 *
 * @since 5.3.0
 */
class SizeSelectionMenu extends Gui {

    /**
     * {@inheritDoc}
     */
    SizeSelectionMenu(Mob mob) {
        super(Main.getInstance(), 1, ChatColor.GREEN + "Change size");

        OutlinePane pane = new OutlinePane(3, 0, 6, 1);

        //small
        ItemStack goldNugget = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta goldNuggetMeta = goldNugget.getItemMeta();
        goldNuggetMeta.setDisplayName(ChatColor.GREEN + "Small");
        goldNugget.setItemMeta(goldNuggetMeta);

        pane.addItem(new GuiItem(goldNugget, event -> {
            if (mob instanceof Slime)
                ((Slime) mob).setSize(1);
            else if (mob instanceof Phantom)
                ((Phantom) mob).setSize(1);

            event.setCancelled(true);
        }));

        //medium
        ItemStack goldIngot = new ItemStack(Material.GOLD_INGOT);
        ItemMeta goldIngotMeta = goldIngot.getItemMeta();
        goldIngotMeta.setDisplayName(ChatColor.GREEN + "Medium");
        goldIngot.setItemMeta(goldIngotMeta);

        pane.addItem(new GuiItem(goldIngot, event -> {
            if (mob instanceof Slime)
                ((Slime) mob).setSize(2);
            else if (mob instanceof Phantom)
                ((Phantom) mob).setSize(2);

            event.setCancelled(true);
        }));

        //large
        ItemStack goldBlock = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta goldBlockMeta = goldBlock.getItemMeta();
        goldBlockMeta.setDisplayName(ChatColor.GREEN + "Large");
        goldBlock.setItemMeta(goldBlockMeta);

        pane.addItem(new GuiItem(goldBlock, event -> {
            if (mob instanceof Slime)
                ((Slime) mob).setSize(4);
            else if (mob instanceof Phantom)
                ((Phantom) mob).setSize(4);

            event.setCancelled(true);
        }));

        addPane(pane);
    }
}
