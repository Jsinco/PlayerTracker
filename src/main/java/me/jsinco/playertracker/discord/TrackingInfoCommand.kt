package me.jsinco.playertracker.discord

import me.jsinco.oneannouncer.api.CommandOption
import me.jsinco.oneannouncer.api.DiscordCommand
import me.jsinco.playertracker.FileManager
import me.jsinco.playertracker.PlayerTracker
import me.jsinco.playertracker.obj.TrackingPlayer
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.Plugin
import java.util.*

class TrackingInfoCommand : DiscordCommand {

    override fun name(): String {
        return "tracker"
    }

    override fun description(): String {
        return "Get tracking info for a player"
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        val playerinfo = event.getOption("player")!!.asString

        // Minecraft Java edition usernames cannot be longer than 16 characters and UUIDs must be 36 characters
        val player: OfflinePlayer = if (playerinfo.length > 16) {
            Bukkit.getOfflinePlayer(UUID.fromString(playerinfo))
        } else {
            Bukkit.getOfflinePlayer(playerinfo)
        }

        val trackingPlayer = TrackingPlayer(player)
        if (!trackingPlayer.beingTracked) {
            event.reply("I'm not watching ${player.name}").queue()
            return
        }

        val message = """
            **PlayerTracker** Info for ${player.name}:
            Watching for: ${(System.currentTimeMillis() - trackingPlayer.trackedSince!!) / 60000}mins
            Is online: ${player.isOnline}
            Commands: ${trackingPlayer.getMostUsedCommands()}
            Messages sent in chat:
        """.trimIndent()

        event.reply(message).queue()
    }

    override fun options(): List<CommandOption> {
        val option = CommandOption(OptionType.STRING,"player", "The player display name or UUID", true)
        return listOf(option)
    }

    override fun permission(): Permission {
        return Permission.MANAGE_SERVER
    }

    override fun plugin(): Plugin? {
        return PlayerTracker.plugin
    }
}