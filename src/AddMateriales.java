import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AddMateriales extends JPanel{
    private JLabel matL;
    private JLabel cantL;
    private JLabel idL;
    private JLabel chusL;
    private JTextField id;
    private JTextField mat;
    private JSpinner cant;
    private JButton send;
    private JButton siChus;
    private JPanel panela;
    private JLabel t;
    private JFileChooser chusma;

    private Usuario userr;
    private File olFile;
    private int yeahChus;

    //Constructor de AddMateriales, pide el JPanel que contiene a AddMateriales, el usuario que intenta agregar un material y el numero de serie del Material
    //En caso de que el numero de serie sea provisto, este no se pedira en el formulario para agregar un material
    public AddMateriales(JPanel contenedor, Usuario user, Material datos){
      
        //Database db = new Database();
        userr = user;
        chusma = new JFileChooser();
        //Filtro para imagenes JFileChooser
        FileNameExtensionFilter soloImgs = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        chusma.addChoosableFileFilter(soloImgs);
        chusma.setAcceptAllFileFilterUsed(false);
        cant.setValue(1);
        //-------FORM DATOS

        //Si se proporciono un numero de serie este no sera solicitado nuevamente
        if (datos != null) {
            t.setText("Modificar material");
            mat.setText(datos.getNombre());
            cant.setValue(datos.getCantDispon());
            id.setText(String.valueOf(datos.getCod()));
            id.setEnabled(false);
        }

        add (panela);
        setVisible(true);

        //Guarda variable para copiar archivo
        siChus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yeahChus = chusma.showSaveDialog(getParent());

            }
        });
        //-------------REGRESA A MENÚ (FALTA)
        //SALVA EN BD
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String idT;

                //Si el numero de serie fue proporcionado como parametro del constructor este se le asigna como valor a idt
                if (datos == null)
                    idT = id.getText();
                else
                    idT = String.valueOf(datos.getCod());

                String sT = mat.getText();
                //Revisa que
                if (sT.equals("") == false && idT.matches("[0-9]{1,3}") == true){
                    int cT = (int) cant.getValue();
                    int idI = Integer.parseInt(idT);

                    if (cT <= 0 || cT > 999){
                        JOptionPane.showConfirmDialog(null, "Mínimo 1, máximo 999 materiales","ERROR", JOptionPane.OK_CANCEL_OPTION);
                    }else{
                        if (yeahChus == JFileChooser.APPROVE_OPTION) {
                            olFile = chusma.getSelectedFile();
                            if (olFile != null){
                                Path to = Paths.get("./img/"+olFile.getName());
                                System.out.println("Save as file: " + olFile.getAbsolutePath() + olFile.getName());
                                try {
                                    Files.copy(olFile.toPath(), to);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }

                        if (olFile != null){
                            System.out.println("Yeah");

                            //Si el numero de serie fue proporcionado como parametro del constructor se realiza una modificacion de material en vez de agregar uno nuevo
                            if (datos == null)
                                userr.agregarMaterial(idI, sT, cT, "./img/"+olFile.getName());
                            else
                                userr.modificarMaterial(new Material( sT, idI, cT));

                        } else {
                            if (datos == null)
                                userr.agregarMaterial(idI, sT, cT, "./imgs/controlProy.png");
                            else
                                userr.modificarMaterial(new Material( sT, idI, cT));
                        }
                        //Muestra la tabla de materiales en el inventario
                        contenedor.removeAll();
                        contenedor.updateUI();
                        contenedor.repaint();
                        contenedor.setLayout(new BorderLayout());
                        contenedor.add(new ModificarMaterial(contenedor, user), BorderLayout.CENTER);
                    }
                }else{
                    JOptionPane.showConfirmDialog(null, "Tus datos están incorrectos (N° Serie = número de 3 dígitos)",
                            "ERROR", JOptionPane.OK_CANCEL_OPTION);
                }
            }
        });
    }

    public JPanel getPanela(){
        return panela;
    }
}
