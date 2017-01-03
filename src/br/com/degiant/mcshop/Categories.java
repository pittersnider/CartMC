package br.com.degiant.mcshop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.github.jonathanxd.guihelper.gui.GUI;
import com.github.jonathanxd.guihelper.gui.GUI.Builder;
import com.github.jonathanxd.guihelper.util.ItemHelper;
import com.github.jonathanxd.guihelper.util.Lore;

public class Categories {

	@SuppressWarnings("deprecation")
	public static GUI generate(FileConfiguration data) {
		Builder builder = GUI.selection(ChatColor.translateAlternateColorCodes('&', data.getString("GUI.Titulo")));
		for (String key : data.getConfigurationSection("Categorias").getKeys(false)) {
			String path = "Categorias." + key;
			try {
				int id = data.getInt(String.format(path + ".%s", "Item.ID"));
				int subtype = data.getInt(String.format(path + ".%s", "Item.Subtipo"));
				String nome = ChatColor.translateAlternateColorCodes('&', data.getString(String.format(path + ".%s", "Item.Nome")));
				Lore lore = null;
				for (String descLine : data.getStringList(String.format(path + ".%s", "Item.Descricao"))) {
					descLine = ChatColor.translateAlternateColorCodes('&', descLine);
					if (lore == null) {
						lore = Lore.first(descLine);
						continue;
					} else {
						lore = lore.add(descLine);
						continue;
					}
				}

				ItemStack item = ItemHelper.stack(Material.getMaterial(id), nome, lore);
				MaterialData mdata = item.getData();
				mdata.setData((byte) subtype);
				item.setData(mdata);
				builder = builder.addItem(data.getInt(String.format(path + ".%s", "Referencias.SlotNumero")), item, click -> {
					Main.manager.openParentGui(click.viewSection, individual(String.format(path + ".%s", "Referencias.AoClicar")), click.player);
				});
			} catch (NumberFormatException e) {
				System.out.println("[CartMC] Erro ao carregar item da categoria no path (NFE) '" + path + "'");
				continue;
			}
		}

		GUI result = builder.build();
		return result;
	}

	public static GUI individual(String name) {
		return null;
	}

}
