package server.persistence.shared;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The {@code ConnectionProviderImpl} class provides a concrete implementation
 * of the {@link ConnectionProvider} interface.
 * <p>
 * It establishes and returns a connection to the PostgreSQL database using
 * hardcoded connection parameters.
 */
public interface ConnectionProvider
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
    Connection getConnection() throws SQLException;
}
