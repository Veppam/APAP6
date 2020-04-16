public class Material {

    private int noSerie;
    private int cantidad;
    private String nombre;
    private String imagen;
    private String descripcion;

    public Material(int noSerie, int cantidad, String nombre, String descripcion, String imagen) {

        this.noSerie = noSerie;
        this.cantidad = cantidad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;

    }

    public Material(int noSerie, int cantidad, String nombre, String descripcion) {

        this.noSerie = noSerie;
        this.cantidad = cantidad;
        this.nombre = nombre;
        this.descripcion = descripcion;

    }

    public boolean verDisponibilidad(){
        return true;
    }

}
