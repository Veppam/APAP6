import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Vector;

public class JPanelPrestamo extends JPanel {

    private JPanel titulo;
    private JScrollPane datosScroll;
    private JPanel datos;
    private JPanel botones;
    private JLabel lblNumProfesor;
    private JLabel lblMaterial;
    private JTextField txtNumProfesor;
    private JButton btnAgregarMat;
    private JButton btnAceptar;
    private JButton btnCancelar;


    private Database db = new Database();
    private ArrayList<Material> materialesDispon;
    private Profesor profesor;



    public JPanelPrestamo(JButton cancelar, Usuario usuario) {
        setLayout(new BorderLayout());
        titulo = new JPanel();
        titulo.setLayout(new FlowLayout());
        titulo.add(new JLabel("Registro de préstamo"));
        datos = new JPanel();
        datos.setLayout(new GridLayout(0,2));
        setBackground(Color.GREEN);
        lblNumProfesor = new JLabel("Número de trabajador:");
        txtNumProfesor = new JTextField("");
        datos.add(lblNumProfesor);
        datos.add(txtNumProfesor);
        lblMaterial = new JLabel("Materiales:");
        datos.add(lblMaterial);
        btnAgregarMat = new JButton("Agregar material");
        materialesDispon = getDBMatDispon();


        btnAgregarMat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> selectMaterial = new JComboBox<String>();
                Material m;
                for ( int x = 0; x < materialesDispon.size(); x++ ) {
                    m = materialesDispon.get(x);
                    if ( m.getCantDispon() > 0 )
                        selectMaterial.addItem( m.getNombre() );
                }
                datos.add(selectMaterial);
                JButton btnElim = new JButton("Eliminar");
                btnElim.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println( datos.getComponentCount() );
                        if ( datos.getComponentCount() > 6  ) {
                            datos.remove(datos.getComponentZOrder(btnElim)-1);
                            datos.remove(datos.getComponentZOrder(btnElim));
                            datos.updateUI();
                            datos.repaint();
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "El préstamo por lo menos debe" +
                                    " de tener un material.");
                        }
                    }
                });
                datos.add( btnElim  );
                datos.updateUI();
                datos.repaint();
            }
        });

        datos.add(btnAgregarMat);

        btnAgregarMat.doClick();
        botones = new JPanel();
        botones.setLayout(new FlowLayout());
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Material> materialesSeleccionados = new ArrayList<Material>();
                int select = 4;
                JComboBox<String> selectMaterial;
                while (select < datos.getComponentCount()) {
                    selectMaterial = (JComboBox<String>) datos.getComponent( select );
                    materialesSeleccionados.add(buscarMaterial(materialesDispon, (String) selectMaterial.getSelectedItem() ));
                    select += 2;
                }
                for ( int x  = 0; x < materialesSeleccionados.size(); x++ )
                    System.out.println(materialesSeleccionados.get(x).getCod());
                usuario.realizarPrestamo( buscarProfesor(Integer.parseInt(txtNumProfesor.getText())), materialesSeleccionados);
                btnCancelar.doClick();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar.doClick();
            }
        });

        add(titulo, BorderLayout.NORTH);
        datosScroll = new JScrollPane(datos);
        datosScroll.setSize(500,400);
        datos.setSize(500,400);
        add(datosScroll, BorderLayout.CENTER);
        botones.add(btnAceptar);
        botones.add(btnCancelar);
        add(botones, BorderLayout.SOUTH);


    }

    public ArrayList<Material> getDBMatDispon(){
        ResultSet res= db.makeSqlCons("SELECT id_material, nombre, cantidad FROM material");
        ResultSet res2;
        ArrayList<Material> m = new ArrayList<Material>();
        int cantPrest;
        try {
            while (res.next()){
                m.add( new Material( res.getNString("nombre"), res.getInt("id_material"), res.getInt("cantidad") ) );
                cantPrest = 0;
                res2 = db.makeSqlCons("SELECT cantidad FROM material_prestado where id_material = " + res.getNString("id_material"));
                while ( res2.next() ) {
                    cantPrest += res2.getInt("cantidad");
                }
                if ( cantPrest < res.getInt("cantidad") )
                    m.get( m.size() - 1 ).setCantDispon(m.get( m.size() - 1 ).getCantDispon() - cantPrest );
                else
                    m.remove( m.size() - 1 );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }



    public Material buscarMaterial ( ArrayList<Material> materiales, String nombreMaterial ) {
        for ( int x = 0; x < materiales.size(); x++ ) {
            if ( materiales.get(x).getNombre() == nombreMaterial ) {
                return materiales.get(x);
            }
        }
        return null;
    }

    public Profesor buscarProfesor (int numTrabajProf) {
        Profesor p = null;
        try {
            Statement sent = db.getConnection().createStatement();
            ResultSet res = sent.executeQuery("SELECT * FROM profesor WHERE id_profesor = " + numTrabajProf );
            if ( res.next() ) {
                p = new Profesor(res.getInt("id_profesor"), res.getNString("nombre"),
                        res.getNString("ap_paterno"), res.getNString("ap_materno"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

}
