package com.gmail.stefvanschiedev.buildinggame.managers.arenas;

import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.stefvanschiedev.buildinggame.Main;
import com.gmail.stefvanschiedev.buildinggame.managers.files.SettingsManager;
import com.gmail.stefvanschiedev.buildinggame.utils.arena.Arena;
import com.gmail.stefvanschiedev.buildinggame.utils.plot.Plot;
import org.jetbrains.annotations.Contract;

public final class MaxPlayersManager {

	private MaxPlayersManager() {}
	
	private static final MaxPlayersManager INSTANCE = new MaxPlayersManager();
	
	@Contract(pure = true)
    public static MaxPlayersManager getInstance() {
		return INSTANCE;
	}
	
	@SuppressWarnings("MethodMayBeStatic")
    public void setup() {
		for (Arena arena : ArenaManager.getInstance().getArenas()) {
			YamlConfiguration arenas = SettingsManager.getInstance().getArenas();
			YamlConfiguration config = SettingsManager.getInstance().getConfig();
			
			try {
				int maxPlayers = arenas.getInt(arena.getName() + ".maxplayers");
				
				arena.setMaxPlayers(maxPlayers);
				
				for (Plot plot : arena.getPlots()) {
					if (!config.contains("team-selection.team." + plot.getID()))
						config.set("team-selection.team." + plot.getID() + ".id", "paper");
				}
				
				if (SettingsManager.getInstance().getConfig().getBoolean("debug")) {
					Main.getInstance().getLogger().info("Loaded max players for " + arena.getName());
				}
			} catch (NullPointerException npe) {
				arena.setMaxPlayers(0);
			}
		}
	}
}