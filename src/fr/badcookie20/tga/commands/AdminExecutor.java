package fr.badcookie20.tga.commands;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            BukkitUtils.sendImpossibleIfNotPlayer(sender);
            return false;
        }

        TGAPlayer p = TGAPlayer.getPlayer((Player) sender);

        if(!p.getBukkitPlayer().isOp()) {
            p.sendImpossible("Vous n'êtes pas op, vous ne pouvez pas ouvrir l'interface d'administration !");
            return false;
        }

        InventoriesManager.handleAsync(p, InventoryType.ADMIN);



        return true;
    }
}
