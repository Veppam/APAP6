import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class JPanelRealizarPrestamo {
    // Declaración de componentes de la interfaz
    private JLabel Nombre;
    private JButton btnAgregarMat;
    private JFormattedTextField txtNumProfesor;
    private JPanel paneel;
    private JPanel datos;
    private JButton btnAceptar;
    private JButton btnCancelar;

    // Objeto que me ayudará con la comunicación con la base de datos
    private Database db;
    // Me ayudará para poner los JComboBox de acuerdo al layout GridBagLayout
    GridBagConstraints gbc = new GridBagConstraints();
    // Materiales disponibles según la BD
    private ArrayList<Material> materialesDispon;

    public JPanelRealizarPrestamo(Usuario usuario, JMenuItem cancelar) {
        db = new Database();
        materialesDispon = getDBMatDispon();
        // Evento del botón para agregar material
        btnAgregarMat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Checo si no hay más de 5 materiales a prestar
                if ( datos.getComponentCount() < 16 ) {
                    JComboBox<String> selectMaterial = new JComboBox<String>();
                    Material m;
                    // Agrego al arrayList materialesDispon los materiales disponibles para prestar
                    for ( int x = 0; x < materialesDispon.size(); x++ ) {
                        m = materialesDispon.get(x);
                        if ( m.getCantDispon() > 0 )
                            selectMaterial.addItem( m.getNombre() );
                    }
                    JButton btnElim = new JButton("Eliminar");
                    // Evento del botón eliminar de cada material
                    btnElim.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Verifico que haya más de un material
                            if ( datos.getComponentCount() > 3  ) {
                                datos.remove(datos.getComponentZOrder(btnElim)-2);
                                datos.remove(datos.getComponentZOrder(btnElim)-1);
                                datos.remove(datos.getComponentZOrder(btnElim));
                                datos.updateUI();
                                datos.repaint();
                            }
                            // Si no hay por lo menos dos materiales, le informo que el préstamo debe contener al menos uno
                            else{
                                JOptionPane.showMessageDialog(null, "El préstamo por lo menos debe" +
                                        " de tener un material.");
                            }
                        }
                    });
                    // Agrego al JPanel datos el nuevo material
                    agregarMat(selectMaterial, btnElim);
                }
                // Si ya hay 6 materiales, le informo que no se pueden prestar 7 o más materiales en el mismo registro de préstamo
                else{
                    JOptionPane.showMessageDialog(null, "Se puede prestar mázimo 6 materiales " +
                            "a la vez. Si desea prestar más materiales al mismo profesor, favor de registrar este " +
                            "préstamo, y empezar un registrar un segundo. Ambos registros se unificarán.");
                }
                // Actualizo la interfaz
                datos.updateUI();
                datos.repaint();
            }
        });
        // Agregó un material para que empieze el registro con uno
        btnAgregarMat.doClick();
        // Evento para el botón aceptar el cual hará el préstamo
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Material> materialesSeleccionados = new ArrayList<Material>();
                int select = 0;
                JComboBox<String> selectMaterial;
                // Recorro los JComboBox del JPanel datos
                while (select < datos.getComponentCount()) {
                    selectMaterial = (JComboBox<String>) datos.getComponent( select );
                    //Agrego el material seleccionado al arrayList de materialesSeleccionados
                    materialesSeleccionados.add(buscarMaterial(materialesDispon, (String) selectMaterial.getSelectedItem() ));
                    // Me salto los tres componentes que no son JComboBox
                    select += 3;
                }
                // Creo al objetoProfesor
                Profesor prof = buscarProfesor(Integer.parseInt(txtNumProfesor.getText()));
                // Si no existe ningún profesor con el número de trabajador ingresado, le informo al usuario
                if ( prof == null ) {
                    JOptionPane.showMessageDialog(null, "El número de trabajador ingresado " +
                            "no existe. Inténtelo de nuevo.");
                }
                // Si existe el profesor con el número de trabajador ingresado, se realiza el préstamo y regresa a consultar préstamos
                else{
                    usuario.realizarPrestamo( prof, materialesSeleccionados);
                    btnCancelar.doClick();
                }
            }
        });

        // Evento del botón cancelar que cancela el registro del préstamo y te regresa a consultar préstamos
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar.doClick();
            }
        });
    }

    // Regresa el JPanel principal del form
    public JPanel getPaneel() {
        return  paneel;
    }

    // Agrega un nuevo JComboBox de material a JPanel datos
    public void agregarMat( JComboBox<String> opciones, JButton eliminar ){
        gbc.gridx = 1;
        datos.add(opciones, gbc);
        gbc.gridx = 2;
        datos.add(new JLabel("           "), gbc);
        gbc.gridx = 3;
        datos.add(eliminar, gbc);
    }

    // Regresa los materiales disponibles de acuerdo a la base de datos
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


    // Busca un material de acuerdo a su nombre en el arrayList de materiales
    public Material buscarMaterial ( ArrayList<Material> materiales, String nombreMaterial ) {
        for ( int x = 0; x < materiales.size(); x++ ) {
            if ( materiales.get(x).getNombre() == nombreMaterial ) {
                return materiales.get(x);
            }
        }
        return null;
    }

    // Busca al profesor en la base de datos de acuerdo a su número de trabajador
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