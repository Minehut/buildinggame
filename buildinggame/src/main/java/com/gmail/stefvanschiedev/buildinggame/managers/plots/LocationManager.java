package com.gmail.stefvanschiedev.buildinggame.managers.plots;

import com.gmail.stefvanschiedev.buildinggame.Main;
import com.gmail.stefvanschiedev.buildinggame.managers.arenas.ArenaManager;
import com.gmail.stefvanschiedev.buildinggame.managers.files.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * This class handles the locations
 *
 * @since 2.1.0
 */
public final class LocationManager {

    /**
     * Creates a new LocationManager. This shouldn't be called to keep this class a singleton.
     */
    private LocationManager() {
    }

    /**
     * An instance of this class for the singleton principle
     */
    private static final LocationManager INSTANCE = new LocationManager();

    /**
     * Returns the instance of this singleton class
     *
     * @return an instance of this class
     * @since 2.1.0
     */
    @NotNull
    @Contract(pure = true)
    public static LocationManager getInstance() {
        return INSTANCE;
    }

    /**
     * Loads/Reloads all locations for all plots
     *
     * @since 2.1.0
     */
    @SuppressWarnings("MethodMayBeStatic")
    public void setup() {
        YamlConfiguration arenas = SettingsManager.getInstance().getArenas();

        ArenaManager.getInstance().getArenas().forEach(arena ->
                arena.getPlots().forEach(plot -> {
                    try {
                        plot.setLocation(new Location(Bukkit.getWorld(
                                arenas.getString(arena.getName() + '.' + plot.getID() + ".world")),
                                arenas.getInt(arena.getName() + '.' + plot.getID() + ".x"),
                                arenas.getInt(arena.getName() + '.' + plot.getID() + ".y"),
                                arenas.getInt(arena.getName() + '.' + plot.getID() + ".z"),
                                (float) arenas.getDouble(arena.getName() + '.' + plot.getID() + ".yaw", 0),
                                (float) arenas.getDouble(arena.getName() + '.' + plot.getID() + ".pitch", 0)));

                        if (SettingsManager.getInstance().getConfig().getBoolean("debug")) {
                            Logger logger = Main.getInstance().getLogger();

                            if (plot.getLocation().getWorld() == null)
                                logger.warning("Unable to load world for plot spawn");

                            logger.info("Loaded spawn for plot " + plot.getID() + " in arena " + arena.getName());
                        }
                    } catch (NullPointerException | IllegalArgumentException npe) {
                        plot.setLocation(null);
                    }
                })
        );
    }

}