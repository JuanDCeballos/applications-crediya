package co.juan.crediya.r2dbc.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostgresqlConnectionPropertiesTest {

    @Test
    void shouldCreateRecordWithCorrectValues() {
        String host = "localhost";
        Integer port = 5432;
        String database = "test_db";
        String schema = "public";
        String username = "user";
        String password = "password";

        PostgresqlConnectionProperties properties = new PostgresqlConnectionProperties(
                host, port, database, schema, username, password
        );

        assertThat(properties.host()).isEqualTo(host);
        assertThat(properties.port()).isEqualTo(port);
        assertThat(properties.database()).isEqualTo(database);
        assertThat(properties.schema()).isEqualTo(schema);
        assertThat(properties.username()).isEqualTo(username);
        assertThat(properties.password()).isEqualTo(password);
    }

    @Test
    void shouldBeEqualWhenPropertiesAreIdentical() {
        PostgresqlConnectionProperties properties1 = new PostgresqlConnectionProperties(
                "localhost", 5432, "test_db", "public", "user", "password"
        );
        PostgresqlConnectionProperties properties2 = new PostgresqlConnectionProperties(
                "localhost", 5432, "test_db", "public", "user", "password"
        );

        assertThat(properties1).isEqualTo(properties2);
        assertThat(properties1.hashCode()).hasSameHashCodeAs(properties2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenPropertiesDiffer() {
        PostgresqlConnectionProperties properties1 = new PostgresqlConnectionProperties(
                "localhost", 5432, "test_db", "public", "user", "password"
        );
        PostgresqlConnectionProperties properties2 = new PostgresqlConnectionProperties(
                "remotehost", 5432, "test_db", "public", "user", "password"
        );

        assertThat(properties1).isNotEqualTo(properties2);
    }
}
