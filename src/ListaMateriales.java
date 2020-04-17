import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class ListaMateriales extends JPanel {

    private Database DB= new Database();
    //private JPanel generalSpace = new JPanel();

    public ListaMateriales(){

        Vector materials = DB.getDBMaterials();
        int rows= materials.size();
        Vector<Integer> total=  (Vector<Integer>) DB.getDBCantidadMat();
        Vector<Integer> ocupados= (Vector<Integer>) DB.getMaterialOcc();

        //setLayout(new BorderLayout());
        //setLayout(new GridLayout(1,4));
        setLayout(new GridBagLayout());
        //generalSpace.setLayout(new GridBagLayout());
        JPanel C1 = new JPanel();
        JPanel C2 = new JPanel();
        JPanel C3 = new JPanel();
        JPanel C4 = new JPanel();
        C1.setLayout(new GridLayout(rows + 1, 1));
        C2.setLayout(new GridLayout(rows + 1, 1));
        C3.setLayout(new GridLayout(rows + 1, 1));
        C4.setLayout(new GridLayout(rows + 1, 1));

        C1.add(new JLabel());
        for (int i=0; i<materials.size();i++) {
            Vector mat= (Vector) materials.get(i);
            //String imgPath= (String) mat.get(3);
            String imgPath= "./img/img.jpg";
            JLabel img= new JLabel();
            img.setIcon(new ImageIcon(imgPath));
            C1.add(img);
        }
        C2.add(new JLabel());
        for(int i=0; i<materials.size();i++){
            Vector mat= (Vector) materials.get(i);
            String nombre= (String) mat.get(1);
            C2.add(new JLabel(nombre));
        }
        Vector <String> disponibles= new Vector<String>();
        for(int i=0; i<materials.size();i++){
            //System.out.println(String.valueOf(total.get(i)-ocupados.get(i))+", ");
            disponibles.add(String.valueOf(total.get(i)-ocupados.get(i)));
        }
        C3.add(new JLabel("Disponibles"));
        for(int i=0; i<materials.size();i++){
            String disp= disponibles.get(i);
            System.out.println(disp);
            C3.add(new JLabel(disp));
        }
        //new GridLayout(1,2,3,4);
        C4.add(new JLabel("En Prestamo"));
        for (int i=0; i<materials.size();i++) {
            C4.add(new JLabel(String.valueOf(ocupados.get(i))));
        }

        Insets inset = new Insets(0, 5, 0, 0);
        /*generalSpace.add(C1, new GridBagConstraints(
                0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST,
                GridBagConstraints.BOTH,
                inset, 0, 0
        ));
        generalSpace.add(C2, new GridBagConstraints(
                1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST,
                GridBagConstraints.BOTH,
                inset, 0, 0
        ));
        generalSpace.add(C3, new GridBagConstraints(
                2, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST,
                GridBagConstraints.BOTH,
                inset, 0, 0
        ));
        generalSpace.add(C4, new GridBagConstraints(
                3, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST,
                GridBagConstraints.BOTH,
                inset, 0, 0
        ));*/

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


        /*add(C1);
        add(C2);
        add(C3);
        add(C4);*/
        //add(generalSpace);

        /*generalSpace.add(C1);
        generalSpace.add(C2);
        generalSpace.add(C3);
        generalSpace.add(C4);
        add(generalSpace);*/

    }

}
