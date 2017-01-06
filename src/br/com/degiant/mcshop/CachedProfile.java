package br.com.degiant.mcshop;

import java.io.Serializable;

public class CachedProfile implements Serializable, Cloneable {

  /**
   *
   */
  private static final long serialVersionUID = -1901994279085585129L;
  String playerName;
  double cashNow;

  public CachedProfile(String playerName, double cashNow) {
    this.playerName = playerName;
    this.cashNow = cashNow;
  }

  public String getPlayerName() {
    return this.playerName;
  }

  public double getCashNow() {
    return this.cashNow;
  }

  public boolean has(double expected) {
    return this.cashNow >= expected;
  }

  public CachedProfile update() {
    this.cashNow = CashAPI.retrieve(this.playerName);
    return this;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result)
        + ((this.playerName == null) ? 0 : this.playerName.toLowerCase().hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CachedProfile)) {
      return false;
    }
    CachedProfile other = (CachedProfile) obj;
    if (this.playerName == null) {
      if (other.playerName != null) {
        return false;
      }
    } else if (!this.playerName.equalsIgnoreCase(other.playerName)) {
      return false;
    }
    return true;
  }

}
