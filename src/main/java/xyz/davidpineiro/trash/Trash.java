package xyz.davidpineiro.trash;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;

import java.util.HashMap;
import java.util.Map;

public class Trash extends JavaPlugin implements Listener {

    private ProtocolManager protocolManager;

    private Map<String, String> songSoundMap = new HashMap<>();
    private Map<Player, String> playerLastSong = new HashMap<>();

    @Override
    public void onEnable() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(this, this);

        protocolManager = ProtocolLibrary.getProtocolManager();

        songSoundMap.put("crackbaby1", "suspicious:sussy.fort_balls1");
        songSoundMap.put("interstellar", "suspicious:sussy.interstellar");
        songSoundMap.put("kendrick", "suspicious:sussy.kendrick_adhd");
        songSoundMap.put("bjork", "suspicious:sussy.bjork_sus1");
        songSoundMap.put("crackbaby2", "suspicious:sussy.fortnitesusballsabstract");
    }

    @EventHandler
    public void onsus(AsyncPlayerChatEvent e){
        if(e.getMessage().equalsIgnoreCase("sussy fortnite balls")){
            e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(), EntityType.LIGHTNING);
            return;
        }

        final String sound = songSoundMap.get(e.getMessage().toLowerCase().strip());
        if(sound == null)return;

        try {
            Player player = e.getPlayer();

            RayTraceResult rayTraceResult = player.rayTraceBlocks(100);
            Block block = rayTraceResult.getHitBlock();
            if (block.getType() != Material.JUKEBOX) return;

            Bukkit.getOnlinePlayers().forEach((onlinePlayer) -> {
                final String previousSound = playerLastSong.get(player);
                if(previousSound != null) {
                    onlinePlayer.stopSound(SoundCategory.RECORDS);
                }

                onlinePlayer.playSound(block.getLocation(), sound, SoundCategory.RECORDS, 1, 1);
                playerLastSong.put(player, sound);
            });
        }catch(NullPointerException ignore){
            ignore.printStackTrace();
        }
    }
}
