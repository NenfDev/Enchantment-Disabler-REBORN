package me.opd.enchantmentdisabler;

import me.opd.enchantmentdisabler.commands.MenuCommand;
import me.opd.enchantmentdisabler.listeners.*;
import me.opd.enchantmentdisabler.utils.ConfigUtilsEN;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class EnchantmentDisablerPlugin extends JavaPlugin {

    public static HashMap<Enchantment, Boolean> blockedEnchants;
    public static ArrayList<Enchantment> allowedEnchant;

    public void onEnable(){

        Bukkit.getServer().getPluginManager().registerEvents(new PrepareItemEnchantListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ItemClickListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EnchantItemListener(), this);

        if(this.getConfig().getBoolean("PurgeExistingDisabledEnchantedItemsToo") == true) {
            Bukkit.getServer().getPluginManager().registerEvents(new InventoryOpenListener(), this);
            Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
            Bukkit.getServer().getPluginManager().registerEvents(new EntityPickupItemListener(), this);
        }


        EnchantmentDisablerPlugin.blockedEnchants = new HashMap<Enchantment, Boolean>();

        Bukkit.getServer().getPluginCommand("ed").setExecutor(new MenuCommand(this));

        getConfig().options().copyDefaults(true);
        saveConfig();

        ConfigUtilsEN.loadHashMapFromConfig(this);
        //System.out.println(EnchantmentDisablerPlugin.blockedEnchants.toString());
        allowedEnchant = new ArrayList<Enchantment>();

        for(Enchantment en : EnchantmentDisablerPlugin.blockedEnchants.keySet()){
            if(EnchantmentDisablerPlugin.blockedEnchants.get(en)==false){
                EnchantmentDisablerPlugin.allowedEnchant.add(en);
                //System.out.println("Enchant is allowed: " + en.toString());
            }
        }
    }

    public void onDisable(){
        ConfigUtilsEN.syncHashMapWithConfig(this);
    }
}
