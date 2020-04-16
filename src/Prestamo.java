import java.time.LocalDate;
import java.util.ArrayList;

public class Prestamo {

    private Profesor profesor;
    private ArrayList<Material> materiales;
    private LocalDate fecha;

    public Prestamo ( Profesor profesor, ArrayList<Material> materiales ) {
        this.profesor = profesor;
        this.materiales = materiales;
        fecha = LocalDate.now();
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public ArrayList<Material> getMateriales() {
        return materiales;
    }

}
