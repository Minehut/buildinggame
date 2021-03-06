package com.gmail.stefvanschiedev.buildinggame.utils.guis;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.Orientable;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.gmail.stefvanschiedev.buildinggame.Main;
import com.gmail.stefvanschiedev.buildinggame.managers.files.SettingsManager;
import com.gmail.stefvanschiedev.buildinggame.managers.messages.MessageManager;
import com.gmail.stefvanschiedev.buildinggame.utils.SubjectVote;
import com.gmail.stefvanschiedev.buildinggame.utils.guis.util.PercentageBar;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A menu for voting on a theme
 *
 * @since 2.1.0
 */
public class SubjectMenu extends Gui {

    /**
     * The subject that is forced to be chosen
     */
    private String forcedTheme;

    /**
     * A map containing all votes for all subjects
     */
    private final Collection<SubjectVote> votes = new HashSet<>();

    /**
     * The moment this theme gui should be accessible
     */
    private final When when;

    /**
     * Whether this menu should open automatically when possible.
     */
    private final boolean openInstantly;

    /**
     * YAML Configuration for the config.yml
     */
    private static final YamlConfiguration CONFIG = SettingsManager.getInstance().getConfig();

    /**
     * YAML Configuration for the messages.yml
     */
    private static final YamlConfiguration MESSAGES = SettingsManager.getInstance().getMessages();

    /**
     * Constructs a new SubjectMenu
     */
    public SubjectMenu() {
        super(Main.getInstance(), CONFIG.getInt("subject-gui.rows"),
                MessageManager.translate(MESSAGES.getString("subject-gui.title")));

        int amountOfSubjects = CONFIG.getInt("subject-gui.subject-amount");

        when = When.fromName(CONFIG.getString("subject-gui.when"));
        openInstantly = CONFIG.getBoolean("subject-gui.open-instantly");

        List<String> subjects = new ArrayList<>();

        if (amountOfSubjects == -1)
            subjects.addAll(CONFIG.getStringList("subjects"));
        else {
            for (int i = 0; i < amountOfSubjects; i++)
                subjects.add(CONFIG.getStringList("subjects").get(ThreadLocalRandom.current()
                        .nextInt(amountOfSubjects)));
        }

        subjects.forEach(s -> votes.add(new SubjectVote(s, 0)));

        //gui
        PaginatedPane paginatedPane = new PaginatedPane(0, 0, 9, getRows() - 1);

        initializePages(paginatedPane, subjects);

        if (paginatedPane.getPages() == 1 && !CONFIG.getBoolean("subject-gui.close-item.enable")) {
            paginatedPane.setHeight(getRows());

            initializePages(paginatedPane, subjects);
        }

        addPane(paginatedPane);

        if (paginatedPane.getPages() != 1) {
            OutlinePane previousPane = new OutlinePane(2, getRows() - 1, 1, 1);
            OutlinePane nextPane = new OutlinePane(6, getRows() - 1, 1, 1);

            //previous page
            ItemStack prevItem = new ItemStack(Material.SUGAR_CANE);
            ItemMeta prevMeta = prevItem.getItemMeta();
            prevMeta
                    .setDisplayName(MessageManager.translate(MESSAGES.getString("subject-gui.previous-page.name")));
            prevMeta.setLore(MessageManager.translate(MESSAGES.getStringList("subject-gui.previous-page.lores")));
            prevItem.setItemMeta(prevMeta);

            previousPane.addItem(new GuiItem(prevItem, event -> {
                paginatedPane.setPage(paginatedPane.getPage() - 1);

                if (paginatedPane.getPage() == 0)
                    previousPane.setVisible(false);

                nextPane.setVisible(true);

                update();

                event.setCancelled(true);
            }));

            previousPane.setVisible(false);

            addPane(previousPane);

            //next page
            ItemStack nextItem = new ItemStack(Material.SUGAR_CANE);
            ItemMeta nextMeta = nextItem.getItemMeta();
            nextMeta.setDisplayName(MessageManager.translate(MESSAGES.getString("subject-gui.next-page.name")));
            nextMeta.setLore(MessageManager.translate(MESSAGES.getStringList("subject-gui.next-page.lores")));
            nextItem.setItemMeta(nextMeta);

            nextPane.addItem(new GuiItem(nextItem, event -> {
                paginatedPane.setPage(paginatedPane.getPage() + 1);

                if (paginatedPane.getPage() == paginatedPane.getPages() - 1)
                    nextPane.setVisible(false);

                previousPane.setVisible(true);

                update();

                event.setCancelled(true);
            }));

            addPane(nextPane);
        }

        if (CONFIG.getBoolean("subject-gui.close-item.enable")) {
            OutlinePane closePane = new OutlinePane(4, getRows() - 1, 1, 1);

            ItemStack closeItem = new ItemStack(Material.BOOK);
            ItemMeta closeMeta = closeItem.getItemMeta();
            closeMeta.setDisplayName(MessageManager.translate(MESSAGES.getString("subject-gui.close-menu.name")));
            closeMeta.setLore(MessageManager.translate(MESSAGES.getStringList("subject-gui.close-menu.lores")));
            closeItem.setItemMeta(closeMeta);

            closePane.addItem(new GuiItem(closeItem, event -> {
                event.getWhoClicked().closeInventory();
                event.setCancelled(true);
            }));

            addPane(closePane);
        }

        //additional items
        CONFIG.getConfigurationSection("subject-gui.additional-items").getKeys(false).forEach(section -> {
            String baseNode = "subject-gui.additional-items." + section;

            int x = CONFIG.getInt(baseNode + ".x") - 1;
            int y = CONFIG.getInt(baseNode + ".y") - 1;

            OutlinePane pane = new OutlinePane(x, y, 1, 1);

            ItemStack item = new ItemStack(Material.matchMaterial(CONFIG.getString(baseNode + ".id")));
            ItemMeta itemMeta = item.getItemMeta();

            String name = MESSAGES.getString(baseNode + ".name");

            if (name == null) {
                String baseName = item.getType().name().substring(1).replace('_', ' ');
                String lowerCaseName = baseName.toLowerCase(Locale.getDefault());
                String prettyName = Character.toUpperCase(item.getType().name().charAt(0)) + lowerCaseName;

                MESSAGES.set(baseNode + ".name", prettyName);
                name = MESSAGES.getString(baseNode + ".name");
            }

            itemMeta.setDisplayName(MessageManager.translate(name));

            List<String> lore = MESSAGES.getStringList(baseNode + ".lore");

            if (lore == null) {
                MESSAGES.set(baseNode + ".name", new ArrayList<String>());
                lore = new ArrayList<>();
            }

            itemMeta.setLore(MessageManager.translate(lore));
            item.setItemMeta(itemMeta);

            pane.addItem(new GuiItem(item, event -> event.setCancelled(true)));

            addPane(pane);
        });
    }

