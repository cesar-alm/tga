package fr.badcookie20.tga.utils;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class Logger {

    public static void logInfo(String msg) {
        Bukkit.getLogger().log(Level.INFO, msg);
    }

    public static void logWarning(String msg) {
        Bukkit.getLogger().log(Level.WARNING, msg);
    }

    public static void logError(String msg) {
        Bukkit.getLogger().log(Level.SEVERE, msg);
    }

}
