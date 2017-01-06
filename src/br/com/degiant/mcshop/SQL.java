package br.com.degiant.mcshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class SQL {

  private String user;
  private String pass;
  private String serv;
  private String base;
  private int port;

  /**
   * @param user Usuário msyql
   * @param pass Senha do usuário mysql
   * @param serv Endereço do servidor mysql
   * @param base Nome do banco de dados mysql
   * @param port Porta do servidor mysql
   */
  protected SQL(String user, String pass, String serv, String base, int port) {
    super();
    this.user = user;
    this.pass = pass;
    this.serv = serv;
    this.base = base;
    this.port = port;
  }

  /**
   * Inicializa uma conexão mysql com autocommit definido como false.
   *
   * @return Conexão mysql aberta
   */
  public Connection initConnection() {
    try {
      String url = String.format("jdbc:mysql://%s:%s/%s", this.getServerHost(),
          this.getServerPort(), this.getDatabaseName());
      Connection db = DriverManager.getConnection(url, this.getUser(), this.getPassword());
      return db;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  private String getUser() {
    return this.user;
  }

  private String getPassword() {
    return this.pass;
  }

  private String getServerHost() {
    return this.serv;
  }

  private String getDatabaseName() {
    return this.base;
  }

  private int getServerPort() {
    return this.port;
  }

}
