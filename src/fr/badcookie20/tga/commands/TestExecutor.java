package fr.badcookie20.tga.commands;

import fr.badcookie20.tga.player.TGAPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Player p = (Player) sender;
        TGAPlayer tgaPlayer = TGAPlayer.getPlayer(p);

        // new Thread(() -> InventoriesManager.getInstance().handle(tgaPlayer, InventoryType.PLAYERS_LIST)).start();

        return false;
    }
}
