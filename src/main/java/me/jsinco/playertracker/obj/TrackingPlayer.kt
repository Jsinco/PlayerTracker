package me.jsinco.playertracker.obj

import me.jsinco.playertracker.PlayerTracker
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

class TrackingPlayer (
    val player: Player

) {
    val plugin: PlayerTracker = PlayerTracker.plugin
    //val trackedSince: Long

    init {
        val data: PersistentDataContainer = player.persistentDataContainer

        //trackedSince = if (data.has(NamespacedKey(plugin, "trackedSince"), PersistentDataType.LONG)) data.get(NamespacedKey(plugin, "trackedSince"), PersistentDataType.LONG)) else System.currentTimeMillis()
    }
}