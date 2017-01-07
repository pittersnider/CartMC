package br.com.degiant.mcshop;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SimpleCommand extends Command
{

  private CommandExecutor executor = null;

  protected SimpleCommand(String command, CommandExecutor executor)
  {
    super(command, "Abrir a loja virtual do servidor", "", Arrays.asList());
    this.executor = executor;
  }

  protected SimpleCommand(String command, String desc, CommandExecutor executor)
  {
    super(command, desc, "", Arrays.asList());
    this.executor = executor;
  }

  @Override
  public boolean execute ( CommandSender sender, String label, String[] arguments )
  {
    return this.executor.onCommand(sender, this, label, arguments);
  }

}
