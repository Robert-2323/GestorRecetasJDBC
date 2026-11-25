import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int option=-1;
        while (option!=0){
            System.out.println("0-Salir\n" +
                    "1-Mostrar todas las recetas almacenadas.\n" +
                    "2-Buscar una receta mediante su identificador.\n" +
                    "3-Añadir nuevas recetas.\n" +
                    "4-Modificar el tiempo de preparación y el número de raciones de una\n" +
                    "receta.\n" +
                    "5-Eliminar una receta del catálogo\n" +
                    "6-Buscar receta por nombre");
            System.out.print("Elige una opcion: ");
            option=sc.nextInt();
            if (option<7 && option>=0){
                if (option == 1) {
                    mostrarRecetas();
                }

                if (option==2){
                    buscarRecetaID();
                }

                if (option==3){
                    añadirReceta();
                }

                if (option==4){
                    modificarReceta();
                }

                if (option==5){
                    borrarReceta();
                }
                if (option==6){
                    buscarRecetaNombre();
                }

            }else {
                System.out.println("El valor introducito no es incorrecto");
                option=-1;
            }
        }
        System.out.println("Fin del programa");
    }

    public static void mostrarRecetas(){
        String querySQL="SELECT * FROM recetas";
        try (Connection con= Conexion.getConexion()){
            Statement st=con.createStatement();
            ResultSet rs= st.executeQuery(querySQL);

            while (rs.next()){
                int id=rs.getInt("id");
                String nombre=rs.getString("nombre");
                String categoria=rs.getString("categoria");
                int tiempoPreparacion=rs.getInt("tiempo_preparacion");
                String dificultad=rs.getString("dificultad");
                int raciones=rs.getInt("raciones");

                System.out.println(id+"-"+nombre+"-"+categoria+"-"+tiempoPreparacion+"-"+dificultad+"-"+raciones);
            }
            System.out.println();
        }catch (SQLException e){
            System.out.println("SQL-ERROR: "+e.getMessage());
        }
    }

    public static void buscarRecetaID(){
        Scanner sc=new Scanner(System.in);
        System.out.println("Introduzca la ID de la receta que desea buscar");
        int idReceta=sc.nextInt();

        String sql= """ 
	                select * from recetas WHERE id=?;
                """;

        try (Connection con= Conexion.getConexion()){
            PreparedStatement pst=con.prepareStatement(sql);
            pst.setInt(1,idReceta);

            ResultSet rs= pst.executeQuery();
            while (rs.next()){
                int id=rs.getInt("id");
                String nombre=rs.getString("nombre");
                String categoria=rs.getString("categoria");
                int tiempoPreparacion=rs.getInt("tiempo_preparacion");
                String dificultad=rs.getString("dificultad");
                int raciones=rs.getInt("raciones");

                System.out.println(id+"-"+nombre+"-"+categoria+"-"+tiempoPreparacion+"-"+dificultad+"-"+raciones);
            }
            System.out.println();


        }catch (SQLException e){
            System.out.println("SQL-ERROR: "+e.getMessage());
        }
    }

    public static void añadirReceta(){
        Scanner sc=new Scanner(System.in);

        System.out.println("Introduzca los datos de la nueva receta que deseea añadir");
        System.out.println("Nombre: ");
        String nombre=sc.nextLine();
        System.out.println("Categoria: ");
        String categoria=sc.nextLine();
        System.out.println("Tiempo de preparacion: ");
        int tiempoPreparacion=sc.nextInt();
        System.out.println("Dificualtad: ");
        String dificultad=sc.next();
        System.out.println("Raciones: ");
        int raciones=sc.nextInt();

        String sql= """
                INSERT INTO recetas(nombre,categoria,tiempo_preparacion,dificultad,raciones)
                VALUES(?,?,?,?,?);
                """;

        Connection con = null;
        PreparedStatement pst = null;

        try {
            con= Conexion.getConexion();
            con.setAutoCommit(false);

            pst=con.prepareStatement(sql);

            pst.setString(1,nombre);
            pst.setString(2,categoria);
            pst.setInt(3,tiempoPreparacion);
            pst.setString(4,dificultad);
            pst.setInt(5,raciones);

            pst.executeUpdate();
            con.commit();

            System.out.println("Receta añadida correctamente");
        }catch (SQLException e){
            System.out.println("SQL-ERROR: "+e.getMessage());
            try {
                if (con!=null){
                    con.rollback();
                    System.out.println("No se ha podido añadir la receta");
                }
            }catch (SQLException ex){
                System.out.println("SQL-ERROR: "+e.getMessage());
            }
        }finally {
            try {
                // Cierra la sentencia
                if (pst != null) {
                    pst.close();
                }
                // Cierra la conexión
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


    }

    public static void modificarReceta(){
        Scanner sc=new Scanner(System.in);

        System.out.println("Introduzca el ID de la receta que desea modificar: ");
        int id=sc.nextInt();
        System.out.println("Introduzca el nuevo tiempo de preparacion: ");
        int tiempoPreparacion=sc.nextInt();
        System.out.println("Introduzca la nueva cantidad de raciones: ");
        int raciones=sc.nextInt();

        String sql= """
                update recetas Set tiempo_preparacion=?,raciones=? where id=?;
                """;
        Connection con=null;
        PreparedStatement pst=null;

        try {
            con=Conexion.getConexion();
            con.setAutoCommit(false);

            pst=con.prepareStatement(sql);

            pst.setInt(1,tiempoPreparacion);
            pst.setInt(2,raciones);
            pst.setInt(3,id);

            pst.executeUpdate();

            con.commit();
            System.out.println("Receta modificada correctamente");

        }catch (SQLException e){
            System.out.println("SQL-ERROR: "+e.getMessage());
            try {
                if (con!=null){
                    con.rollback();
                    System.out.println("No se ha podido modificar la receta");
                }
            }catch (SQLException ex){
                System.out.println("SQL-ERROR: "+e.getMessage());
            }
        }finally {
            try {
                // Cierra la sentencia
                if (pst != null) {
                    pst.close();
                }
                // Cierra la conexión
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void borrarReceta(){
        Scanner sc=new Scanner(System.in);

        System.out.println("Introduzca el ID de la receta que desea borrar: ");
        int id=sc.nextInt();

        String sql= """
                delete from recetas where id=?;
                """;
        Connection con=null;
        PreparedStatement pst=null;

        try {
            con=Conexion.getConexion();
            con.setAutoCommit(false);

            pst=con.prepareStatement(sql);

            pst.setInt(1,id);

            pst.executeUpdate();

            con.commit();
            System.out.println("Receta borrada correctamente");

        }catch (SQLException e){
            System.out.println("SQL-ERROR: "+e.getMessage());
            try {
                if (con!=null){
                    con.rollback();
                    System.out.println("No se ha podido borrar la receta");
                }
            }catch (SQLException ex){
                System.out.println("SQL-ERROR: "+e.getMessage());
            }
        }finally {
            try {
                // Cierra la sentencia
                if (pst != null) {
                    pst.close();
                }
                // Cierra la conexión
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void buscarRecetaNombre() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduzca el nombre de la receta que desea buscar");
        String nombre = sc.next();

        String sql = "{CALL buscarReceta(?)}";

        try (Connection con = Conexion.getConexion()) {
            CallableStatement cstm = con.prepareCall(sql);
            cstm.setString(1, nombre);


            ResultSet rs = cstm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nombre");
                String categoria = rs.getString("categoria");
                int tiempoPreparacion = rs.getInt("tiempo_preparacion");
                String dificultad = rs.getString("dificultad");
                int raciones = rs.getInt("raciones");

                System.out.println(id + "-" + nom + "-" + categoria + "-" + tiempoPreparacion + "-" + dificultad + "-" + raciones);
            }
            System.out.println();


        } catch (SQLException e) {
            System.out.println("SQL-ERROR: "+e.getMessage());
        }
    }


}