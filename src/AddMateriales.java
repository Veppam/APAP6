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

public class AddMateriales extends JFrame{
    private JLabel matL;
    private JLabel cantL;
    private JLabel idL;
    private JLabel chusL;
    private JTextField id;
    private JTextField mat;
    private JSpinner cant;
    private JButton send;
    private JButton menu;
    private JButton siChus;
    private JPanel panela;
    private JFileChooser chusma;

    private Usuario user;
    private File olFile;
    private int yeahChus;

    public AddMateriales(){
        super ("Adición de materiales");
        //Database db = new Database();
        //----------------------FALATA CONFIGURAR ACCESO DE USUARIO POR CONTRASEÑA
        user = new Usuario ("Yo", "aiwei");
        matL = new JLabel("Nombre:");
        mat = new JTextField(20);
        cantL = new JLabel("Cantidad:");
        cant = new JSpinner();
        id = new JTextField(20);
        idL = new JLabel("N° serie:");
        send = new JButton("Enviar");
        menu = new JButton("Menú");
        panela = new JPanel(new GridBagLayout());
        chusL = new JLabel ("Seleccionar imagen (opcional):");
        siChus = new JButton("Agregar");
        chusma = new JFileChooser();
        //Filtro para imagenes JFileChooser
        FileNameExtensionFilter soloImgs = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        chusma.addChoosableFileFilter(soloImgs);
        chusma.setAcceptAllFileFilterUsed(false);
        //
        cant.setPreferredSize(new Dimension(100, 25));
        cant.setValue(1);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //-------FORM DATOS
        c.gridx = 0;
        c.gridy = 0;
        panela.add(idL, c);

        c.gridx = 1;
        c.gridy = 0;
        panela.add (id, c);

        c.gridx = 0;
        c.gridy = 1;
        panela.add(matL, c);

        c.gridx = 1;
        c.gridy = 1;
        panela.add (mat, c);

        c.gridx = 0;
        c.gridy = 2;
        panela.add(cantL, c);

        c.gridx = 1;
        c.gridy = 2;
        panela.add(cant, c);

        //-----AGREGAR FORM y BUTTONS
        c.gridy = 1;
        c.gridx = 2;
        c.weighty = 0.1;
        add(panela, c);

        c.gridx = 1;
        c.gridy = 3;
        add(chusL, c);

        c.gridx = 2;
        c.gridy = 3;
        add(siChus, c);

        c.gridx = 1;
        c.gridy = 4;
        c.weighty = 0.1;
        c.anchor = GridBagConstraints.PAGE_END;
        add(menu, c);

        c.gridx = 3;
        c.gridy = 4;
        c.weighty = 0.1;
        c.anchor = GridBagConstraints.PAGE_END;
        add(send, c);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
        //Guarda variable para copiar archivo
        siChus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yeahChus = chusma.showSaveDialog(getParent());

            }
        });
        //-------------REGRESA A MENÚ (FALTA)
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println ("Return");
            }
        });
        //SALVA EN BD
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idT = id.getText();
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
                            Path to = Paths.get("./img/"+olFile.getName());
                            System.out.println("Save as file: " + olFile.getAbsolutePath() + olFile.getName());
                            try {
                                Files.copy(olFile.toPath(), to);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }

                        if (olFile.exists() == true){
                            System.out.println("Yeah");
                            user.agregarMaterial(idI, sT, cT, "./img/"+olFile.getName());
                        }else user.agregarMaterial(idI, sT, cT, "./imgs/controlProy.png");


                    }
                }else{
                    JOptionPane.showConfirmDialog(null, "Tus datos están incorrectos (N° Serie son puros números)",
                            "ERROR", JOptionPane.OK_CANCEL_OPTION);
                }
            }
        });
    }
}