import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class Usuario {

    private String nomUsuario;
    private String password;
    private Database db;

    public Usuario(String nomUsuario, String password) {
        this.nomUsuario = nomUsuario;
        this.password = password;
        db = new Database();
    }

    public String getNomUsuario () {
        return nomUsuario;
    }

    public void realizarPrestamo(Profesor solicitante, ArrayList<Material> materialPrestado){
        try {
            Statement sent = db.getConnection().createStatement();
            ResultSet res = db.makeSqlCons("SELECT id_prestamo FROM prestamo WHERE id_profesor = " + solicitante.getNumTrabajador() + " AND fecha = '" + LocalDate.now() + "'");
            int exito = 1, idPrestamo;
            if ( !res.next() ){
                exito = sent.executeUpdate("INSERT INTO prestamo (id_profesor, fecha) VALUES (" + solicitante.getNumTrabajador() + ", '" + LocalDate.now() + "')");
                res = db.makeSqlCons("SELECT id_prestamo FROM prestamo WHERE id_profesor = " + solicitante.getNumTrabajador() + " AND fecha = '" + LocalDate.now() + "'");
                res.next();
            }
            if ( exito > 0 ) {
                idPrestamo = res.getInt("id_prestamo");
                int mat, cant;
                while ( materialPrestado.size() != 0 ){
                    mat = materialPrestado.get(0).getCod();
                    cant = 0;
                    for ( int y = 0; y < materialPrestado.size(); y++ ){
                        if ( materialPrestado.get(y).getCod() == mat ){
                            cant++;
                            materialPrestado.remove(y--);
                        }
                    }
                    res = db.makeSqlCons("SELECT cantidad FROM material_prestado WHERE id_prestamo = " + idPrestamo
                            + " AND id_material = " + mat + "");
                    if ( !res.next() ){
                        sent.executeUpdate("INSERT INTO material_prestado (id_prestamo, id_material, cantidad) VALUES ("
                                + idPrestamo + ", " + mat + ", " + cant + ")");
                    }
                    else{
                        sent.executeUpdate("UPDATE material_prestado SET cantidad = "
                                + (res.getInt("cantidad") + cant) + " WHERE id_prestamo = " + idPrestamo
                                + " AND id_material = " + mat);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}