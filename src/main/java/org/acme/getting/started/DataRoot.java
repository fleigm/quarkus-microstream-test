package org.acme.getting.started;

import java.util.ArrayList;
import java.util.List;

public class DataRoot {
  private final List<String> messages = new ArrayList<>();

  public DataRoot() {
  }

  public List<String> messages() {
    return new ArrayList<>(messages);
  }

  public void add(String message) {
    this.messages.add(message);
    App.getInstance().storageManager().store(this.messages);
  }


}
