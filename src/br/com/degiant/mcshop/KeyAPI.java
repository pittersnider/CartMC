package br.com.degiant.mcshop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KeyAPI {

  public static double use(String key, String playerName) {
    Connection db = Main.data.initConnection();
    double cashToGive = 0.0;
    try {
      cashToGive = retrieve(key);
      if (cashToGive >= 0) {
        CashAPI.add(playerName, cashToGive);
        PreparedStatement ps = db.prepareStatement(
            "UPDATE degiant_mcshop_keys SET already_used = ?, used_by = ? WHERE (LOWER(`key`) = LOWER(?));");
        ps.setBoolean(1, true);
        ps.setString(2, playerName);
        ps.setString(3, key);
        ps.executeUpdate();
        ps.close();
      }
      db.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return cashToGive;
  }

  public static List<String> list() {
    Main.cachedKeys.clear();
    List<String> collection = new ArrayList<>();
    Connection db = Main.data.initConnection();
    try {
      PreparedStatement ps =
          db.prepareStatement("SELECT * FROM degiant_mcshop_keys WHERE already_used = ?;");
      ps.setBoolean(1, false);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        Main.cachedKeys.add(new Key(rs.getString("key")));
        collection.add("§3§lKey: §b" + rs.getString("key") + " §f§l| §3§lTotal de Cash: §b"
            + rs.getDouble("cash"));
      }

      rs.close();
      ps.close();
      db.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return collection;
  }

  public static void del(String key) {
    Connection db = Main.data.initConnection();
    try {
      PreparedStatement ps =
          db.prepareStatement("DELETE FROM degiant_mcshop_keys WHERE LOWER(`key`) = LOWER(?);");
      ps.setString(1, key);
      ps.executeUpdate();
      ps.close();
      db.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static double retrieve(String key) {
    Connection db = Main.data.initConnection();
    double result = 0.00;
    try {
      PreparedStatement ps = db.prepareStatement(
          "SELECT * FROM degiant_mcshop_keys WHERE (LOWER(`key`) = LOWER(?) AND already_used = ?);");
      ps.setString(1, key);
      ps.setBoolean(2, false);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        result = rs.getDouble("cash");
      } else {
        result = -1.0;
      }
      ps.close();
      db.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }

  public static void save(String key, String admin, double cash) {
    Connection db = Main.data.initConnection();
    try {
      PreparedStatement ps = db.prepareStatement(
          "INSERT INTO degiant_mcshop_keys (`key`,cash,generated_by) VALUES (?, ?, ?);");
      ps.setString(1, key);
      ps.setDouble(2, cash);
      ps.setString(3, admin);
      ps.executeUpdate();
      ps.close();
      db.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void createTables() {
    Connection db = Main.data.initConnection();
    try {
      PreparedStatement ps = db.prepareStatement(
          "CREATE TABLE IF NOT EXISTS `degiant_mcshop_keys` ( `id` int(255) NOT NULL AUTO_INCREMENT, `key` varchar(32) NOT NULL, `cash` double(15,2) NOT NULL DEFAULT '0.00', `used_by` varchar(17) DEFAULT NULL, `generated_by` varchar(17) NOT NULL, `already_used` tinyint(1) NOT NULL DEFAULT '0', `register_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`id`), UNIQUE KEY `key` (`key`) ) ENGINE=MyIsam DEFAULT CHARSET=utf8");
      ps.execute();
      ps.close();
      db.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
