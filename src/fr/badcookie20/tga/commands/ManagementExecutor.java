package fr.badcookie20.tga.commands;

import fr.badcookie20.tga.inventories.manager.InventoriesManager;
import fr.badcookie20.tga.inventories.manager.InventoryType;
import fr.badcookie20.tga.player.TGAPlayer;
import fr.badcookie20.tga.utils.BukkitUtils;
import net.minecraft.server.v1_8_R3.CancelledPacketHandleException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        try {
            InventoriesManager.handleAsync(p, InventoryType.MANAGEMENT);
        }catch(CancelledPacketHandleException e) {
            e.printStackTrace();
        }

        return true;
    }
}
