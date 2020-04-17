import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {
    private JButton btnMateriales;
    private JButton btnPrestamo;
    private JButton btnInicio;
    private JButton creditos;
    private JPanel principal;
    private JPanel contenedor;
    private JPanel opciones;
    private JPanel btnsOpciones;
    private JLabel txtEncabezado;
    private JPanel aux;
    private JMenuBar menu;
    private JMenu menuPrestamos;
    private JMenu menuMateriales;
    private JMenuItem itemConsultMat;
    private JMenuItem itemAgregarMat;
    private JMenuItem itemConsultPrest;
    private JMenuItem itemHacerPrest;
    private JButton btnCerrarSesion;
    JFrame cred;

    private Usuario usuario;

    public MenuPrincipal( Usuario us){
        cred = null;
        usuario = us;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("APA6");
        setVisible(true);
        setSize(1200,700);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1,1));
        principal = new JPanel();
        principal.setBackground(Color.LIGHT_GRAY);
        principal.setLayout(new GridBagLayout());
        opciones = new JPanel();
        opciones.setLayout( new GridLayout(6,1) );
        btnInicio = new JButton("Consultar préstamos");
        btnPrestamo = new JButton("Hacer préstamos");
        btnMateriales = new JButton("Materiales");
        btnInicio.setPreferredSize(new Dimension(30,15));
        btnPrestamo.setPreferredSize(new Dimension(30,15));
        btnMateriales.setPreferredSize(new Dimension(30,15));




        txtEncabezado = new JLabel("");
        txtEncabezado.setIcon( new ImageIcon( "img/Icono.png" ) );
       // opciones.add(txtEncabezado);
        //opciones.add(new JLabel("Bienvenido \n" + usuario.getNomUsuario()));
        //opciones.add(new JLabel(""));
        //opciones.add(btnInicio);
        //opciones.add(btnPrestamo);
        //opciones.add(btnMateriales);
        //principal.add(opciones, BorderLayout.EAST);
        contenedor = new JPanel();
        contenedor.setLayout( new FlowLayout(FlowLayout.CENTER) );
        contenedor.setBackground(Color.DARK_GRAY);
        //principal.add(contenedor, BorderLayout.CENTER);
        menu = new JMenuBar();
        menuPrestamos = new JMenu("Préstamos",true);
        menuMateriales = new JMenu("Materiales", true);
        itemConsultMat = new JMenuItem("Consultar",'B');
        itemAgregarMat = new JMenuItem("Agregar",'F');
        itemConsultPrest = new JMenuItem("Ver",'H');
        itemHacerPrest = new JMenuItem("Hacer",'K');

        menuPrestamos.add( itemHacerPrest );
        menuPrestamos.add( itemConsultPrest );
        menuMateriales.add( itemAgregarMat );
        menuMateriales.add( itemConsultMat );
        btnCerrarSesion = new JButton("Cerrar sesión");

        menu.setOpaque(false);
        menu.setBorderPainted(false);
        menu.setLayout(new GridBagLayout());
        menu.add(menuPrestamos, new GridBagConstraints(0,0,1,1,1.0,1,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0 ));
        menu.add(menuMateriales, new GridBagConstraints(1,0,1,1,1.0,1,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0 ));

        itemConsultPrest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contenedor.removeAll();
                contenedor.updateUI();
                contenedor.repaint();
                contenedor.setLayout(new BorderLayout());
                aux = new JPanelConsultaPrestamos(usuario );
                aux.setMaximumSize(new Dimension(300, 400));
                contenedor.add( aux, BorderLayout.CENTER);
                System.out.println("ENTRÉ");
            }
        });

        creditos = new JButton("Creditos");

        principal.add(menu, new GridBagConstraints(0,0,5,1,1.0,0.5,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 400, 40));
        principal.add(contenedor, new GridBagConstraints(0,1,5,10,6.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
        principal.add(txtEncabezado, new GridBagConstraints(6,1,1,2,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));
        principal.add(new JLabel("Administración de Préstamos de Audovisuales P6"), new GridBagConstraints(6,0,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));
        principal.add(new JLabel("Bienvenido \n" + usuario.getNomUsuario()), new GridBagConstraints(6,5,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));
        principal.add(btnCerrarSesion, new GridBagConstraints(6,7,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 80, 20));
        principal.add(creditos, new GridBagConstraints(6,9,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));

        itemHacerPrest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contenedor.removeAll();
                contenedor.updateUI();
                contenedor.repaint();
                contenedor.setLayout(new BorderLayout());


                JPanelRealizarPrestamo p = new JPanelRealizarPrestamo(usuario, itemConsultPrest);
                contenedor.add( p.getPaneel()/*new JPanelPrestamo( btnInicio, usuario )*/ , BorderLayout.CENTER );
            }
        });

        add(principal);
        itemHacerPrest.doClick();

        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( cred != null && cred.isVisible() ){
                    cred.setVisible(false);
                    cred.dispose();
                }
                setVisible(false);
                dispose();
                new LogIn("LogIn");
            }
        });

        creditos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cred = new JFrame("Créditos");
                cred.setSize(500, 600);
                cred.setResizable(false);
                cred.setLocationRelativeTo(null);
                cred.setLayout( new GridLayout(0,1) );
                JPanel credPan = new JPanel();
                credPan.setLayout( new GridBagLayout() );
                JLabel logo = new JLabel();
                logo.setIcon( new ImageIcon("img/Icono.png" ) );
                credPan.add(new JLabel("       "), new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 100, 100));
                credPan.add(new JLabel("       "), new GridBagConstraints(1,0,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 100, 100));
                credPan.add(new JLabel("       "), new GridBagConstraints(2,0,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 100, 100));
                credPan.add(new JLabel("       "), new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 100, 100));
                credPan.add(new JLabel("       "), new GridBagConstraints(1,2,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 100, 100));
                credPan.add(logo, new GridBagConstraints(1,3,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 100, 100));
                credPan.add(new JLabel("       "), new GridBagConstraints(1,4,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 100, 100));
                credPan.add(new JLabel("       "), new GridBagConstraints(1,5,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 100, 100));
                credPan.add(new JLabel("       "), new GridBagConstraints(1,6,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 100, 100));
                credPan.add(new JLabel("Camacho Barrientos Juan Carlos"), new GridBagConstraints(1,7,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 100, 100));
                credPan.add(new JLabel("López Chong Jorge Antonio"), new GridBagConstraints(1,8,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 100, 100));
                credPan.add(new JLabel("List Manuel"), new GridBagConstraints(1,9,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 100, 100));
                credPan.add(new JLabel("Sebas"), new GridBagConstraints(1,10,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 100, 100));
                credPan.add(new JLabel("Hernández"), new GridBagConstraints(1,11,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 100, 100));
                cred.add(credPan);
                cred.setVisible(true);
            }
        });





    }


}
