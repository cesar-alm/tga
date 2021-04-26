package fr.badcookie20.tga.commands;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            BukkitUtils.sendImpossibleIfNotPlayer(sender);
            return false;
        }

        TGAPlayer p = TGAPlayer.getPlayer((Player) sender);

        if(!p.checkNew()) {
            p.sendImpossible(ChatColor.RED + "Vous êtes déjà enregistré dans le jeu ! Amusez-vous bien !");
            return false;
        }

        p.getBukkitPlayer().sendMessage(ChatColor.AQUA + "Choisissez votre affinité");
        InventoriesManager.handleAsync(p, InventoryType.AFFINITIES);

        return true;
    }
}
