package me.RainStxrm.portalGun.events;

import me.RainStxrm.portalGun.PortalGun;
import me.RainStxrm.portalGun.customevents.PlayerEnterPortalEvent;
import me.RainStxrm.portalGun.entities.Portal;
import me.RainStxrm.portalGun.models.PortalType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;

public class PortalGunEvents implements Listener {

    Portal bluePortal;
    Portal orangePortal;
    boolean bluePortalSpawned = false;

    @EventHandler
    public void onDiamondHoeRightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null) {
            return;
        }
        if (!(item.getType().equals(Material.IRON_HOE))) {
            return;
        }
        if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR))) {
            return;
        }
        player.sendMessage("Iron Hoe right clicked");

        Block portalBlock = player.getTargetBlockExact(20, FluidCollisionMode.SOURCE_ONLY);
        if (portalBlock == null) {
            player.sendMessage("Portal Block not found");
            return;
        }

        Location portalLoc = portalBlock.getLocation();
        player.sendMessage("Portal Placed at " + portalLoc);
        portalLoc.getWorld().spawnParticle(Particle.CRIT, portalLoc, 20);

        if (bluePortalSpawned) {
            if (orangePortal == null) {
                orangePortal = new Portal(portalLoc, PortalType.ORANGE);
                orangePortal.start();
            } else {
                orangePortal.move(portalLoc);
            }
            bluePortalSpawned = false;
        } else {
            if (bluePortal == null) {
                bluePortal = new Portal(portalLoc, PortalType.BLUE);
                bluePortal.start();
            } else {
                bluePortal.move(portalLoc);
            }
            bluePortalSpawned = true;
        }
    }

    ArrayList<Player> portalCooldowns = new ArrayList<>(); //To stop the player from constantly entering a portal

    @EventHandler
    public void onPlayerEnterPortal(PlayerEnterPortalEvent e) {
        if (portalCooldowns.contains(e.getPlayer())) {
            return;
        }

        Location location = (e.getPortal().getType().equals(PortalType.BLUE) ? orangePortal.getLocation() : bluePortal.getLocation());
        e.getPlayer().teleport(location.clone().add(0,1,0));
        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        portalCooldowns.add(e.getPlayer());
        runPortalCooldown(e.getPlayer());
    }

    /*
    TODO: Improvements
         - Track speed and momentum
         - Directional portals, Walls and floors
         - Better aesthetics of portals
         - End reliance on armour stand entities
     */

    private void runPortalCooldown(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                portalCooldowns.remove(player);
            }
        }.runTaskLater(PortalGun.getPlugin(PortalGun.class), 20);
    }
}
