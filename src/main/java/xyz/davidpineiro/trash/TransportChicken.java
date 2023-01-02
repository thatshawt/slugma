package xyz.davidpineiro.trash;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class TransportChicken implements CommandExecutor, Listener {

//    private enum TCState

    private static class TCState {
//        public final
    }

    private final Trash trash;
    private final Map<Player, TCState> playerStates = new HashMap<>();

    public TransportChicken(Trash trash) {
        this.trash = trash;
    }

    private static final ItemStack ITEM_AIR = new ItemStack(Material.AIR);
    private static final Vector VECTOR_ZERO = new Vector(0,0,0);

    private static Runnable getEndTeleportRunnable(Player to, ItemStack handItem){
        return () -> {
            try {
                final World world = to.getWorld();
                Item droppedItem = world.dropItem(to.getLocation().add(0, 1, 0), handItem);
                droppedItem.setVelocity(VECTOR_ZERO);

//                final Chicken chicken = (Chicken)world.spawnEntity(to.getLocation(), EntityType.CHICKEN);
//                chicken.setCollidable(false);
//                chicken.setAI(false);
//                chicken.setAware(false);
//                chicken.setInvulnerable(true);
//                chicken.setGravity(false);

                to.playSound(to.getLocation(),
                        "suspicious:sussy.transportchicken.receiver_receive", 1, 1);
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
    private void startTransport(Player player, Location destination, long tickSendDelay){
        try {
            ItemStack handItem = player.getInventory().getItemInMainHand().clone();
//            if(handItem.getType() == Material.AIR)return;
            player.getInventory().setItemInMainHand(ITEM_AIR);
            player.playSound(player.getLocation(),
                    "suspicious:sussy.transportchicken.user_activate", 1, 1);

            Bukkit.getScheduler().runTaskLater(trash,
                    getEndTeleportRunnable(destination, handItem),
                    Math.max(1,tickSendDelay));//make sure its always at least 1

        }catch(NullPointerException | IllegalArgumentException exception){
            System.out.println("catched little ratty");
        }
    }

    private void startTransport(Player player, Player to, long tickSendDelay){
        try {
            ItemStack handItem = player.getInventory().getItemInMainHand().clone();
//            if(handItem.getType() == Material.AIR)return;
            player.getInventory().setItemInMainHand(ITEM_AIR);
            player.playSound(player.getLocation(),
                    "suspicious:sussy.transportchicken.user_activate", 1, 1);

            Bukkit.getScheduler().runTaskLater(trash,
                    getEndTeleportRunnable(to, handItem),
                    Math.max(1,tickSendDelay));//make sure its always at least 1

        }catch(NullPointerException | IllegalArgumentException exception){
            System.out.println("catched little ratty");
        }
    }

//    @EventHandler
//    public void handleChicken(){
//        // idk yet bruh
//    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))return false;
        if(!label.equalsIgnoreCase("tc"))return false;

        final float distanceMul1 = 0.10f;
        final float distanceMul2 = 0.15f;

        Player player = (Player)sender;
        if(args.length == 0){
            Location destination = player.getTargetBlockExact(100).getLocation().add(0,1,0);

            startTransport(player,
                    destination,
                    (long)(player.getLocation().distance(destination) * distanceMul1) * 20);

            player.playSound(player.getLocation(), "suspicious:sussy.transportchicken.user_activate", 1, 1);
        }else if(args.length == 1){
            try{
                Player sendToPlayer = Bukkit.getPlayer(args[0]);
                Location destination = sendToPlayer.getLocation();

//                startTransport(player, destination);
                startTransport(player,
                        sendToPlayer,
                        (long)(player.getLocation().distance(destination) * distanceMul2) * 20);
//                player.getWorld().playSound(
//                        player.getLocation(),"suspicious:sussy.transportchicken.user_activate", 1,1
//                );

//                sendToPlayer.getWorld().playSound(
//                        player.getLocation(),"suspicious:sussy.transportchicken.receiver_receive", 1,1
//                );

            }catch(Exception exception){
                exception.printStackTrace();
            }
        }

        return true;
    }
}
