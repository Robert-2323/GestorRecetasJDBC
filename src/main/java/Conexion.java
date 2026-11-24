import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String SERVIDOR="localhost";
    private static final String PUERTO="3306";
    private static final String BD="recetario";
    private static final String URL="jdbc:mysql://" + SERVIDOR + ":" + PUERTO + "/" + BD;

    private static final String USUARIO="root";
    private static final String PSW="root";

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PSW);
    }

}
