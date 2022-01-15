package com.nflsedition.deathPunishment;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import net.milkbowl.vault.economy.Economy;

public class deathPunishment extends JavaPlugin implements Listener{
	
	public static HashMap<String, String> loghash = new HashMap<>();
	public static HashMap<String, JSONObject> langhash = new HashMap<>();

	public static Economy economy = null;
	public static boolean economyEnabled = false;
	
	public void onEnable() {
		
		//vault
		
        //��ʼ��Vault Economy API
        //���Ը���ʵ����Ҫ�����޸�
		if (getServer().getPluginManager().getPlugin("Vault")!=null) {
			RegisteredServiceProvider<Economy> vaultEcoAPI = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        	if (vaultEcoAPI != null) {
        		if ((economy = vaultEcoAPI.getProvider())!=null){
        			economyEnabled = true;
        			getLogger().info("Vault���������������þ�����ع���");
        		}
        	}else {
        		getLogger().info("Vaultδ������ȷ���ѽ��þ�����ع���");
        	}
        }else{
        	getLogger().info("Vaultδ��װ���ѽ��þ�����ع���");
        }
		
		if (!(getDataFolder().exists())){
			this.getDataFolder().mkdir();
		}
		
		//�����ļ�
		File config = new File(getDataFolder(),"config.yml");
		if (!(config.exists())) {
			this.saveDefaultConfig();
		}
		reloadConfig();
		
		getLogger().info("�����ļ��汾:"+getConfig().getString("version"));
		if (getConfig().getString("version").equals("1.1")) {
			getConfig().set("Locale", true);
			getConfig().set("LowVersionMode.Enabled", false);
			getConfig().set("LowVersionMode.Title", "{\"text\":\"LowVersionMode\",\"color\":\"red\"}");
			getConfig().set("LowVersionMode.Subtitle", "{\"text\":\"This server is running a low version\",\"color\":\"red\"}");
			getConfig().set("version", "1.2");
			saveConfig();
			getLogger().info("�����ļ��汾��������v1.1��");
		}
		
		//���������������
		
		File logfile = new File(getDataFolder(),"log.bin");
		if (logfile.exists()) {
			loghash = dpFile.loadMap(logfile.getAbsolutePath())
		}else {
			dpFile.saveMap(loghash, logfile.getAbsolutePath());
		}
		this.getLogger().info("�����ļ� "+logfile.getAbsolutePath()+" �ѹ���");
		
		//�����ļ�
		
		File langDirectory = new File(getDataFolder(),"lang");
		if (!(langDirectory.exists())) {
			langDirectory.mkdir();

			InputStream inputStream = getResource("zh_cn.lang");
			Path path = Paths.get(new File(langDirectory,"zh_cn.lang").getAbsolutePath());
			try {
				Files.copy(inputStream,path);
				inputStream.close();
			}catch (Exception e) {
				getLogger().info("Error occured");;
				e.printStackTrace();
			}

			inputStream = getResource("en_gb.lang");
			path = Paths.get(new File(langDirectory,"en_gb.lang").getAbsolutePath());
			try {
				Files.copy(inputStream,path);
				inputStream.close();
			}catch (Exception e) {
				getLogger().info("Error occured");;
				e.printStackTrace();
			}

			inputStream = getResource("zh_tw.lang");
			path = Paths.get(new File(langDirectory,"zh_tw.lang").getAbsolutePath());
			try {
				Files.copy(inputStream,path);
				inputStream.close();
			}catch (Exception e) {
				getLogger().info("Error occured");;
				e.printStackTrace();
			}
		}
		langhash = dpFile.loadLangs(langDirectory);
		
		//ע������ִ����
		this.getCommand("deathpunishment").setExecutor(new dpCommandExecutor(this));
		//ע���¼�
		this.getServer().getPluginManager().registerEvents(new dpPlayerDeathEventListener(this), this);
		this.getServer().getPluginManager().registerEvents(new dpPlayerRespawnEventListener(this), this);
		if (getConfig().getBoolean("LowVersionMode.Enabled")) {
			this.getServer().getPluginManager().registerEvents(new dpPlayerPickupItemEventListener(this), this);
		}else {
			this.getServer().getPluginManager().registerEvents(new dpEntityPickupItemEventListener(this), this);
		}
		this.getServer().getPluginManager().registerEvents(this, this);
		
		getLogger().info("deathPunishment �����ͷ����������!");
	}
	public void onDisable() {
		if (!(getDataFolder().exists())){
			this.getDataFolder().mkdir();
		}
		dpFile.saveMap(loghash, new File(getDataFolder(),"log.bin"));
		loghash.clear();
		langhash.clear();
		HandlerList.unregisterAll();
	}
	
	@EventHandler
	public void onInventoryPickupItem(InventoryPickupItemEvent event) {
		if ((event.getItem().getItemStack().hasItemMeta())&&(event.getItem().getItemStack().getItemMeta().hasLore())&&(event.getItem().getItemStack().getItemMeta().getLore().contains("��c��Ǯ����"))) {
			event.setCancelled(true);
		}
	}
}