    /**
     * Initializes the pages for this gui
     *
     * @param paginatedPane the paginated pane
     * @param subjects      the subjects
     * @since 5.6.0
     */
    @Contract("null, _ -> fail")
    private void initializePages(@NotNull PaginatedPane paginatedPane, List<String> subjects) {
        paginatedPane.clear();

        for (int page = 0;
             page < Math.ceil((float) subjects.size() / (paginatedPane.getHeight() * paginatedPane.getLength()));
             page++) {
            OutlinePane pane = new OutlinePane(0, 0, paginatedPane.getLength(), paginatedPane.getHeight());

            pane.setOrientation(Orientable.Orientation.valueOf(
                    CONFIG.getString("subject-gui.vote-items.orientation").toUpperCase(Locale.getDefault())
            ));

            for (int index = 0; index < paginatedPane.getLength() * paginatedPane.getHeight(); index++) {
                if (subjects.size() - 1 < index + (page * paginatedPane.getLength() * paginatedPane.getHeight()))
                    break;

                final String subject = ChatColor
                        .stripColor(subjects.get(index + (page * paginatedPane.getLength() * paginatedPane.getHeight())));

                if (getSubjectVote(subject) == null)
                    votes.add(new SubjectVote(subject, 0));

                ItemStack item =
                        new ItemStack(Material.matchMaterial(CONFIG.getString("subject-gui.vote-items.item.id")));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(MessageManager.translate(MESSAGES.getString("subject-gui.subject.name")
                        .replace("%subject%", subject)));
                List<String> lores = new ArrayList<String>();

                MESSAGES.getStringList("subject-gui.subject.lores").forEach(lore ->
                        lores.add(MessageManager.translate(lore
                                .replace("%votes%", getSubjectVote(subject).getVotes() + ""))));

                meta.setLore(lores);
                item.setItemMeta(meta);

                pane.addItem(new GuiItem(item, event -> {
                    addVote((Player) event.getWhoClicked(), subject);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            initializePages(paginatedPane, subjects);

                            update();
                        }
                    }.runTaskLater(Main.getInstance(), 1L);

                    event.setCancelled(true);
                }));

                if (CONFIG.getBoolean("subject-gui.percentage-bars.enable")) {
                    int x;
                    int y;

                    if (pane.getOrientation() == Orientable.Orientation.HORIZONTAL) {
                        x = index % pane.getLength();
                        y = (int) Math.floor((double) index / pane.getLength());
                    } else if (pane.getOrientation() == Orientable.Orientation.VERTICAL) {
                        x = (int) Math.floor((double) index / pane.getHeight());
                        y = index % pane.getHeight();
                    } else {
                        throw new UnsupportedOperationException("Unknown orientation found");
                    }

                    int xOffset = CONFIG.getInt("subject-gui.percentage-bars.offset.x");
                    int yOffset = CONFIG.getInt("subject-gui.percentage-bars.offset.y");

                    int totalVotes = votes.stream().mapToInt(SubjectVote::getVotes).sum();
                    int userVotes = getSubjectVote(subject).getVotes();

                    PercentageBar percentageBar = new PercentageBar(x + xOffset, y + yOffset, 7, 1);
                    percentageBar.setPercentage(totalVotes == 0 ? 0 : (float) userVotes / totalVotes);

                    addPane(percentageBar);
                }
            }

