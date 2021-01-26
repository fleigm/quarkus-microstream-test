package org.acme.getting.started;

import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class MicrostreamWithQuarkusTest {

  private static final Logger logger = Logger.getLogger(MicrostreamWithQuarkusTest.class);

  @Inject
  App app;

  @Test
  void load_with_quarkus() {
    app.data().add("hello world");

    // shutdown and reload storage
    app.shutdown();
    app.storageManager();

    List<String> messages = app.data().messages();

    logger.info(messages.toString());

    assertTrue(app.data().messages().contains("hello world"));
  }
}
