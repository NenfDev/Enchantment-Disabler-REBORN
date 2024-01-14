package me.opd.enchantmentdisabler.listeners;

import me.opd.enchantmentdisabler.EnchantmentDisablerPlugin;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class PrepareItemEnchantListener implements Listener {

    @EventHandler
    public void onPlayerPrepEnchant(PrepareItemEnchantEvent e){
        //Bukkit.getServer().broadcastMessage("Prep");

        //	Player p = e.getEnchanter();
        //ArrayList<Enchantment> allowed = new ArrayList<Enchantment>();

//        for(Enchantment en : EnchantmentDisablerPlugin.blockedEnchants.keySet()){
//            if(EnchantmentDisablerPlugin.blockedEnchants.get(en) == false && en.canEnchantItem(e.getItem())){
//                allowed.add(en);
//                e.getEnchanter().sendMessage(ChatColor.GOLD + en.toString());
//            }
//        }
        //e.getEnchanter().sendMessage(ChatColor.DARK_PURPLE + "" + EnchantmentDisablerPlugin.allowedEnchant.size() + " possible enchants");
       // e.getEnchanter().sendMessage(ChatColor.AQUA + "" + EnchantmentDisablerPlugin.blockedEnchants.size() + " size of blocked list");

        for(EnchantmentOffer eo : e.getOffers()){
            if(eo == null){
                continue;
            }
            if(EnchantmentDisablerPlugin.blockedEnchants.get(eo.getEnchantment()) == true){
               // e.getEnchanter().sendMessage(ChatColor.RED + String.valueOf(EnchantmentDisablerPlugin.allowedEnchant.size()));
                //e.getEnchanter().sendMessage(String.valueOf(chosen));
                //p.sendMessage(ChatColor.RED + allowed.get(chosen).toString());

                //e.getEnchanter().sendMessage("The enchantment " + eo.getEnchantment().toString() + " was blocked and replaced with " + EnchantmentDisablerPlugin.allowedEnchant.get(chosen).toString());
                eo.setEnchantment(newChosenEnchantment(e.getItem()));
                eo.setEnchantmentLevel((int)(newChosenEnchantment(e.getItem()).getMaxLevel()/2)+1);
                e.getEnchanter().updateInventory();

            }

            //eo.setEnchantment(Enchantment.ARROW_DAMAGE);
            //p.sendMessage("That enchantment is blocked!");
        }
    }

    public Enchantment newChosenEnchantment(ItemStack item){
        Random rand = new Random();
        int chosen = rand.nextInt(EnchantmentDisablerPlugin.allowedEnchant.size());
        if(EnchantmentDisablerPlugin.allowedEnchant.get(chosen).canEnchantItem(item)){
            return EnchantmentDisablerPlugin.allowedEnchant.get(chosen);
        }else{
            return newChosenEnchantment(item);
        }
    }
}
