package br.com.degiant.mcshop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CashAPI {

  public static boolean has(String playerName, double expected) {
    return retrieve(playerName) >= expected;
  }

  public static void cleanup() {
    Connection db = Main.data.initConnection();
    try {
      PreparedStatement ps = db.prepareStatement(
          "DELETE FROM degiant_mcshop_cash WHERE (cash = ? AND DATE_SUB(CURDATE(),INTERVAL 15 DAY) >= last_update);");
      ps.setDouble(1, 0.00);
      ps.executeUpdate();
      ps.close();
      db.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void reset(String playerName) {
    Connection db = Main.data.initConnection();
    try {
      PreparedStatement ps = db.prepareStatement(
          "INSERT INTO degiant_mcshop_cash (playerName, cash) VALUES (?, '0.0') ON DUPLICATE KEY UPDATE cash = '0.0';");
      ps.setString(1, playerName);
      ps.executeUpdate();
      ps.close();
      db.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void take(String playerName, double cashToBeRemoved) {
    Connection db = Main.data.initConnection();
    try {
      PreparedStatement ps = db.prepareStatement(
          "INSERT INTO degiant_mcshop_cash (playerName, cash) VALUES (?, ?) ON DUPLICATE KEY UPDATE cash = (cash - ?);");
      ps.setString(1, playerName);
      ps.setDouble(2, 0.0);
      ps.setDouble(3, cashToBeRemoved);
      ps.executeUpdate();
      ps.close();
      db.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void def(String playerName, double cashToBeSet) {
    Connection db = Main.data.initConnection();
    try {
      PreparedStatement ps = db.prepareStatement(
          "INSERT INTO degiant_mcshop_cash (playerName, cash) VALUES (?, ?) ON DUPLICATE KEY UPDATE cash = (cash);");
      ps.setString(1, playerName);
      ps.setDouble(2, cashToBeSet);
      ps.setDouble(3, cashToBeSet);
      ps.executeUpdate();
      ps.close();
      db.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void add(String playerName, double cashToBeAdded) {
    Connection db = Main.data.initConnection();
    try {
      PreparedStatement ps = db.prepareStatement(
          "INSERT INTO degiant_mcshop_cash (playerName, cash) VALUES (?, ?) ON DUPLICATE KEY UPDATE cash = (cash + ?);");
      ps.setString(1, playerName.toLowerCase());
      ps.setDouble(2, cashToBeAdded);
      ps.setDouble(3, cashToBeAdded);
      ps.executeUpdate();
      ps.close();
      db.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static double retrieve(String playerName) {
    Connection db = Main.data.initConnection();
    try {
      PreparedStatement ps = db.prepareStatement(
          "SELECT cash FROM degiant_mcshop_cash WHERE (LOWER(playerName) = LOWER(?));");
      ps.setString(1, playerName);

      ResultSet rs = ps.executeQuery();
      double result = 0.0;
      if (rs.next()) {
        result = rs.getDouble(1);
      }

      rs.close();
      ps.close();
      db.close();
      return result;
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return 0.0;
  }

  public static void createTables() {
    Connection db = Main.data.initConnection();
    try {
      PreparedStatement ps = db.prepareStatement(
          "CREATE TABLE IF NOT EXISTS degiant_mcshop_cash (id INT(255) NOT NULL AUTO_INCREMENT, playerName VARCHAR(17) UNIQUE NOT NULL, cash double(15,2) NOT NULL DEFAULT 0.00, register_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, primary key(id));");
      ps.execute();
      ps.close();
      db.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
