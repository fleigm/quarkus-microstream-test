package org.acme.getting.started;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import one.microstream.jdk8.java.util.BinaryHandlersJDK8;
import one.microstream.reflect.ClassLoaderProvider;
import one.microstream.storage.configuration.Configuration;
import one.microstream.storage.types.EmbeddedStorageFoundation;
import one.microstream.storage.types.EmbeddedStorageManager;
import one.microstream.storage.types.StorageManager;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import java.nio.file.Path;

@ApplicationScoped
public class App {
  private static final Logger logger = Logger.getLogger(App.class);

  private static App instance = null;

  public static App getInstance() {
    return instance;
  }

  private volatile StorageManager storageManager;

  public App() {
    App.instance = this;
  }

  private EmbeddedStorageManager createStorageManager() {

    final Configuration configuration = Configuration.Default()
        .setBaseDirectory(Path.of("storage").toString())
        .setChannelCount(2);

    final EmbeddedStorageFoundation<?> foundation = configuration
        .createEmbeddedStorageFoundation()
        .onConnectionFoundation(BinaryHandlersJDK8::registerJDK8TypeHandlers)
        .onConnectionFoundation(cf ->
            cf.setClassLoaderProvider(ClassLoaderProvider.New(Thread.currentThread().getContextClassLoader())));

    final EmbeddedStorageManager storageManager = foundation.createEmbeddedStorageManager().start();

    if (storageManager.root() == null) {
      final DataRoot data = new DataRoot();
      storageManager.setRoot(data);
      storageManager.storeRoot();
    }

    return storageManager;
  }

  public StorageManager storageManager() {
    /*
     * Double-checked locking to reduce the overhead of acquiring a lock
     * by testing the locking criterion.
     * The field (this.storageManager) has to be volatile.
     */
    if (this.storageManager == null) {
      synchronized (this) {
        if (this.storageManager == null) {
          this.storageManager = this.createStorageManager();
        }
      }
    }

    return this.storageManager;
  }

  @Produces
  @ApplicationScoped
  public DataRoot data() {
    return (DataRoot) storageManager().root();
  }

  public void shutdown() {
    storageManager().shutdown();
  }

  public void init(@Observes StartupEvent event) {
    storageManager();
    logger.info("Initialized storage manager");
  }

  public void exit(@Observes ShutdownEvent event) {
    shutdown();
    logger.info("shutdown storage manager");
  }
}
