package server.persistence.shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProviderImpl implements ConnectionProvider
{
    @Override
    public Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres?currentSchema=sep2",
                "postgres", "1234");
    }
}
