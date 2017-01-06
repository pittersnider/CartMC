package br.com.degiant.mcshop;

import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class Commands implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!(sender.hasPermission("cartmc.admin"))) {
      sender.sendMessage("§cVocê não tem permissão para usar este comando.");
      return false;
    }

    if ((args.length <= 0) || (args.length >= 4)) {
      return this.sendHelpTopic(sender);
    }

    String section = args[0].toLowerCase().trim();

    if (section.equalsIgnoreCase("keys")) {
      for (String row : KeyAPI.list()) {
        sender.sendMessage(row);
      }
    }

    if (section.equalsIgnoreCase("del")) {
      if (args.length != 2) {
        return this.sendHelpTopic(sender);
      } else {
        String key = args[1];
        KeyAPI.del(key);
        return this.performed(sender);
      }
    }

    if (section.equalsIgnoreCase("new")) {
      if (args.length != 2) {
        return this.sendHelpTopic(sender);
      } else {
        String cash = args[1];
        if (!(this.isInt(cash))) {
          return this.numberPlease(sender);
        } else {
          String key = this.nextKey();
          KeyAPI.save(key, sender.getName(), Integer.parseInt(cash));
          sender.sendMessage(String.format("§3[CartMC] §bKey gerada: '%s'", key));
          return true;
        }
      }
    }

    if (section.equalsIgnoreCase("?")) {
      if (args.length != 2) {
        return this.sendHelpTopic(sender);
      } else {
        String target = args[1].toLowerCase();
        double balance = CashAPI.retrieve(target);
        sender.sendMessage(
            String.format("§3[CartMC] O jogador '%s' tem '%s' de cash.", target, balance));
        return true;
      }
    }

    if (section.equalsIgnoreCase("+") || section.equalsIgnoreCase("-")
        || section.equalsIgnoreCase("=")) {
      if (args.length != 3) {
        return this.sendHelpTopic(sender);
      } else {
        String target = args[1].toLowerCase();
        String cash = args[2];
        if (!this.isDouble(cash)) {
          return this.numberPlease(sender);
        } else {
          if (section.equalsIgnoreCase("+")) {
            CashAPI.add(target, Double.parseDouble(cash));
          } else if (section.equalsIgnoreCase("-")) {
            CashAPI.take(target, Double.parseDouble(cash));
          } else {
            CashAPI.def(target, Double.parseDouble(cash));
          }

          this.performed(sender);
          return true;
        }
      }
    }


    return false;
  }

  public boolean performed(CommandSender sender) {
    sender.sendMessage("§3[CartMC] §bOrdem processada com sucesso!");
    return false;
  }

  public boolean numberPlease(CommandSender sender) {
    sender.sendMessage("§cPor favor, use um número (inteiro ou decimal) válido.");
    return false;
  }

  public boolean isDouble(String value) {
    try {
      double d = Double.parseDouble(value);
      return (d >= 0);
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public boolean isInt(String value) {
    try {
      double i = Integer.parseInt(value);
      return (i >= 0);
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public boolean sendHelpTopic(CommandSender sender) {
    sender.sendMessage("§3[CartMC] §bComandos do Sistema de Loja:");
    sender.sendMessage("§3» §b/cartmc new <quantia de cash> §3= gerar nova key.");
    sender.sendMessage("§3» §b/cartmc del <key> §3= deletar key existente.");
    sender.sendMessage("§3» §b/cartmc keys §3= listar keys existentes.");
    sender.sendMessage("§3» §b/cartmc + <player> <quantia de cash> §3= doar cash à um player.");
    sender
    .sendMessage("§3» §b/cartmc - <player> <quantia de cash> §3= subtrair cash de um player.");
    sender.sendMessage(
        "§3» §b/cartmc = <player> <quantia de cash> §3= redefinir o cash de um player.");
    sender.sendMessage("§3» §b/cartmc ? <player> §3= ver quanto de cash um player tem.");
    return false;
  }

  public String nextKey() {
    String uuid = UUID.randomUUID().toString();
    return uuid.replaceAll(Pattern.quote("-"), "");
  }

}