            paginatedPane.addPane(page, pane);
        }
    }

    /**
     * Adds a vote for the specified theme
     *
     * @param player  the player that voted for the theme
     * @param subject the them the player voted for
     * @since 2.1.0
     */
    private void addVote(Player player, String subject) {
        subject = ChatColor.stripColor(subject);

        votes.stream().filter(subjectVote -> subjectVote.getPlayers().contains(player)).forEach(subjectVote -> {
            subjectVote.removePlayer(player);
            subjectVote.setVotes(subjectVote.getVotes() - 1);
        });

        SubjectVote subjectVote = getSubjectVote(subject);

        subjectVote.addPlayer(player);
        subjectVote.setVotes(subjectVote.getVotes() + 1);
    }

    /**
     * Forces a specific theme to be chosen
     *
     * @param theme the theme that's forced to be chosen
     * @since 4.0.4
     */
    public void forceTheme(String theme) {
        forcedTheme = theme;
    }

    /**
     * Returns the theme that received the most votes. If there are multiple themes with the highest amount of votes,
     * one of those will be picked at random.
     *
     * @return the theme that received the highest amount of votes
     */
    @Nullable
    @Contract(pure = true)
    public String getHighestVote() {
        if (forcedTheme != null)
            return forcedTheme;

        int highest = -1;

        for (SubjectVote subjectVote : votes) {
            if (subjectVote.getVotes() > highest)
                highest = subjectVote.getVotes();
        }

        List<String> subjects = new ArrayList<>();
        for (SubjectVote subjectVote : votes) {
            if (subjectVote.getVotes() == highest)
                subjects.add(subjectVote.getSubject());
        }

        return subjects.get(ThreadLocalRandom.current().nextInt(subjects.size()));
    }

    /**
     * Returns the subject vote by the given subject
     *
     * @param subject the subject to look for
     * @return the subject vote corresponding with the subject
     * @since 5.2.0
     */
    @Nullable
    @Contract(pure = true)
    private SubjectVote getSubjectVote(String subject) {
        return votes.stream()
                .filter(subjectVote -> subjectVote.getSubject().equals(subject))
                .findAny()
                .orElse(null);
    }

    /**
     * Gets the time when the menu should be accessible
     *
     * @return when the menu should be accessible
     * @see When
     * @since 6.4.0
     */
    @NotNull
    @Contract(pure = true)
    public When getWhen() {
        return when;
    }

    /**
     * Gets whether this menu should be opened immediately when possible
     *
     * @return true if this menu should open directly, false otherwise
     * @since 6.4.0
     */
    @Contract(pure = true)
    public boolean opensInstantly() {
        return openInstantly;
    }

    /**
     * An enum for the possible timings when the theme gui can be accessed
     *
     * @since 6.4.0
     */
    public enum When {

        /**
         * The subject menu will be accessible while waiting in the lobby
         *
         * @since 6.4.0
         */
        LOBBY,

        /**
         * The subject menu will be accessible right before building
         *
         * @since 6.4.0
         */
        BEFORE_BUILD;

        /**
         * Gets an enum value from this enum by the given name.
         *
         * @param name the name of the enum value
         * @return the enum value
         * @since 6.4.0
         */
        @NotNull
        @Contract(pure = true)
        static When fromName(@NotNull String name) {
            return When.valueOf(name
                    .trim()
                    .toUpperCase(Locale.getDefault())
                    .replace('-', '_')
                    .replace(' ', '_'));
        }
    }
}