import java.sql.*;
import java.util.Vector;

public class Database {
    private String URL= "jdbc:" +
                        "mariadb://" +
                        "132.248.96.65:" + //Dirección IP
                        "3306/" + //Puerto
                        "mod8_1_apa"; //Nombre base de datos
    private String USER="ete62a";
    private String PASSWORD="etemysql";

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

    public ResultSet makeSqlCons(String sql){ //Metodo para realizar consultas
        ResultSet respuesta = null;
        try {
            respuesta = sent.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error en consulta SQL");
            System.out.println(e);
        }
        return respuesta;
    }
    public void exeSql(String sql){ //Metodo para ejecutar inserciones, actualizaciones o borrados
        try {
            sent.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println("Error en ejecución SQL");
        }
    }
    public String[][] getDBUsers(){ //Obtiene los usarios de la base
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

    public Vector getDBMaterials(){ //Obtiene toda la info de los materiales
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

    public boolean matchTrabNum(String TN){ //Valida si el no ingresado pertenece al id de un profesor
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

    public String[] getDBprofessorData(String NT){ //DEc¿vuelve toda la informacion de un profesor
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

    public boolean valUsuario(String nom, String contra){ //Valida al usuario
        String[] usuario1 = getDBUsers()[0];
        String[] usuario2 = getDBUsers()[1];

        if (nom.equals(usuario1[0]) && contra.equals(usuario1[1]))
            return true;
        if (nom.equals(usuario2[0]) && contra.equals(usuario2[1]))
            return  true;
        return false;
    }
    public Vector getMaterialOcc(){ //Devuelve los materiales ocupados
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

    public Vector getDBCantidadMat(){ //Devuelve el total de materiales disponibles
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

    public Connection getConnection () {
        return con;
    }

}
