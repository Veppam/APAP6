import javax.swing.*;
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

    public void realizarPrestamo(Profesor solicitante, ArrayList<Material> materialPrestado, ArrayList<Integer> cuantosDeCadaUno ){
        try {
            Statement sent = db.getConnection().createStatement();
            // Consulta si hay un préstamo vigente del día de hoy
            ResultSet res = db.makeSqlCons("SELECT id_prestamo FROM prestamo WHERE id_profesor = " + solicitante.getNumTrabajador() + " AND fecha = '" + LocalDate.now() + "'");
            int exito = 1, idPrestamo;
            // Si no hay un préstamo del día de hoy, se hace uno
            if ( !res.next() ){
                exito = sent.executeUpdate("INSERT INTO prestamo (id_profesor, fecha) VALUES (" + solicitante.getNumTrabajador() + ", '" + LocalDate.now() + "')");
                res = db.makeSqlCons("SELECT id_prestamo FROM prestamo WHERE id_profesor = " + solicitante.getNumTrabajador() + " AND fecha = '" + LocalDate.now() + "'");
                res.next();
            }
            // Si se lleva a cabo correctamente la inserción o si ya había un préstamo vigente
            if ( exito > 0 ) {
                idPrestamo = res.getInt("id_prestamo");
                int mat, cant;
                // Recorre el ArrayList de los material a prestar
                while ( materialPrestado.size() != 0 ){
                    // Si hay un prétamo del día de hoy con el material, se le aumenta la cantidad, de lo contrario se creará un registro nuevo del material
                    // y su cantidad
                    res = db.makeSqlCons("SELECT cantidad FROM material_prestado WHERE id_prestamo = " + idPrestamo
                            + " AND id_material = " + materialPrestado.get(0).getCod() + "");
                    if ( !res.next() ){
                        sent.executeUpdate("INSERT INTO material_prestado (id_prestamo, id_material, cantidad) VALUES ("
                                + idPrestamo + ", " + materialPrestado.get(0).getCod() + ", " + cuantosDeCadaUno.get(0) + ")");
                    }
                    else{
                        sent.executeUpdate("UPDATE material_prestado SET cantidad = "
                                + (res.getInt("cantidad") + cuantosDeCadaUno.get(0) ) + " WHERE id_prestamo = "
                                + idPrestamo + " AND id_material = " + materialPrestado.get(0).getCod());
                    }
                    // Quito el material y su cantidad para seguir con el siguiente
                    materialPrestado.remove(0);
                    cuantosDeCadaUno.remove(0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void agregarMaterial(int No_Serie, String nombre, int Cantidad, String newFile){
        try{
            Statement inset = db.getConnection().createStatement();
            int in = inset.executeUpdate("INSERT INTO material VALUES ("
                    + No_Serie + ", \""+nombre+"\", " + Cantidad + ", \""+newFile+"\")");
            JOptionPane.showConfirmDialog(null, "Adición Exitosa",
                    "ERROR", JOptionPane.OK_CANCEL_OPTION);
        }catch (SQLException e){
            JOptionPane.showConfirmDialog(null, "Error en la adición",
                    "ERROR", JOptionPane.OK_CANCEL_OPTION);
        }
    }

    public void modificarMaterial(Material materialModificado){

        db.exeSql("UPDATE material SET nombre = '" + materialModificado.getNombre() +
                  "', cantidad = '" + materialModificado.getCantDispon() +
                  "' WHERE id_material = " + materialModificado.getCod() + ";");
        JOptionPane.showConfirmDialog(null, "Modificación Exitosa",
                "EXITO", JOptionPane.OK_CANCEL_OPTION);
    }

    public void eliminarMaterial(int noSerie){
        db.exeSql("UPDATE material_prestado SET id_material = null WHERE id_material = " + noSerie + ";");
        db.exeSql("DELETE FROM material WHERE id_material = " + noSerie + ";");
        JOptionPane.showConfirmDialog(null, "Eliminación Exitosa",
                "EXITO", JOptionPane.OK_CANCEL_OPTION);
    }

}