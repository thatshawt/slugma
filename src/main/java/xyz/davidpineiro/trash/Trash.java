package xyz.davidpineiro.trash;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Trash extends JavaPlugin implements Listener {

    TransportChicken transportChicken;

    private ProtocolManager protocolManager;

    private Map<String, String> songSoundMap = new HashMap<>();
    private Map<Player, String> playerLastSong = new HashMap<>();

    @Override
    public void onEnable() {

        PluginManager manager = Bukkit.getPluginManager();

        transportChicken = new TransportChicken(this);

        manager.registerEvents(transportChicken, this);
        manager.registerEvents(this, this);

        this.getCommand("tc").setExecutor(transportChicken);

        protocolManager = ProtocolLibrary.getProtocolManager();

        songSoundMap.put("crackbaby1", "suspicious:sussy.fort_balls1");
        songSoundMap.put("interstellar", "suspicious:sussy.interstellar");
        songSoundMap.put("kendrick", "suspicious:sussy.kendrick_adhd");
        songSoundMap.put("bjork", "suspicious:sussy.bjork_sus1");
        songSoundMap.put("crackbaby2", "suspicious:sussy.fortnitesusballsabstract");
        songSoundMap.put("howl", "suspicious:sussy.howls_castle");
        songSoundMap.put("california", "suspicious:sussy.hotelcalifornia");
        songSoundMap.put("rocket man", "suspicious:sussy.rocketman");
    }

    @Override
    public void onDisable() {
        try {
            transportChicken.onDisable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onsus(AsyncPlayerChatEvent e){
        if(e.getMessage().equalsIgnoreCase("sussy fortnite balls")){
            Bukkit.getScheduler().runTask(this, () -> {
                e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(), EntityType.LIGHTNING);
            });
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
