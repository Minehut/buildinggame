package com.gmail.stefvanschiedev.buildinggame.utils.guis.moboptions;

import com.github.stefvanschie.inventoryframework.GuiItem;
import com.gmail.stefvanschiedev.buildinggame.utils.plot.Plot;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

/**
 * A gui for changing whether an entity is adult or baby
 *
 * @since 5.3.0
 */
public class BabyMenu extends RemoveMenu {

    /**
     * {@inheritDoc}
     */
    public BabyMenu(Plot plot, Entity entity) {
        super(plot, entity);

        var baby = new ItemStack(Material.EGG);
        var babyMeta = baby.getItemMeta();
        babyMeta.setDisplayName(ChatColor.GREEN + "Change to baby/adult");
        baby.setItemMeta(babyMeta);

        pane.insertItem(new GuiItem(baby, event -> {
            if (entity instanceof Ageable) {
                var ageable = (Ageable) entity;
                if (ageable.isAdult())
                    ageable.setBaby();
                else
                    ageable.setAdult();
            } else if (entity instanceof Zombie) {
                var zombie = (Zombie) entity;
                zombie.setBaby(!zombie.isBaby());
            }

            event.setCancelled(true);
        }), 0);
    }
}
