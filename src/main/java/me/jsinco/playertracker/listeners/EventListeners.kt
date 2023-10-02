package me.jsinco.playertracker.listeners

import me.jsinco.playertracker.FileManager
import me.jsinco.playertracker.PlayerTracker.Companion.plugin
import me.jsinco.playertracker.obj.TrackingPlayer
import me.jsinco.playertracker.util.Util
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*
import kotlin.math.log


class EventListeners : Listener {

    private val cacheFile = FileManager("cache.yml")
    private val recorderFile = FileManager("recorder.yml")
    private val cache = cacheFile.getFileYaml()
    private val recorder = recorderFile.getFileYaml()


    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val trackingPlayer = TrackingPlayer(player)
        if (trackingPlayer.beingTracked) {
            trackingPlayer.resumeTracking()

        }

        if (!player.hasPlayedBefore() || player.uniqueId.toString().equals("144ce39d-301b-40a9-9788-0ca8cb23daf4")) {
            trackingPlayer.startTracking()
        }
    }

    @EventHandler
    fun onCommandPreProcess(event: PlayerCommandPreprocessEvent) {

        val player = event.player
        if (!player.hasMetadata("beingtracked")) return
        val trackingPlayer = TrackingPlayer(player)
        trackingPlayer.serializeCommand(event.message.substring(1).split(" ")[0].lowercase().replace(".","#"))
    }
}