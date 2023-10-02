package me.jsinco.playertracker.util

import me.jsinco.playertracker.PlayerTracker
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit

object Util {
    val plugin: PlayerTracker = PlayerTracker.plugin

    const val WITH_DELIMITER = "((?<=%1\$s)|(?=%1\$s))"

    /**
     * @param text The string of text to apply color/effects to
     * @return Returns a string of text with color/effects applied
     */
    fun colorcode(text: String): String {
        val texts = text.split(String.format(WITH_DELIMITER, "&").toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val finalText = StringBuilder()
        var i = 0
        while (i < texts.size) {
            if (texts[i].equals("&", ignoreCase = true)) {
                //get the next string
                i++
                if (texts[i][0] == '#') {
                    finalText.append(ChatColor.of(texts[i].substring(0, 7)).toString() + texts[i].substring(7))
                } else {
                    finalText.append(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&" + texts[i]))
                }
            } else {
                finalText.append(texts[i])
            }
            i++
        }
        return finalText.toString()
    }

    fun log(message: String) {
        if (plugin.config.getBoolean("verbose-logging")) {
            Bukkit.getConsoleSender().sendMessage(colorcode("${plugin.config.getString("prefix")}$message"))
        }
    }
}