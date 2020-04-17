public class Material {

    // Atributos de la clase Material
    private int cod;
    private String nombre;
    private int cantDispon = 0;

    // Constructor de la clase Material que recibe nombre, código y cantidad disponible del material
    public Material ( String nombre, int cod, int cantDispon ) {
        this.nombre = nombre;
        this.cod = cod;
        this.cantDispon = cantDispon;
    }

    // Regresa la cantidad disponible del material
    public int getCantDispon() {
        return cantDispon;
    }

    // Regresa el código del material
    public int getCod() {
        return cod;
    }

    // Regresa el nombre del material
    public String getNombre() {
        return nombre;
    }

    // Establece un nueva cantidad disponible del material
    public void setCantDispon(int cantDispon) {
        this.cantDispon = cantDispon;
    }
}