package com.nflsedition.deathPunishment;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
					}else {
						p.sendMessage("��c[��f�����ͷ������c]��f:��c���Ȩ�޲��㣡");
					}
				}else{
					sender.sendMessage("��c�������ֻ��������ã�");
				}
			}
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
			return true;
		}
		return false;
    }
}
