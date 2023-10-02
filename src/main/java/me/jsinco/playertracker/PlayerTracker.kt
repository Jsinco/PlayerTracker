package me.jsinco.playertracker

import me.jsinco.oneannouncer.api.DiscordCommandManager
import me.jsinco.playertracker.discord.TrackingInfoCommand
import me.jsinco.playertracker.listeners.EventListeners
import org.bukkit.plugin.java.JavaPlugin

class PlayerTracker : JavaPlugin() {
    override fun onEnable() {
        plugin = this
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }
        FileManager.loadDefaultConfig(false)


        FileManager("cache.yml").generateFile()
        FileManager("recorder.yml").generateFile()
        FileManager("config.yml").generateFile()

        DiscordCommandManager.registerCommand(TrackingInfoCommand())
        server.pluginManager.registerEvents(EventListeners(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }


    companion object {
        lateinit var plugin: PlayerTracker
    }
}