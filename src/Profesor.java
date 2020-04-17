public class Profesor {

    // Atributos de la calse Profesor
    private int numTrabajador;
    private String nombre;
    private String apPaterno;
    private String apMaterno;

    // Constructor de la clase Profesor
    public Profesor ( int numTrab, String nom, String apPat, String apMat ) {
        numTrabajador = numTrab;
        nombre = nom;
        apPaterno = apPat;
        apMaterno = apMat;
    }

    // Regresa el número de trabajador del profesor
    public int getNumTrabajador() {
        return numTrabajador;
    }

    // Regresa el nombre del profesor
    public String getNombre() {
        return nombre;
    }

    // Regresa el apellido paterno del profesor
    public String getApPaterno() {
        return apPaterno;
    }

    // Regresa el apellido materno del profesor
    public String getApMaterno() {
        return apMaterno;
    }

}