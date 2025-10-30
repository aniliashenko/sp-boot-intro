package mate.academy.intro.config;

import org.testcontainers.containers.MySQLContainer;

public class CustomMySqlContainer extends MySQLContainer<CustomMySqlContainer> {
    private static final String IMAGE_VERSION = "mysql:8.0";
    private static CustomMySqlContainer container;

    private CustomMySqlContainer() {
        super(IMAGE_VERSION);
    }

    public static synchronized CustomMySqlContainer getInstance() {
        if (container == null) {
            container = new CustomMySqlContainer()
                    .withUsername("test")
                    .withPassword("test")
                    .withDatabaseName("test_db");
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("spring.datasource.url", container.getJdbcUrl());
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
    }

    @Override
    public void stop() {
    }
}
