package net.hynse.kPF.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import java.util.concurrent.ThreadLocalRandom;

public class CrystalCurseListener implements Listener {
    
    private final NamespacedKey CRYSTAL_CURSE = new NamespacedKey("kattersstructures", "crystal_curse");
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        
        Player player = (Player) event.getDamager();
        ItemStack weapon = player.getInventory().getItemInMainHand();
        
        if (hasEnchantment(weapon)) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            
            // Crystal curse effects on hit with random variations
            Particle[] particles = {Particle.ENCHANT, Particle.PORTAL, Particle.END_ROD};
            player.getWorld().spawnParticle(
                particles[random.nextInt(particles.length)],
                event.getEntity().getLocation(),
                20,
                0.5, 0.5, 0.5,
                0.1
            );
            
            // Random sound effects
            Sound[] sounds = {
                Sound.BLOCK_AMETHYST_CLUSTER_BREAK,
                Sound.BLOCK_AMETHYST_BLOCK_CHIME,
                Sound.BLOCK_AMETHYST_BLOCK_HIT
            };
            player.playSound(
                player.getLocation(),
                sounds[random.nextInt(sounds.length)],
            1.0f,
                0.8f + (random.nextFloat() * 0.4f)  // Random pitch between 0.8 and 1.2
            );
            
            // Random durability damage and repair cost
            ItemMeta meta = weapon.getItemMeta();
            if (meta instanceof Damageable damageable) {
                int maxDurability = weapon.getType().getMaxDurability();
                int currentDamage = damageable.getDamage();
                if (currentDamage < maxDurability) {
                    damageable.setDamage(currentDamage + random.nextInt(1, 4)); // Random 1-3 damage
                }
                
                // Random repair cost increase (1-3)
                if (meta instanceof Repairable repairable) {
                    int currentCost = repairable.getRepairCost();
                    repairable.setRepairCost(currentCost + random.nextInt(1, 4));
                }
                
                weapon.setItemMeta(meta);
            }
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        
        if (hasEnchantment(mainHand)) {
            // Visual effects
            player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 3, 0.5, 0.5, 0.5, 0.05);
            
            // Sound effect (rare)
            if (ThreadLocalRandom.current().nextDouble() < 0.05) { // 5% chance
                player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 0.9f, 1.0f);
            }
        }
    }
    
    private boolean hasEnchantment(ItemStack item) {
        if (item != null && item.getItemMeta() != null) {
            ItemMeta meta = item.getItemMeta();
            Enchantment crystalCurse = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(CRYSTAL_CURSE);
            return crystalCurse != null && meta.hasEnchant(crystalCurse);
        }
        return false;
    }
}
