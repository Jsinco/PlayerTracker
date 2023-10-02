package me.jsinco.playertracker.obj

import me.jsinco.playertracker.FileManager
import me.jsinco.playertracker.PlayerTracker
import me.jsinco.playertracker.util.Util
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import java.util.*

class TrackingPlayer (
    val player: OfflinePlayer
) {
    private val plugin: PlayerTracker = PlayerTracker.plugin
    private val onlinePlayer: Player? = if (player.isOnline) player.player else null
    private val cacheFile = FileManager("cache.yml")
    private val recorderFile = FileManager("recorder.yml")

    private val recorder: YamlConfiguration = recorderFile.getFileYaml()
    private val cache: YamlConfiguration = cacheFile.getFileYaml()

    val beingTracked: Boolean = if (cache.contains("players.${player.uniqueId}")) cache.getBoolean("players.${player.uniqueId}.beingTracked") else false
    val trackedSince: Long? = if (beingTracked) cache.getLong("players.${player.uniqueId}.trackedSince") else null


    fun startTracking() {
        cache.set("players.${player.uniqueId}.beingTracked", true)
        cache.set("players.${player.uniqueId}.trackedSince", System.currentTimeMillis())
        onlinePlayer?.setMetadata("beingtracked", FixedMetadataValue(plugin, true))
        save()
    }

    fun resumeTracking() {
        player.player?.setMetadata("beingtracked", FixedMetadataValue(PlayerTracker.plugin, true))
    }

    fun stopTracking() {
        cache.set("players.${player.uniqueId}.beingTracked", false)
        cache.set("players.${player.uniqueId}.trackedSince", null)
        recorder.set("session.${player.uniqueId}", null)
        onlinePlayer?.removeMetadata("beingtracked", plugin)
        save()
    }

    fun clearData() {
        stopTracking()
        cache.set("players.${player.uniqueId}", null)
        save()
    }

    private fun save() {
        cacheFile.saveFileYaml(cache)
        recorderFile.saveFileYaml(recorder)
    }


    fun serializeCommand(command: String) {
        cache.set("players.${player.uniqueId}.commands.$command", cache.getInt("players.${player.uniqueId}.commands.$command") + 1)
        save()
        Util.log("Serialized command: $command for ${player.name}")
    }

    fun getMostUsedCommands(): Map<String, Int> {
        val commands: List<String>? = cache.getConfigurationSection("players.${player.uniqueId}.commands")?.getKeys(false)?.toList()

        val amountUsed: MutableList<Int> = arrayListOf()
        for (command in commands!!) {
            amountUsed.add(cache.getInt("players.${player.uniqueId}.commands.$command"))
        }

        val sortedCommands: Map<String, Int> = commands.zip(amountUsed).toMap().toList().sortedByDescending { (_, value) -> value }.toMap()

        return sortedCommands
    }
}