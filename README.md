on ` one.microstream.storage.exceptions.StorageException: Active storage for one.microstream.storage.types.Database$Default@67220094 "MicroStream@storage_test" already exists.
` run application / test again

When running ./mvnw quarkus:dev microstream is not able to load the DataRoot class:

one.microstream.persistence.exceptions.PersistenceException: Missing runtime type for required type handler for type: org.acme.getting.started.DataRoot


The problem does not appear when running tests annotated with @QuarkusTest