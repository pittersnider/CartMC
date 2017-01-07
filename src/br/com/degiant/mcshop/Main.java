package br.com.degiant.mcshop;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.jonathanxd.guihelper.GUIHelper;
import com.github.jonathanxd.guihelper.gui.GUI;
import com.github.jonathanxd.guihelper.manager.GUIManager;
import com.google.common.collect.Lists;

public class Main extends JavaPlugin implements Listener
{

  protected static GUIManager manager;
  protected static FileConfiguration categories;
  protected static File categoryFolder;
  protected static String commandName;
  protected static SQL data = null;
  protected static GUI categoriesInventory = null;
  protected static HashMap<String, FileConfiguration> catConfig = new HashMap<>();
  protected static Set<Key> cachedKeys = new HashSet<>();

  @Override
  public void onEnable ()
  {
    Bukkit.getConsoleSender().sendMessage("§f[ §7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f= §f]");
    this.getLogger().info("Registrando biblioteca GUIHelper desenvolvida por Jonathan Henrique...");
    manager = GUIHelper.init(this);
    this.reloadConfig();
    if (this.isEnabled())
    {
      this.getLogger().info("Inicializando rotina de tabelas do banco de dados...");
      CashAPI.createTables();
      KeyAPI.createTables();
      // CashAPI.cleanup();

      this.getLogger().info("Inicializando registros e tasks...");
      initCommand(new SimpleCommand(categories.getString("Comando", "cart"), this));
      initCommand(new SimpleCommand("cash", "Ver quanto de cash você tem", this));
      initCommand(new SimpleCommand("rlcash", "Comando de reiniciação do PL de cash", this));
      initCommand(new SimpleCommand("cartmc", "Comando de gerenciamento de cash", new Commands()));

      Bukkit.getScheduler().runTaskTimer(this, () ->
      {
        KeyAPI.list(); // Refresh cachedKeys list
      }, 0L, 15 * 20L);

      this.getServer().getPluginManager().registerEvents(this, this);
      this.getLogger().info("CartMC foi inicializado com êxito, desenvolvido por João Pedro Viana.");
    }
    Bukkit.getConsoleSender().sendMessage("§f[ §7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f=§7=§f= §f]");
  }

  @EventHandler(priority = EventPriority.LOWEST)
  private void onVipZero ( PlayerCommandPreprocessEvent event )
  {
    Player p = event.getPlayer();
    String f = event.getMessage().toLowerCase();
    String[] s = f.split(" ");
    String c = s[0];

    if (c.equalsIgnoreCase("/usarkey") || c.equalsIgnoreCase("/usekey"))
    {
      try
      {
        String key = s[1];
        Key cachedKey = new Key(key.trim());
        if (cachedKeys.contains(cachedKey))
        {
          cachedKeys.remove(cachedKey);
          event.setCancelled(true);
          double gift = KeyAPI.use(key, p.getName());
          String pn = p.getName();
          String gt = String.valueOf(gift);
          for (String comando : this.getConfig().getStringList("Comandos_Quando_Ativa_a_Key"))
          {
            comando = comando.replace("@player", pn).replaceAll("@cash", gt);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), comando);
          }
        }
      }
      catch (IndexOutOfBoundsException iof)
      {
      }
    }
  }

  @Override
  public boolean onCommand ( CommandSender sender, Command command, String label, String[] args )
  {
    if (!(sender instanceof Player))
    {
      sender.sendMessage("§cComando não suportado no console.");
      return false;
    }

    if (command.getName().equalsIgnoreCase("cash"))
    {
      double cash = CashAPI.retrieve(sender.getName());
      sender.sendMessage("§3Atualmente você tem §f$§b§l" + cash + "§3 de cash!");
    }
    else if (command.getName().equalsIgnoreCase("rlcash"))
    {
      if (sender.hasPermission("cartmc.admin"))
      {
        this.reloadConfig();
        sender.sendMessage("§3Configuração recarregada com sucesso!");
      }
    }
    else
    {
      manager.openGui(categoriesInventory, ((Player) sender));
    }
    return false;
  }

  @Override
  public void reloadConfig ()
  {
    if (categoriesInventory != null)
    {
      manager.unregisterGui(categoriesInventory);
      categoriesInventory = null;
    }

    catConfig.clear();

    File f = new File(this.getDataFolder(), "config.yml");
    File cats = new File(this.getDataFolder(), "categorias.yml");
    boolean desligar = false;

    if (!f.exists())
    {
      this.saveResource("config.yml", false);
      this.getLogger().info("Configuração 'config.yml' extraída com êxito.");
      desligar = true;
    }

    if (!cats.exists())
    {
      this.saveResource("categorias.yml", false);
      this.getLogger().info("Configuração 'categorias.yml' extraída com êxito.");
    }

    categoryFolder = new File(this.getDataFolder(), "categories");
    if (!categoryFolder.exists())
    {
      categoryFolder.mkdir();
      this.saveResource("testando.yml", false);
      File testando = new File(this.getDataFolder(), "testando.yml");
      if (testando.exists())
      {
        testando.renameTo(new File(categoryFolder, "testando.yml"));
      }
    }

    categories = YamlConfiguration.loadConfiguration(cats);

    for (File catFile : categoryFolder.listFiles())
    {
      if (catFile != null)
      {
        String fileName = catFile.getName();
        if (fileName.toLowerCase().endsWith(".yml"))
        {
          FileConfiguration catData = YamlConfiguration.loadConfiguration(catFile);
          catConfig.put(fileName, catData);
          this.getLogger().info(String.format("Carregando a categoria '%s'...", fileName));
        }
      }
    }

    categoriesInventory = Categories.generate(categories);
    manager.registerGui(categoriesInventory);

    super.reloadConfig();

    if (!this.getConfig().contains("Comandos_Quando_Ativa_a_Key"))
    {
      List<String> empty = Lists.newArrayList();
      this.getConfig().set("Comandos_Quando_Ativa_a_Key", empty);
      super.saveConfig();
      super.reloadConfig();
      this.getLogger().info("Adicionamos a nova variável 'Comandos_Quando_Ativa_a_Key' na config.yml, configure-o!");
    }

    if (desligar)
    {
      this.getLogger().info("Plugin desligado para que você configure o mysql no config.yml!");
      this.getServer().getPluginManager().disablePlugin(this);
      return;
    }

    String user = this.getConfig().getString("sql.user", "root"), pass = this.getConfig().getString("sql.pass", ""), serv = this.getConfig().getString("sql.serv", "localhost"), base = this.getConfig().getString("sql.base", "base");
    int port = this.getConfig().getInt("sql.port", 3306);
    data = new SQL(user, pass, serv, base, port);
  }

  public static void initCommand ( final Command command )
  {
    try
    {
      final Field field = Class.forName("org.bukkit.craftbukkit." + getVersion() + ".CraftServer").getDeclaredField("commandMap");
      field.setAccessible(true);

      final CommandMap cmap = (CommandMap) field.get(Bukkit.getServer());
      cmap.register(command.getName(), command);
    }
    catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  public static String getVersion ()
  {
    final String ver = Bukkit.getServer().getClass().getPackage().getName();
    return ver.substring(ver.lastIndexOf('.') + 1);
  }
}
