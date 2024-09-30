package guru.qa.niffler.data.entity.auth;

public enum Authority {
  read("read"), write("write");

  private String value;

  Authority(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
