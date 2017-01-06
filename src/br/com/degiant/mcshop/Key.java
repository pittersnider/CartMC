package br.com.degiant.mcshop;

import java.io.Serializable;

public class Key implements Serializable, Cloneable {

  private static final long serialVersionUID = -7626389362163206646L;
  private String key;

  protected Key(String key) {
    super();
    this.key = key;
  }

  public String getKey() {
    return this.key;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.key == null) ? 0 : this.key.toLowerCase().hashCode());
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
    if (!(obj instanceof Key)) {
      return false;
    }
    Key other = (Key) obj;
    if (this.key == null) {
      if (other.key != null) {
        return false;
      }
    } else if (!this.key.equalsIgnoreCase(other.key)) {
      return false;
    }
    return true;
  }

}
