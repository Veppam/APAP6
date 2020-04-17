import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class JPanelRealizarPrestamo {
    private JLabel Nombre;
    private JButton btnAgregarMat;
    private JFormattedTextField txtNumProfesor;
    private JPanel paneel;
    private JPanel datos;
    private JButton btnAceptar;
    private JButton btnCancelar;
    private Database db;
    GridBagConstraints gbc = new GridBagConstraints();
    private ArrayList<Material> materialesDispon;

    public JPanelRealizarPrestamo(Usuario usuario, JMenuItem cancelar) {
        //datos.setPreferredSize(new Dimension(250,400));
        db = new Database();
        materialesDispon = getDBMatDispon();
        paneel.setFont( new Font("Monospaced", Font.BOLD, 36) );
        datos.setFont( new Font("Monospaced", Font.BOLD, 30) );
        btnAgregarMat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( datos.getComponentCount() < 16 ) {
                    JComboBox<String> selectMaterial = new JComboBox<String>();
                    Material m;
                    for ( int x = 0; x < materialesDispon.size(); x++ ) {
                        m = materialesDispon.get(x);
                        if ( m.getCantDispon() > 0 )
                            selectMaterial.addItem( m.getNombre() );
                    }
                    JButton btnElim = new JButton("Eliminar");
                    btnElim.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println( datos.getComponentCount() );
                            if ( datos.getComponentCount() > 3  ) {
                                datos.remove(datos.getComponentZOrder(btnElim)-2);
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
                    agregarMat(selectMaterial, btnElim);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Se puede prestar mázimo 6 materiales " +
                            "a la vez. Si desea prestar más materiales al mismo profesor, favor de registrar este " +
                            "préstamo, y empezar un registrar un segundo. Ambos registros se unificarán.");
                }
                datos.updateUI();
                datos.repaint();
            }
        });

        btnAgregarMat.doClick();
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Material> materialesSeleccionados = new ArrayList<Material>();
                int select = 0;
                JComboBox<String> selectMaterial;
                while (select < datos.getComponentCount()) {
                    selectMaterial = (JComboBox<String>) datos.getComponent( select );
                    materialesSeleccionados.add(buscarMaterial(materialesDispon, (String) selectMaterial.getSelectedItem() ));
                    select += 3;
                }
                for ( int x  = 0; x < materialesSeleccionados.size(); x++ )
                    System.out.println(materialesSeleccionados.get(x).getCod());
                Profesor prof = buscarProfesor(Integer.parseInt(txtNumProfesor.getText()));
                if ( prof == null ) {
                JOptionPane.showMessageDialog(null, "El número de trabajador ingresado " +
                        "no existe. Inténtelo de nuevo.");
                }
                else{
                    usuario.realizarPrestamo( prof, materialesSeleccionados);
                    btnCancelar.doClick();
                }
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar.doClick();
            }
        });
    }

    public JPanel getPaneel() {
        return  paneel;
    }

    public void agregarMat( JComboBox<String> opciones, JButton eliminar ){
        gbc.gridx = 1;
        datos.add(opciones, gbc);
        gbc.gridx = 2;
        datos.add(new JLabel("           "), gbc);
        gbc.gridx = 3;
        datos.add(eliminar, gbc);
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
