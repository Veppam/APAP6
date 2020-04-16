public class Profesor {

    private int numTrabajador;
    private String nombre;
    private String apPaterno;
    private String apMaterno;

    public Profesor ( int numTrab, String nom, String apPat, String apMat ) {
        numTrabajador = numTrab;
        nombre = nom;
        apPaterno = apPat;
        apMaterno = apMat;
    }

    public int getNumTrabajador() {
        return numTrabajador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

}
