import java.util.ArrayList;

public class Usuario {

    private String nomUsuario;
    private String password;

    public Usuario(String nomUsuario, String password) {
        this.nomUsuario = nomUsuario;
        this.password = password;
    }

    public Material consultarMaterial(int noSerie){
        Material resp = null;
        return resp;
    }

    public void agregarMaterial(int noSerie, int cantidad, String nombre, String descripcion){
        Material registro  = new Material(noSerie, cantidad, nombre, descripcion);
    }

    public void agregarMaterial(int noSerie, int cantidad, String nombre, String descripcion, String dirImg){
        Material registro  = new Material(noSerie, cantidad, nombre, descripcion, dirImg);
    }

    public void eliminarMaterial(int noSerie){

    }

    public void modificarMaterial(Material materialModificado){

    }

    public void realizarPrestamo(Profesor solicitante, ArrayList materialPrestado){

    }

    public void finalizarPrestamo(){}

}
