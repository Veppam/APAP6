import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {
    private JButton btnMateriales;
    private JButton btnPrestamo;
    private JButton btnInicio;
    private JPanel principal;
    private JPanel contenedor;
    private JPanel opciones;
    private JPanel btnsOpciones;
    private JLabel txtEncabezado;
    private JPanel aux;

    private Usuario usuario;

    public MenuPrincipal( Usuario us){
        usuario = us;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("APA6");
        setVisible(true);
        setSize(900,700);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        principal = new JPanel();
        principal.setLayout(new BorderLayout());
        opciones = new JPanel();
        opciones.setLayout( new GridLayout(2,1) );
        btnInicio = new JButton("Consultar préstamos");
        btnPrestamo = new JButton("Hacer préstamos");
        btnMateriales = new JButton("Inventario");
        btnInicio.setPreferredSize(new Dimension(30,15));
        btnPrestamo.setPreferredSize(new Dimension(30,15));
        btnMateriales.setPreferredSize(new Dimension(30,15));

        btnInicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contenedor.removeAll();
                contenedor.updateUI();
                contenedor.repaint();
                contenedor.setLayout(new BorderLayout());
                aux = new JPanelConsultaPrestamos(usuario);
                contenedor.add( aux, BorderLayout.CENTER );
                System.out.println("ENTRe");
            }
        });

        btnPrestamo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contenedor.removeAll();
                contenedor.updateUI();
                contenedor.repaint();
                contenedor.setLayout(new BorderLayout());
                contenedor.add( new JPanelPrestamo( btnInicio, usuario ), BorderLayout.CENTER );
            }
        });
        txtEncabezado = new JLabel("Administración de préstamos audiovisuales");
        btnsOpciones = new JPanel();
        btnsOpciones.setLayout(new GridLayout(4,1));
        btnsOpciones.add(btnInicio, BorderLayout.WEST);
        btnsOpciones.add(btnPrestamo, BorderLayout.CENTER);
        btnsOpciones.add(btnMateriales, BorderLayout.EAST);
        opciones.add(txtEncabezado);
        opciones.add(btnsOpciones);
        principal.add(opciones, BorderLayout.EAST);
        contenedor = new JPanel();
        contenedor.setLayout( new FlowLayout(FlowLayout.CENTER) );
        principal.add(contenedor, BorderLayout.CENTER);
        add(principal, BorderLayout.CENTER);
        btnInicio.doClick();
    }
}
