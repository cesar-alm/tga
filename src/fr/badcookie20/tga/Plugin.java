package fr.badcookie20.tga;

import fr.badcookie20.tga.cards.creatures.CreaturesList;
import fr.badcookie20.tga.cards.enchantment.EnchantmentList;
import fr.badcookie20.tga.commands.*;
import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.listeners.ChestOpenListener;
import fr.badcookie20.tga.listeners.DefaultListener;
import fr.badcookie20.tga.listeners.InventoryListener;
import fr.badcookie20.tga.listeners.JoinListener;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import fr.badcookie20.tga.utils.NextUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class of the plugin
 */
public class Plugin extends JavaPlugin {

    private static Plugin instance;
    private int idCount;

    /**
     * Deactivation of the plugin. We make sure that the asynchronous threads are off (default in bukkit) + that the TGAPlayer instances are deleted
     */
    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        BukkitUtils.enableAsyncCatcher();
        TGAPlayer.disconnectAllPlayers();
    }

    /**
     * Activation of the plugin. We load all the classes we need, we register the listeners, we activate asynchronous threads, we instantiate the commands.
     * We also initialize the inventories manager, create the TGAPlayer instance and create the counter of played battles
     */
    @Override
    public void onEnable() {
        instance = this;
        
        try {
            this.getClassLoader().loadClass(CreaturesList.class.getName());
            this.getClassLoader().loadClass(EnchantmentList.class.getName());
            this.getClassLoader().loadClass(NextUtils.class.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        saveConfig();

        BukkitUtils.registerListeners(new JoinListener(), new InventoryListener(), new ChestOpenListener(), new DefaultListener());
        BukkitUtils.disableAsyncCatcher();

        this.getCommand("test").setExecutor(new TestExecutor());
        this.getCommand("login").setExecutor(new LoginExecutor());
        this.getCommand("management").setExecutor(new ManagementExecutor());
        this.getCommand("battlefield").setExecutor(new BattleFieldExecutor());
        this.getCommand("admin").setExecutor(new AdminExecutor());
        InventoriesManager.init();

        idCount = -1;

        for(Player p : Bukkit.getOnlinePlayers()) {
            new TGAPlayer(p);
        }
    }

    public static Plugin getInstance() {
        return instance;
    }

    public int newBattleField() {
        this.idCount++;
        return idCount;
    }
}
