package com.nflsedition.deathPunishment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class dpCommandExecutor implements CommandExecutor{

    private final Plugin dp;

    public dpCommandExecutor(Plugin plugin) {
        this.dp = plugin; // Store the plugin in situations where you need it.
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ((label.equalsIgnoreCase("deathpunishment")) || (label.equalsIgnoreCase("dp"))){ //�ǲ���dp������
			
			//"/dp help"����ָ��
			
			if ((args.length==0)||(args[0].equalsIgnoreCase("help"))) { //����ָ��
				if (sender instanceof Player) {
					Player p=(Player)sender;
					if (p.hasPermission("deathpunishment.admin.help")) {
						p.sendMessage("��c[��f�����ͷ����ָ�������c]");
						p.sendMessage("cʹ�á�f\"/deathpunishment help\"��c��ʾ��ҳ��");
						p.sendMessage(dp.getConfig().getString("DropSlots.Count"));
					}else{
						p.sendMessage("��c[��f�����ͷ������c]��f:��c���Ȩ�޲��㣡");
					}
				}else{
					sender.sendMessage("��c[��f�����ͷ����ָ�������c]");
					sender.sendMessage("��cʹ�á�f\"/deathpunishment help\"��c��ʾ��ҳ��");
				}
			}
			
			//"/dp what"��ѯָ��
			
			if (args[0].equalsIgnoreCase("what")) { //�鿴��Ʒ����
				if (sender instanceof Player) {
					Player p=(Player)sender;
					if (p.hasPermission("deathpunishment.admin.what")) {
						p.sendMessage("��c[��f�����ͷ������c]��f:��8�����ϵ���Ʒid�� ��f"+p.getInventory().getItemInMainHand().getType().toString());
						p.sendMessage("��c[��f�����ͷ������c]��f:��8ע���Сд�� ��f");
						p.sendMessage("��c[��f�����ͷ������c]��f:��8��������� ��f"+p.getLocale());
						//��ʾ��ħ������
						if ((p.getInventory().getItemInMainHand().hasItemMeta())&&((p.getInventory().getItemInMainHand().getItemMeta().hasEnchants()))){
							p.sendMessage("��c[��f�����ͷ������c]��f:��8��������Ʒ�ĸ�ħΪ��f:"+p.getInventory().getItemInMainHand().getItemMeta().getEnchants().toString());
						}
					}else {
						p.sendMessage("��c[��f�����ͷ������c]��f:��c���Ȩ�޲��㣡");
					}
				}else{
					sender.sendMessage("��c�������ֻ��������ã�");
				}
			}
			
			//"/dp reload"��������
			
			if (args[0].equalsIgnoreCase("reload")) { //���ز������
				
				if (sender.hasPermission("deathpunishment.admin.reload")) {
					try {
						if (!(dp.getDataFolder().exists())){
							dp.getDataFolder().mkdir();
						}
						
						//�����ļ�
						File config = new File(dp.getDataFolder(),"config.yml");
						if (!(config.exists())) {
							dp.saveDefaultConfig();
						}
						dp.reloadConfig();
						sender.sendMessage("��c[��f�����ͷ������c]��f:��8������������أ�");
					}catch(Exception e) {
						e.printStackTrace();
					}
				}else {
					sender.sendMessage("��c[��f�����ͷ������c]��f:��c���Ȩ�޲��㣡");
				}
			}
			
			//"/dp lore"�޸�Lore
			
			if (args[0].equalsIgnoreCase("lore")) { //�鿴��Ʒ����
				if (sender instanceof Player) {
					Player p=(Player)sender;
					if (p.hasPermission("deathpunishment.admin.lore")) {
						if (args.length>1) {
							if (args[1].equalsIgnoreCase("add")) {
								if (args.length>=3) {
									if (p.getInventory().getItemInMainHand().getType()!=Material.AIR) {
										if (p.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
											List<String> lorelist = p.getInventory().getItemInMainHand().getItemMeta().getLore();
											for(int i=2;i<args.length;i++) {
												lorelist.add(StringUtils.replace(args[i], "&", "��"));
											}
											ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
											meta.setLore(lorelist);
											p.getInventory().getItemInMainHand().setItemMeta(meta);
										}else {
											List<String> lorelist = new ArrayList<>();
											for(int i=2;i<args.length;i++) {
												lorelist.add(StringUtils.replace(args[i], "&", "��"));
											}
											ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
											meta.setLore(lorelist);
											p.getInventory().getItemInMainHand().setItemMeta(meta);
										}
										p.sendMessage("��c[��f�����ͷ������c]��f:��c�ɹ���ӱ�ǩ��");
										return true;
									}else{
										p.sendMessage("��c[��f�����ͷ������c]��f:��c��������������ϱ�ǩ���Ի󣩣�");
										return true;
									}
								}else {
									p.sendMessage("��c[��f�����ͷ������c]��f:��c��ʽ����ʹ�÷�ʽ��/dp lore add <��ǩ�ı�> <��ǩ�ı�> <...>");
									return true;
								}
							}
							if (args[1].equalsIgnoreCase("set")) {
								if (args.length==4) {
									if (p.getInventory().getItemInMainHand().getType()!=Material.AIR) {
										if (p.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
											try {
												List<String> lorelist = p.getInventory().getItemInMainHand().getItemMeta().getLore();
												lorelist.set(Integer.valueOf(args[2])-1, StringUtils.replace(args[3], "&", "��"));
												ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
												meta.setLore(lorelist);
												p.getInventory().getItemInMainHand().setItemMeta(meta);
												p.sendMessage(String.format("��c[��f�����ͷ������c]��f:��7�ɹ����ڡ�f%s��7�еı�ǩ�޸�Ϊ %s",args[2],StringUtils.replace(args[3], "&", "��")));
											}catch (Exception e) {
												p.sendMessage("��c[��f�����ͷ������c]��f:��cָ��ִ�й��̷�������������ȷ�����ñ�����ʹ�÷�ʽ��/dp lore set <���> <��ǩ�ı�>");
												e.printStackTrace();
												return false;
											}
										}else {
											p.sendMessage("��c[��f�����ͷ������c]��f:��c��Ʒ����ǩ��û�У���������ɶ���Ի󣩣�");
										}
										return true;
									}else{
										p.sendMessage("��c[��f�����ͷ������c]��f:��c��������������ϱ�ǩ���Ի󣩣�");
										return true;
									}
								}else {
									p.sendMessage("��c[��f�����ͷ������c]��f:��c��ʽ����ʹ�÷�ʽ��/dp lore set <���> <��ǩ�ı�>");
									return true;
								}
							}
							if (args[1].equalsIgnoreCase("remove")) {
								if (args.length==3) {
									if (p.getInventory().getItemInMainHand().getType()!=Material.AIR) {
										if (p.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
											try {
												List<String> lorelist = p.getInventory().getItemInMainHand().getItemMeta().getLore();
												lorelist.remove(Integer.valueOf(args[2])-1);
												ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
												meta.setLore(lorelist);
												p.getInventory().getItemInMainHand().setItemMeta(meta);
												p.sendMessage(String.format("��c[��f�����ͷ������c]��f:��7�ɹ����ڡ�f%s��7�Ƴ�",args[2]));
											}catch (Exception e) {
												p.sendMessage("��c[��f�����ͷ������c]��f:��cָ��ִ�й��̷�������������ȷ�����ñ�����ʹ�÷�ʽ��/dp lore remove <���>");
												e.printStackTrace();
												return false;
											}
										}else {
											p.sendMessage("��c[��f�����ͷ������c]��f:��c��Ʒ����ǩ��û�У���������ɶ���Ի󣩣�");
										}
										return true;
									}else{
										p.sendMessage("��c[��f�����ͷ������c]��f:��c��������������ϱ�ǩ���Ի󣩣�");
										return true;
									}
								}else {
									p.sendMessage("��c[��f�����ͷ������c]��f:��c��ʽ����ʹ�÷�ʽ��/dp lore remove <���>");
									return true;
								}
							}
							p.sendMessage("��c[��f�����ͷ������c]��f:��c��ʽ����ʹ�÷�ʽ��/dp lore <add/set/remove>");
						}else {
							p.sendMessage("��c[��f�����ͷ������c]��f:��c��ʽ����ʹ�÷�ʽ��/dp lore <add/set/remove>");
						}
					}else {
						p.sendMessage("��c[��f�����ͷ������c]��f:��c���Ȩ�޲��㣡");
					}
				}else{
					sender.sendMessage("��c[��f�����ͷ������c]��f:��c�������ֻ��������ã�");
				}
			}
			
			return true;
		}
		return false;
    }
}
