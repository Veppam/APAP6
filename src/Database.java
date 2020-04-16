import java.sql.*;
import java.util.Vector;

public class Database {
    private String URL= "jdbc:" +
                        "mariadb://" +
                        "localhost:" + //Dirección IP
                        "3306/" + //Puerto
                        "apap6"; //Nombre base de datos
    private String USER="root";
    private String PASSWORD="";

    private Connection con;
    private Statement sent;

    public Database(){
        try {
            this.con= DriverManager.getConnection(URL,USER,PASSWORD);
            this.sent= con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet makeSqlCons(String sql){
        ResultSet respuesta = null;
        try {
            respuesta = sent.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error en consulta SQL");
        }
        return respuesta;
    }
    public String[][] getDBUsers(){
        String[] user1 = new String[2];
        String[] user2 = new String[2];

        String sql = "SELECT nombre_usuario, contrasenia FROM usuario";
        ResultSet res = makeSqlCons(sql);
        try {
            if(res.next()){
                user1[0]= res.getNString("nombre_usuario");
                user1[1]= res.getNString("contrasenia");
            }
            if(res.next()){
                user2[0]= res.getNString("nombre_usuario");
                user2[1]= res.getNString("contrasenia");
            }
        } catch (SQLException e) {
            System.out.println("Error en resultados");
        }

        return new String[][]{user1, user2};
    }

    public Vector getDBMaterials(){
        Vector materials= new Vector();
        String sql= "SELECT * FROM material";
        ResultSet res= makeSqlCons(sql);
        boolean next=true;
        while (next){
            try {
                if(res.next()) {
                    Vector nMat = new Vector();
                    nMat.addElement(res.getNString("id_material"));
                    nMat.addElement(res.getNString("nombre"));
                    nMat.addElement(res.getNString("cantidad"));
                    nMat.addElement(res.getNString("dir_imagen"));
                    materials.addElement(nMat);
                }else
                    next=false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return materials;
    }

    public boolean matchTrabNum(String TN){
        boolean exist= false;
        String sql= "SELECT nombre FROM profesor WHERE id_profesor="+TN;
        ResultSet res= makeSqlCons(sql);
        try {
            if(res.first())
                exist = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exist;
    }

    public String[] getDBprofessorData(String NT){
        String[] nombre= new String[3];

        String sql= "SELECT ap_paterno, ap_materno, nombre FROM profesor WHERE id_profesor= "+NT;
        ResultSet res= makeSqlCons(sql);

        try {
            res.first();
            nombre[0]= res.getNString("nombre");
            nombre[1]= res.getNString("ap_materno");
            nombre[2]= res.getNString("ap_paterno");
        } catch (SQLException e) {
            System.out.println("Error en consulta de nombre de profesor");
        }
        return nombre;
    }

    public Connection getConnection () {
        return con;
    }

}
