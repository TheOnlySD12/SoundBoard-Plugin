package me.theonlysd12.soundboard;

import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public final class SoundBoard extends JavaPlugin implements PluginMessageListener {

    @Override
    public void onEnable() {
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "soundboard:play_sound", this);
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals("soundboard:play_sound")) {
            return;
        }

        PluginMessageByteBuffer buffer = new PluginMessageByteBuffer(message);
        String sound = buffer.readString();

        player.getWorld().playSound(player.getLocation(), sound, SoundCategory.RECORDS, 1f, 1f);
    }
}
