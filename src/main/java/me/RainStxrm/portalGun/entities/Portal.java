package me.RainStxrm.portalGun.entities;

import me.RainStxrm.portalGun.PortalGun;
import me.RainStxrm.portalGun.customevents.PlayerEnterPortalEvent;
import me.RainStxrm.portalGun.models.PortalType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.sound.sampled.Port;

public class Portal {

    Location location;
    ArmorStand portalStand;
    PortalType type;
    boolean active = false;

    public Portal(Location location, PortalType type) {
        this.location = location;
        this.type = type;
    }

    private Portal getPortal() {
        return this;
    }

    private void showParticles() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!active) cancel();
                location.clone().add(0,1.5,0).getWorld().spawnParticle(type == PortalType.BLUE ? Particle.SPLASH : Particle.LAVA, location, 5);
            }
        }.runTaskTimer(PortalGun.getPlugin(PortalGun.class), 0, 1);
    }

    private void checkForPlayers() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!active) cancel();

                for (Entity e : portalStand.getNearbyEntities(0.2, 0.2, 0.2)) {
                    if (!(e instanceof Player)) continue;
                    Bukkit.getPluginManager().callEvent(new PlayerEnterPortalEvent(((Player) e).getPlayer(), getPortal()));
                }
            }
        }.runTaskTimer(PortalGun.getPlugin(PortalGun.class), 0, 1);
    }

    private void spawnPortal() {
        if (portalStand != null) {
            portalStand.remove();
        }
        portalStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        portalStand.setSmall(true);
        portalStand.setGlowing(true);
        portalStand.setInvulnerable(true);
    }

    public void start() {
        if (!active) {
            this.active = true;
            spawnPortal();
            showParticles();
            checkForPlayers();
        }
    }

    public void move(Location location) {
        setLocation(location);
        spawnPortal();
        showParticles();
        checkForPlayers();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public int[] getBlockLocation() {
        return new int[]{location.getBlockX(), location.getBlockY(), location.getBlockZ()};
    }

    public void stop() {
        this.active = false;
    }

    public PortalType getType() {
        return type;
    }
}
