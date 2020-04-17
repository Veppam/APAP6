import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class ListaMateriales extends JPanel {

    private Database DB= new Database();
    public ListaMateriales(){

        Vector materials = DB.getDBMaterials(); //Obtiene los materiales en la base de datos
        int rows= materials.size(); //El número de columnas necesarias para cada material
        Vector<Integer> total=  (Vector<Integer>) DB.getDBCantidadMat(); //La cantidad total de cada material
        Vector<Integer> ocupados= (Vector<Integer>) DB.getMaterialOcc(); //La cantidad de material ocupado

        setLayout(new GridBagLayout());
        JPanel C1 = new JPanel(); //Imagen del material
        JPanel C2 = new JPanel(); //Nombre del material
        JPanel C3 = new JPanel(); //Cantidad disponible
        JPanel C4 = new JPanel(); //Cantidad ocupada
        C1.setLayout(new GridLayout(rows + 1, 1)); //Cada grid Layout es para colocar los datos como tabla
        C2.setLayout(new GridLayout(rows + 1, 1));
        C3.setLayout(new GridLayout(rows + 1, 1));
        C4.setLayout(new GridLayout(rows + 1, 1));

        C1.add(new JLabel());

        for (int i=0; i<materials.size();i++) { //Añadir la imagen a un JLabel para poner en el gridLayout
            Vector mat= (Vector) materials.get(i);
            //String imgPath= (String) mat.get(3);
            String imgPath= "./img/img.jpg"; //Añadir imagen default, comentar esta línea y descomentar la anterior para el caso de imagen existente
            JLabel img= new JLabel();
            img.setIcon(new ImageIcon(imgPath));
            C1.add(img);
        }
      
        C2.add(new JLabel()); //Añadir el nombre del material en un JLabel para añadir al GridLayout
        for(int i=0; i<materials.size();i++){
            Vector mat= (Vector) materials.get(i);
            String nombre= (String) mat.get(1);
            C2.add(new JLabel(nombre));
        }
        Vector <String> disponibles= new Vector<String>();
        for(int i=0; i<materials.size();i++){ //Obtener el número de materiales disponibles
            //System.out.println(String.valueOf(total.get(i)-ocupados.get(i))+", ");
            disponibles.add(String.valueOf(total.get(i)-ocupados.get(i)));
        }
        C3.add(new JLabel("Disponibles"));
        for(int i=0; i<materials.size();i++){ //Insertar el número de materiales disponibles en un JLabel para el GridLayout
            String disp= disponibles.get(i);
            System.out.println(disp);
            C3.add(new JLabel(disp));
        }
        C4.add(new JLabel("En Prestamo")); //Insertar la cantidad de material prestado en Un JLabel para el GridLayout
        for (int i=0; i<materials.size();i++) {
            C4.add(new JLabel(String.valueOf(ocupados.get(i))));
        }

        Insets inset = new Insets(0, 5, 0, 0);
      
        add(C1, new GridBagConstraints(
                0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST,
                GridBagConstraints.BOTH,
                inset, 0, 0
        ));
        add(C2, new GridBagConstraints(
                1, 0, 2, 1, 1.0, 1.0,
                GridBagConstraints.EAST,
                GridBagConstraints.BOTH,
                inset, 0, 0
        ));
        add(C3, new GridBagConstraints(
                3, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST,
                GridBagConstraints.BOTH,
                inset, 0, 0
        ));
        add(C4, new GridBagConstraints(
                4, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST,
                GridBagConstraints.BOTH,
                inset, 0, 0
        ));
      
    }
  
}
