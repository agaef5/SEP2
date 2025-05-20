package server.persistence.shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The {@code ConnectionProviderImpl} class provides a concrete implementation
 * of the {@link ConnectionProvider} interface.
 * <p>
 * It establishes and returns a connection to the PostgreSQL database using
 * hardcoded connection parameters.
 */
public class ConnectionProviderImpl implements ConnectionProvider
{
    /**
     * Returns a new connection to the PostgreSQL database.
     * <p>
     * The connection is established using the PostgreSQL JDBC URL, user,
     * and password specified in the method.
     *
     * @return A {@link Connection} object connected to the database.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres?currentSchema=sep2",
                "postgres", "1234");
    }
}
