package net.hynse.kPF;

import net.hynse.kPF.listeners.CrystalCurseListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class KPF extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new CrystalCurseListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
