package xyz.davidpineiro.trash;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface TroundTypeInterface {

    String getSongID();

    default void playForAll(float volume, float pitch){
        Bukkit.getOnlinePlayers().forEach(
                (player) -> playForPlayer(player, volume, pitch)
        );
    }

    default void playForPlayer(Player player, float volume, float pitch){
        player.playSound(player.getLocation(), getSongID(), volume, pitch);
    }

}
