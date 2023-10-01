package me.jsinco.playertracker

import org.bukkit.plugin.java.JavaPlugin

class PlayerTracker : JavaPlugin() {
    override fun onEnable() {
        plugin = this
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }
        FileManager.loadDefaultConfig(false)

        val fileManager = FileManager("cache.yml")
        fileManager.generateFile()

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    companion object {
        lateinit var plugin: PlayerTracker

        @JvmStatic
        fun getPlugin(): PlayerTracker {
            return plugin
        }
    }
}