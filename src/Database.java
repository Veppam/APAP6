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
    public void exeSql(String sql){
        try {
            sent.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println("Error en ejecución SQL");
        }
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

    public boolean valUsuario(String nom, String contra){
        String[] usuario1 = getDBUsers()[0];
        String[] usuario2 = getDBUsers()[1];

        if (nom.equals(usuario1[0]) && contra.equals(usuario1[1]))
            return true;
        if (nom.equals(usuario2[0]) && contra.equals(usuario2[1]))
            return  true;
        return false;
    }
    public Vector getMaterialOcc(){
        String subSql= "SELECT id_material FROM material";
        ResultSet res= makeSqlCons(subSql);
        Vector <Integer> ocupados= new Vector<Integer>();
        Vector <String> mats= new Vector();
        boolean fin=false;
        try {
            while (!fin) {
                if(res.next()) {
                    mats.add(res.getNString("id_material"));
                }else{
                    fin=true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(String id:mats) {
            String sql = "SELECT cantidad FROM material_prestado WHERE id_material=" + id;
            ResultSet result= makeSqlCons(sql);
            //Vector <String> numsOcup= new Vector <String>();
            //Sumar los resultados de cada préstamo
            int num=0;
            boolean end= false;
            while (!end){
                try {
                    if(result.next()){
                        num+=result.getInt("cantidad");
                    }else{
                        end=true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            ocupados.add(num);
        }
        return ocupados;
    }

    public Vector getDBCantidadMat(){
        Vector<Integer> total= new Vector<Integer>();
        String sql= "SELECT cantidad FROM material ";
        ResultSet res= makeSqlCons(sql);
        boolean end=false;
        while (!end){
            try {
                if(res.next()){
                    total.add(res.getInt("cantidad"));
                }else{
                    end=true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    //Modifica un material en la base de datos conforme al material recibido como parametro
    public void modificarMaterial(Material materialModificado){
        try {
            Statement modificacion = con.createStatement();
            modificacion.executeUpdate("UPDATE material SET nombre = " + materialModificado.getNombre() +
                                        ", cantidad = " + materialModificado.getCantDispon() +
                                        " WHERE id_material = " + materialModificado.getCod() + ";");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection () {
        return con;
    }

}
