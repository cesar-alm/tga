package fr.badcookie20.tga.utils;

import fr.badcookie20.tga.Plugin;
import fr.badcookie20.tga.cards.Card;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.AsyncCatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

    public class BukkitUtils {

        private static final int LORE_LENGHT = 30;

        public static void registerListeners(Listener... listeners) {
            PluginManager pm = Bukkit.getServer().getPluginManager();

            for(Listener l : listeners) {
                pm.registerEvents(l, Plugin.getInstance());
            }
        }

        public static void disableAsyncCatcher() {
            try {
                if (AsyncCatcher.enabled) {
                    AsyncCatcher.enabled = false;
                }
            }catch(NoClassDefFoundError e) {
                Logger.logWarning("The AsyncCatcher class couldn't be found! Bugs may happen");
            }
        }

        public static void enableAsyncCatcher() {
            try {
                if (!AsyncCatcher.enabled) {
                    AsyncCatcher.enabled = true;
                }
            }catch(NoClassDefFoundError e) {
                Logger.logWarning("The AsyncCatcher class couldn't be found! Bugs may happen");
            }
        }

        public static boolean isCancellable(Event event) {
            List<Class<?>> interfaces = Arrays.asList(event.getClass().getInterfaces());
            return interfaces.contains(Cancellable.class);
        }

        public static Inventory renameInventory(Inventory inv, String newName) {
            int size = inv.getSize();

            List<ItemStack> fullContents = new ArrayList<>();

            for(int i = 0; i < size; i++) {
                ItemStack item = inv.getItem(i);
                if(item == null || item.getType() == Material.AIR) {
                    fullContents.add(new ItemStack(Material.AIR));
                }else{
                    fullContents.add(item);
                }
            }

            Inventory newInventory = Bukkit.createInventory(null, size, newName);
            newInventory.setContents(fullContents.toArray(new ItemStack[fullContents.size()]));

            return newInventory;
        }

        public static ItemStack createItemStack(Material material, String name, List<String> lores) {
            ItemStack item = new ItemStack(material);
            ItemMeta itemMeta = item.getItemMeta();
            if(name != null) itemMeta.setDisplayName(name);
            if(lores != null && !lores.isEmpty()) itemMeta.setLore(lores);
            item.setItemMeta(itemMeta);
            return item;
        }

        public static String placeColor(String s) {
            if(!s.contains("&")) return s;

            return s.replace("&", "§");
        }

        public static Inventory createInventory(List<? extends Card> cards, int initialSize, String name) {
            // TODO WHY ?
            int size = initialSize;
            List<Card> alreadyCalled = new ArrayList<>();

            for(Card c : cards) {
                if(!alreadyCalled.contains(c)) {
                    size++;
                    alreadyCalled.add(c);
                }
            }

            return createInventory(size, name);
        }

        public static Inventory createInventory(int size, String name) {
            int i = size%9;
            int j = size/9;
            int neededSize;

            if(i == 0) {
                neededSize = j;
            }else{
                neededSize = j+1;
            }

            return Bukkit.createInventory(null, neededSize*9, ChatColor.BLACK + name);
        }

        public static List<String> cut(String lore) {
            List<String> cutLore = new ArrayList<>();
            int i = 0;
            String tempString = "";

            for(String s : lore.split(" ")) {
                if(i == 0 && !cutLore.isEmpty()) {
                    String color = ChatColor.getLastColors(cutLore.get(cutLore.size() - 1));
                    tempString+=color;
                }
                i+=s.length();
                tempString += (" " + s);
                if(i >= LORE_LENGHT) {
                    cutLore.add(tempString);
                    tempString = "";
                    i = 0;
                }
            }

            if(!tempString.isEmpty()) cutLore.add(tempString);

            return cutLore;
        }

        public static String removeFirstColor(String s) {
            return removeColor(s, 0);
        }

        public static String removeColor(String s, int start) {
            return new StringBuilder(s).delete(start, start+2).toString();
        }

        public static boolean areSimilar(ItemStack item1, ItemStack item2) {
            if(item1 == null || item2 == null) return false;
            if(item1.equals(item2)) return true;

            if(item1.getType() != item2.getType()) return false;
            if(item1.getAmount() != item2.getAmount()) return false;
            return !(item1.hasItemMeta() && !item2.hasItemMeta());
        }

        public static void sendImpossibleIfNotPlayer(CommandSender sender) {
            sender.sendMessage(ChatColor.RED + "Vous ne pouvez pas exécuter cette commande si vous n'êtes pas un joueur");
        }

        /**
         * Transforms a <code>true</code> in a "Oui", a <code>false</code> in a "Non". If the object isn't one of those,
         * nothing happens
         * @param o the object to be transformed
         * @return what is mentioned earlier
         */
        public static Object booleanFilterer(Object o) {
            if(o.equals(true)) {
                return "Oui";
            }

            if(o.equals(false)) {
                return "Non";
            }

            return o;
        }
    }
