package xyz.davidpineiro.trash;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;

public class TransportChicken implements CommandExecutor, Listener {

    public static final String DATA_FILE_NAME = "data.yml";

    private final Trash trash;
    public final YamlConfiguration dataFileConfig;
    final File dataFile;

    public void onDisable() throws IOException {
        dataFileConfig.save(dataFile);
    }

    private Location getWarpLocation(Player player, String warpName){
        final Location warpLocation = dataFileConfig.getLocation(
                "transportchicken." + player.getName() + "." + warpName
                );
        return warpLocation;
    }

    private void setWarpLocation(Player player, String warpName){
        dataFileConfig.set(
                "transportchicken." + player.getName() + "." + warpName,
                player.getLocation()
        );
    }

    public TransportChicken(Trash trash) {
        this.trash = trash;

        dataFile = new File(trash.getDataFolder(), DATA_FILE_NAME);

        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            trash.saveResource(DATA_FILE_NAME, false);
        }
        dataFileConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    private static final ItemStack ITEM_AIR = new ItemStack(Material.AIR);
    private static final Vector VECTOR_ZERO = new Vector(0,0,0);

    private static Runnable getEndTeleportRunnable(Player to, ItemStack handItem){
        return () -> {
            try {
                final World world = to.getWorld();
                Item droppedItem = world.dropItem(to.getLocation().add(0, 1, 0), handItem);
                droppedItem.setVelocity(VECTOR_ZERO);

//                to.playSound(to.getLocation(),
//                        "suspicious:sussy.transportchicken.receiver_receive", 1, 1);
                Trounds.Sfx.tc_receive.playForPlayer(to, 1, 1);

            }catch(Exception exception){
                exception.printStackTrace();
            }
        };
    }

    private static Runnable getEndTeleportRunnable(Location to, ItemStack handItem){
        return () -> {
            try {
                Item droppedItem = to.getWorld().dropItem(to, handItem);
                droppedItem.setVelocity(VECTOR_ZERO);
            }catch(Exception exception){
                exception.printStackTrace();
            }
        };
    }

    /**
     * the item that the player is holding gets transported to destination
     * @param player
     * @param destination
     */
    private void startTransport(Player player, Location destination){
        try {
            ItemStack handItem = player.getInventory().getItemInMainHand().clone();
            if(handItem.getType() == Material.AIR)return;
            player.getInventory().setItemInMainHand(ITEM_AIR);

            Trounds.Sfx.tc_activate.playForPlayer(player, 1, 1);

            Bukkit.getScheduler().runTaskLater(trash,
                    getEndTeleportRunnable(destination, handItem),
                    Math.max(1, (long)(player.getLocation().distance(destination) * 0.1f) * 20)
            );

        }catch(NullPointerException | IllegalArgumentException exception){
            System.out.println("catched little ratty");
        }
    }

    private void startTransport(Player player, Player to){
        try {
            ItemStack handItem = player.getInventory().getItemInMainHand().clone();
            if(handItem.getType() == Material.AIR)return;

            player.getInventory().setItemInMainHand(ITEM_AIR);

            Trounds.Sfx.tc_activate.playForPlayer(player, 1, 1);

            Bukkit.getScheduler().runTaskLater(trash,
                    getEndTeleportRunnable(to, handItem),
                    Math.max(1, (long)(player.getLocation().distance(to.getLocation()) * 0.1f) * 20)
            ); //make sure its always at least 1

        }catch(NullPointerException | IllegalArgumentException exception){
            System.out.println("catched little ratty");
        }
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))return false;
        if(!label.equalsIgnoreCase("tc"))return false;

        Player player = (Player)sender;
        if(args.length == 0){ // "/tc"
            Location destination = player.getTargetBlockExact(100).getLocation().add(0,1,0);

            startTransport(player, destination);

            return true;
        }else if(args.length == 1){// "/tc player_name"
            try{
                Player sendToPlayer = Bukkit.getPlayer(args[0]);

                startTransport(player, sendToPlayer);

                return true;
            }catch(Exception exception){
                exception.printStackTrace();
                return false;
            }
        }else if(args.length == 2){// "/tc warp warp_name"
            if(!args[0].equalsIgnoreCase("warp")) return false;
            final String warpName = args[1];
            final Location warpLocation = getWarpLocation(player, warpName);

            if(warpLocation == null)return false;

            startTransport(player, warpLocation);

            return true;
        }else if(args.length == 3){// "/tc warp set warp_name"
            if(!args[0].equalsIgnoreCase("warp") &&
            !args[1].equalsIgnoreCase("set")) return false;

            final String warpName = args[2];
            setWarpLocation(player, warpName);

            player.sendMessage(ChatColor.GREEN +
                    "Set warp location peroply,! zzeezz zaa zaa gii gii goo go ga gaa");

            return true;
        }

        return false;
    }
}
