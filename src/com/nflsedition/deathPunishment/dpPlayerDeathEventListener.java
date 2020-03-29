package com.nflsedition.deathPunishment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class dpPlayerDeathEventListener implements Listener{

    private final Plugin dp;

    public dpPlayerDeathEventListener(Plugin plugin) {
        this.dp = plugin; // Store the plugin in situations where you need it.
    }
    
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOW)
	
	public void onPlayerDeath(PlayerDeathEvent event){
		
		if (dp.getConfig().contains("Enabled")) {
			
			boolean gamemode = true;
			
			Player player = event.getEntity();
			Location location = player.getLocation();
			Inventory inventory = player.getInventory();
			String worldname = location.getWorld().getName();
			String worlddir = "";
			
			Map<String, String> messagehash = new HashMap<>();
			messagehash.put("death", dp.getConfig().getString("Messages.death"));
			messagehash.put("deathworld", dp.getConfig().getString("Messages.deathworld"));
			messagehash.put("drop", dp.getConfig().getString("Messages.drop"));
			messagehash.put("exp", dp.getConfig().getString("Messages.exp"));
			messagehash.put("title", dp.getConfig().getString("Messages.title"));
			messagehash.put("subtitle", dp.getConfig().getString("Messages.subtitle"));
			messagehash.put("money", dp.getConfig().getString("Messages.money"));
			messagehash.put("loot", dp.getConfig().getString("Messages.loot"));
			messagehash.put("lootname", dp.getConfig().getString("Messages.lootname"));
			
			for (Map.Entry<String, String> entry: messagehash.entrySet()) {
				messagehash.put(entry.getKey(), dpFile.getLocaleMessage(entry.getKey(), player.getLocale(), entry.getValue()));
			}
			
			//��¼��������
			deathPunishment.loghash.put(player.getName(), worldname);
			
			//�����Ǯ
			
			if ((dp.getConfig().getBoolean(worlddir+"DropMoney.Enabled"))&&(deathPunishment.economyEnabled)){
				String mode = dp.getConfig().getString(worlddir+"DropMoney.Mode");
				Random random = new Random();
				if (mode != null) {
					double amount = 0.0;
					switch (mode) {
						case "Fixed": {
							double money = dp.getConfig().getDouble(worlddir+"DropMoney.Fixed.Money");
							OfflinePlayer offlinePlayer = (OfflinePlayer)player;
							if (deathPunishment.economy.hasAccount(offlinePlayer)){
								if (deathPunishment.economy.getBalance(offlinePlayer)>=money) {
									deathPunishment.economy.withdrawPlayer(offlinePlayer, money);
									amount = money;
								}else {
									deathPunishment.economy.withdrawPlayer(offlinePlayer, deathPunishment.economy.getBalance(offlinePlayer));
									amount = deathPunishment.economy.getBalance(offlinePlayer);
								}
							}else {
								deathPunishment.economy.createPlayerAccount(offlinePlayer);
							}
							break;
						}
						case "Random": {
							double moneymin = dp.getConfig().getDouble(worlddir+"DropMoney.Random.MoneyMin");
							double moneymax = dp.getConfig().getDouble(worlddir+"DropMoney.Random.MoneyMax");
							int digit = dp.getConfig().getInt(worlddir+"DropMoney.Random.DigitAfterDot");
							double moneyraw = random.nextDouble()*(moneymax-moneymin)+moneymin;
							BigDecimal bigDecimal = new BigDecimal(moneyraw);
							double money = bigDecimal.setScale(digit,BigDecimal.ROUND_HALF_UP).doubleValue();
							OfflinePlayer offlinePlayer = (OfflinePlayer)player;
							if (deathPunishment.economy.hasAccount(offlinePlayer)){
								if (deathPunishment.economy.getBalance(offlinePlayer)>=money) {
									deathPunishment.economy.withdrawPlayer(offlinePlayer, money);
									amount = money;
								}else {
									deathPunishment.economy.withdrawPlayer(offlinePlayer, deathPunishment.economy.getBalance(offlinePlayer));
									amount = deathPunishment.economy.getBalance(offlinePlayer);
								}
							}else {
								deathPunishment.economy.createPlayerAccount(offlinePlayer);
							}
							break;
						}
						case "Rate": {
							double moneyrate = dp.getConfig().getDouble(worlddir+"DropMoney.Rate.MoneyRate");
							int digit = dp.getConfig().getInt(worlddir+"DropMoney.Random.DigitAfterDot");
							OfflinePlayer offlinePlayer = (OfflinePlayer)player;
							double moneyraw = deathPunishment.economy.getBalance(offlinePlayer)*moneyrate;
							BigDecimal bigDecimal = new BigDecimal(moneyraw);
							double money = bigDecimal.setScale(digit,BigDecimal.ROUND_HALF_UP).doubleValue();
							if (deathPunishment.economy.hasAccount(offlinePlayer)){
								if (deathPunishment.economy.getBalance(offlinePlayer)>=money) {
									deathPunishment.economy.withdrawPlayer(offlinePlayer, money);
									amount = money;
								}else {
									deathPunishment.economy.withdrawPlayer(offlinePlayer, deathPunishment.economy.getBalance(offlinePlayer));
									amount = deathPunishment.economy.getBalance(offlinePlayer);
								}
							}else {
								deathPunishment.economy.createPlayerAccount(offlinePlayer);
							}
							break;
						}
						default: dp.getLogger().info("��Ǯ����ģʽ���ô�����ȡ������");break;
					}
					player.sendMessage(String.format(messagehash.get("money"), ""+amount));
					if (dp.getConfig().getDouble(worlddir+"DropMoney.Loot")>0){
						double loot = dp.getConfig().getDouble(worlddir+"DropMoney.Loot");
						int digit = dp.getConfig().getInt(worlddir+"DropMoney.DigitAfterDot");
						BigDecimal lootbd = new BigDecimal(amount*loot);
						double lootfinal = lootbd.setScale(digit,BigDecimal.ROUND_HALF_UP).doubleValue();
						Material looticon = Material.getMaterial(dp.getConfig().getString(worlddir+"DropMoney.LootIcon"));
						ItemStack lootitem = new ItemStack(looticon, (int)Math.floor(amount));
						ItemMeta meta = lootitem.getItemMeta();
						meta.setDisplayName(""+amount);
						List<String> lore = new ArrayList<>();
						lore.add("��c��Ǯ����");
						lore.add(""+lootfinal);
						meta.setLore(lore);
						meta.setDisplayName(""+lootfinal);
						lootitem.setItemMeta(meta);
						Item itemdrop = location.getWorld().dropItem(location, lootitem);
						itemdrop.setCustomNameVisible(true);
						itemdrop.setCustomName(String.format(messagehash.get("lootname"), ""+lootfinal));
						player.sendMessage(String.format(messagehash.get("loot"), ""+lootfinal));
					}
				}else {
					dp.getLogger().info("��ȡ��Ǯ��������ʱ����������ɾ�������ļ��������ɣ�");
				}
			}
			
			//������Ʒ
			
			//boolean Slots[] = new boolean[41];
			String namestring = "";
			List<Integer> Slots= new ArrayList<>();
			int namestringlns = 0,droppedcount = 0,droppingslot = 0,ran1 = 1;
			
			for(int i=0;i<41;i++) {
				Slots.add(i);
			}
			
			if (dp.getConfig().getBoolean("Enabled")) {  //����Ƿ�����
				if (dp.getConfig().getBoolean("SurvivalOnly")) {  //����Ƿ��������
					if (player.getGameMode().equals(GameMode.CREATIVE)) {
						gamemode = false;
					}
				}
				
				if (gamemode) {
					player.sendMessage(messagehash.get("death"));
					player.sendMessage(String.format(messagehash.get("deathworld"),worldname));
					
					if (dp.getConfig().contains(worldname))
						worlddir = worldname+".";
					
					//������Ʒ
					
					int count = dp.getConfig().getInt(worlddir+"DropSlots.Count");
					List<?> protectedslots = dp.getConfig().getList(worlddir+"DropSlots.ProtectedSlots");
					List<?> protecteditems = dp.getConfig().getList(worlddir+"DropSlots.ProtectedItems");
					List<?> protectedlores = dp.getConfig().getList(worlddir+"DropSlots.ProtectedLores");
					List<?> protectedenchantments = dp.getConfig().getList(worlddir+"DropSlots.ProtectedEnchantments");
					
					if ((dp.getConfig().contains(worlddir+"DropSlots.Enabled"))&&(dp.getConfig().getBoolean(worlddir+"DropSlots.Enabled"))) {
						if (count!=0) {
							Random r = new Random();
							for(int i=0;((i<count)&&(!(Slots.isEmpty())));) {
								if (Slots.size()>1){
									ran1 = r.nextInt(Slots.size()-1);
								}else {
									ran1 = 0;
								}
								if (!(protectedslots.size()+count>41)) {
									
									droppingslot = Slots.get(ran1);
									
									if (inventory.getItem(droppingslot)!=null) {
										boolean anyProtected = false;
										if (protecteditems.contains(inventory.getItem(droppingslot).getType().toString())) {
											anyProtected = true;
										}
											
										//Lore����
											
											
										for (Object lore : protectedlores) {
											String string = lore.toString();
											if (lore.toString().contains("%s")) {
												string = String.format(string, player.getName());
											}
											if (inventory.getItem(droppingslot).getItemMeta().hasLore()) {
												if (inventory.getItem(droppingslot).getItemMeta().getLore().contains(string)) {
													anyProtected = true;
												}
											}
										}
										
										//��ħ����
										
										for (Object enchantment: protectedenchantments) {
											Enchantment ench = Enchantment.getByName((String) enchantment);
											if (inventory.getItem(droppingslot).getEnchantments().containsKey(ench)) {
												anyProtected = true;
											}
										}
										
										if (!(anyProtected)){
											location.getWorld().dropItem(location, inventory.getItem(droppingslot));
											droppedcount++;
											if (inventory.getItem(droppingslot).getItemMeta().hasDisplayName()){
												namestring += inventory.getItem(droppingslot).getItemMeta().getDisplayName()+"��c*"+inventory.getItem(droppingslot).getAmount()+", ��f";
												if ((namestring.length()-namestringlns*10)>=10) {
													namestring += "\\n";
													namestringlns++;
												}
											}else {
												namestring += dpFile.getLocaleName(inventory.getItem(droppingslot).getType(),player.getLocale())+"��c*"+inventory.getItem(droppingslot).getAmount()+", ��f";
												if ((namestring.length()-namestringlns*10)>=10) {
													namestring += "\\n";
													namestringlns++;
												}
											}
											inventory.clear(droppingslot);
											i++;
										}
									}
									Slots.remove(ran1);
								}else {
									Bukkit.getServer().getLogger().info("δд��������Ʒ��������������ȡ�����䣡");
									Bukkit.getServer().getLogger().info("������Ӳ��������ء���");
								}
							}
							player.sendTitle(messagehash.get("title"), messagehash.get("subtitle"), dp.getConfig().getInt("Messages.fadein"), dp.getConfig().getInt("Messages.stay"), dp.getConfig().getInt("Messages.fadeout"));
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw "+player.getName()+" [{\"text\":\""+String.format(messagehash.get("drop"),droppedcount)+"\",\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+namestring+"\"},\"color\":\"red\",\"underlined\":\"true\"}]");
							player.updateInventory();
						}
					}
					
					//���侭��
					
					if (dp.getConfig().getBoolean(worlddir+"DropExp.Enabled")){
						
						String mode = dp.getConfig().getString(worlddir+"DropExp.Mode");
						int droppedlevel = 0;
						
						switch (mode) {
						case "Fixed":{
							int level = dp.getConfig().getInt(worlddir+"DropExp.Fixed.Level");
							boolean spawnorb = dp.getConfig().getBoolean(worlddir+"DropExp.Fixed.SpawnOrb");
							int orbperlevel = dp.getConfig().getInt(worlddir+"DropExp.Fixed.OrbPerLevel");
							int orbvalue = dp.getConfig().getInt(worlddir+"DropExp.Fixed.OrbValue");
							droppedlevel = dpExpDrop.dropExpFixed(player, level, spawnorb, orbperlevel, orbvalue);
							break;
						}
						case "Random":{
							int levelmin = dp.getConfig().getInt(worlddir+"DropExp.Random.LevelMin");
							int levelmax = dp.getConfig().getInt(worlddir+"DropExp.Random.LevelMax");
							boolean spawnorb = dp.getConfig().getBoolean(worlddir+"DropExp.Fixed.SpawnOrb");
							int orbperlevel = dp.getConfig().getInt(worlddir+"DropExp.Fixed.OrbPerLevel");
							int orbvalue = dp.getConfig().getInt(worlddir+"DropExp.Fixed.OrbValue");
							droppedlevel = dpExpDrop.dropExpRandom(player, levelmin, levelmax, spawnorb, orbperlevel, orbvalue);
							break;
						}
						case "Rate":{
							double levelrate = dp.getConfig().getDouble(worlddir+"DropExp.Rate.Level");
							boolean spawnorb = dp.getConfig().getBoolean(worlddir+"DropExp.Rate.SpawnOrb");
							int orbperlevel = dp.getConfig().getInt(worlddir+"DropExp.Rate.OrbPerLevel");
							int orbvalue = dp.getConfig().getInt(worlddir+"DropExp.Rate.OrbValue");
							droppedlevel = dpExpDrop.dropExpRate(player, levelrate, spawnorb, orbperlevel, orbvalue);
							break;
						}
						default: droppedlevel = dpExpDrop.dropExpDefault(player, dp.getConfig().getInt(worlddir+"DropExp.Default.Limit"));break;
						}
						
						player.sendMessage(String.format(messagehash.get("exp"), ""+droppedlevel));
					}
				}
			}
		}else {
			dp.saveDefaultConfig();
		}
	}
}
