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

public class AddMateriales extends JPanel {
    private JPanel panela;
    private JTextField id;
    private JSpinner cant;
    private JButton siChus;
    private JTextField mat;
    private JButton send;
    private JFileChooser chusma;

    private Usuario userr;
    private File olFile;
    private int yeahChus;

    //Constructor de AddMateriales, pide el JPanel que contiene a AddMateriales, el usuario que intenta agregar un material y el numero de serie del Material
    //En caso de que el numero de serie sea provisto, este no se pedira en el formulario para agregar un material
    public AddMateriales(JPanel contenedor, Usuario user, String noSerie) {
        chusma = new JFileChooser();
        userr = user;
        cant.setValue(1);
        //Si existe un ID, DESACTIVA JTextField que lo edita
        if (noSerie.equals("") == false){
            id.setEditable(false);
            id.setEnabled(false);
            id.setText(noSerie);
        }

        //Filtro para imagenes JFileChooser
        FileNameExtensionFilter soloImgs = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        chusma.addChoosableFileFilter(soloImgs);
        chusma.setAcceptAllFileFilterUsed(false);

        add(panela);
        setSize(600, 400);
        ImageIcon img = new ImageIcon("./img/icono.png");
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
                if (noSerie.isEmpty())
                    idT = id.getText();
                else
                    idT = noSerie;

                String sT = mat.getText();
                //Revisa que
                if (sT.equals("") == false && idT.matches("[0-9]{1,3}") == true) {
                    int cT = (int) cant.getValue();
                    int idI = Integer.parseInt(idT);

                    if (cT <= 0 || cT > 999) {
                        JOptionPane.showConfirmDialog(null, "Mínimo 1, máximo 999 materiales", "ERROR", JOptionPane.OK_CANCEL_OPTION);
                    } else {
                        if (yeahChus == JFileChooser.APPROVE_OPTION) {
                            olFile = chusma.getSelectedFile();
                            if (olFile != null) {
                                Path to = Paths.get("./img/" + olFile.getName());
                                System.out.println("Save as file: " + olFile.getAbsolutePath() + olFile.getName());
                                try {
                                    Files.copy(olFile.toPath(), to);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }

                        if (olFile != null) {
                            System.out.println("Yeah");

                            //Si el numero de serie fue proporcionado como parametro del constructor se realiza una modificacion de material en vez de agregar uno nuevo
                            if (noSerie.isEmpty())
                                userr.agregarMaterial(idI, sT, cT, "./img/" + olFile.getName());
                            else
                                userr.modificarMaterial(new Material(sT, idI, cT));

                        } else {
                            if (noSerie.isEmpty())
                                userr.agregarMaterial(idI, sT, cT, "./imgs/controlProy.png");
                            else
                                userr.modificarMaterial(new Material(sT, idI, cT));
                        }
                        //Muestra la tabla de materiales en el inventario
                        contenedor.removeAll();
                        contenedor.updateUI();
                        contenedor.repaint();
                        contenedor.setLayout(new BorderLayout());
                        contenedor.add(new ModificarMaterial(contenedor, user), BorderLayout.CENTER);
                    }
                } else {
                    JOptionPane.showConfirmDialog(null, "Tus datos están incorrectos (N° Serie son puros números)",
                            "ERROR", JOptionPane.OK_CANCEL_OPTION);
                }
            }
        });
    }

    public JPanel getPanela(){ return panela;}
}

