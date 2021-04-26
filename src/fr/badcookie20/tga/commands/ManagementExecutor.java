package fr.badcookie20.tga.commands;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import net.minecraft.server.v1_8_R3.CancelledPacketHandleException;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ManagementExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            BukkitUtils.sendImpossibleIfNotPlayer(sender);
            return false;
        }

        TGAPlayer p = TGAPlayer.getPlayer((Player) sender);

        if(p.isInBattle()) {
            p.sendImpossible("Vous ne pouvez pas ouvrir l'interface de gestion si vous êtes en cours de duel");
            return false;
        }

        test(p.getBukkitPlayer());

        try {
            InventoriesManager.handleAsync(p, InventoryType.MANAGEMENT);
        }catch(CancelledPacketHandleException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void test(Player p) {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1);
        // ItemMeta itemMeta = item.getItemMeta();
        // itemMeta.setDisplayName("Coucou");
        // item.setItemMeta(itemMeta);

        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound compound = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
        compound.set("hello", new NBTTagString("hello"));
        nmsStack.setTag(compound);

        item = CraftItemStack.asBukkitCopy(nmsStack);
        p.getInventory().addItem(item);

        p.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
    }
}
