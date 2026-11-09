package me.RainStxrm.portalGun;

import me.RainStxrm.portalGun.events.PortalGunEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class PortalGun extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().log(Level.INFO, "PortalGun plugin Online");
        getServer().getPluginManager().registerEvents(new PortalGunEvents(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().log(Level.INFO, "PortalGun plugin Offline");
    }
}
