package br.com.degiant.mcshop;

import java.io.File;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.jonathanxd.guihelper.GUIHelper;
import com.github.jonathanxd.guihelper.gui.GUI;
import com.github.jonathanxd.guihelper.manager.GUIManager;

public class Main extends JavaPlugin {

	protected static GUIManager			manager;
	protected static FileConfiguration	categories;
	protected static File				categoryFolder;
	protected static String				commandName;
	protected static SQL				data				= null;
	protected static GUI				categoriesInventory	= null;

	public static void main(String[] args) {
		System.out.println("Just to classpath");
	}

	@Override
	public void onEnable() {
		this.reloadConfig();
		if (this.isEnabled()) {
			manager = GUIHelper.init(this);
			categoriesInventory = Categories.generate(categories);
			manager.registerGui(categoriesInventory);
			initCommand(new SimpleCommand(categories.getString("Comando", "cart"), this));
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cComando não suportado no console.");
			return false;
		}

		manager.openGui(categoriesInventory, ((Player) sender));
		return false;
	}

	@Override
	public void reloadConfig() {
		File f = new File(this.getDataFolder(), "config.yml");
		File cats = new File(this.getDataFolder(), "categorias.yml");
		boolean desligar = false;

		if (!f.exists()) {
			this.saveResource("config.yml", false);
			this.getLogger().info("Configuração 'config.yml' extraída com êxito.");
			desligar = true;
		}

		if (!cats.exists()) {
			this.saveResource("categorias.yml", false);
			this.getLogger().info("Configuração 'categorias.yml' extraída com êxito.");
		}

		categoryFolder = new File(this.getDataFolder(), "categories");
		if (!categoryFolder.exists()) {
			categoryFolder.mkdir();
			this.saveResource("testando.yml", false);
			File testando = new File(this.getDataFolder(), "testando.yml");
			if (testando.exists()) {
				testando.renameTo(new File(categoryFolder, "testando.yml"));
			}
		}

		categories = YamlConfiguration.loadConfiguration(cats);
		super.reloadConfig();

		if (desligar) {
			this.getLogger().info("Plugin desligado para que você configure o mysql no config.yml!");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}

		String user = this.getConfig().getString("sql.user", "root"), pass = this.getConfig().getString("sql.pass", ""), serv = this.getConfig().getString("sql.serv", "localhost"), base = this.getConfig().getString("sql.base", "base");
		int port = this.getConfig().getInt("sql.port", 3306);
		data = new SQL(user, pass, serv, base, port);
	}

	public static void initCommand(final Command command) {
		try {
			final Field field = Class.forName("org.bukkit.craftbukkit." + getVersion() + ".CraftServer").getDeclaredField("commandMap");
			field.setAccessible(true);

			final CommandMap cmap = (CommandMap) field.get(Bukkit.getServer());
			cmap.register(command.getName(), command);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String getVersion() {
		final String ver = Bukkit.getServer().getClass().getPackage().getName();
		return ver.substring(ver.lastIndexOf('.') + 1);
	}
}
