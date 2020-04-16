public class Material {

    private int cod;
    private String nombre;
    private int cantDispon = 0;

    public Material ( String nombre, int cod, int cantDispon ) {
        this.nombre = nombre;
        this.cod = cod;
        this.cantDispon = cantDispon;
    }

    public int getCantDispon() {
        return cantDispon;
    }

    public int getCod() {
        return cod;
    }

    public String getNombre() {
        return nombre;
    }

    public void setCantDispon(int cantDispon) {
        this.cantDispon = cantDispon;
    }
}