package org.acme.getting.started;


import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MicrostreamTest {

  @Test
  void load_without_quarkus() {
    App app = new App();
    app.storageManager(); // init storage

    app.data().add("hello world");

    // shutdown and reload storage
    app.shutdown();
    app.storageManager();

    List<String> messages = app.data().messages();
    System.out.println(messages.toString());

    assertTrue(app.data().messages().contains("hello world"));


    app.shutdown();
  }
}
