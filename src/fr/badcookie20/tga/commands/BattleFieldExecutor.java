package fr.badcookie20.tga.commands;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.player.BattleField;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BattleFieldExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            BukkitUtils.sendImpossibleIfNotPlayer(sender);
            return false;
        }

        Player p = (Player) sender;
        TGAPlayer tgaPlayer = TGAPlayer.getPlayer(p);

        if(!tgaPlayer.isInBattle()) {
            tgaPlayer.sendImpossible("Vous n'êtes pas en train de combattre !");
            return false;
        }

        BattleField.Location.BATTLEFIELD.update(tgaPlayer);
        InventoriesManager.handleAsync(tgaPlayer, InventoryType.BATTLEFIELD);

        return true;
    }
}
