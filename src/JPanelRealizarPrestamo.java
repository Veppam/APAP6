import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class JPanelRealizarPrestamo {
    // Declaración de componentes de la interfaz
    private JLabel Nombre;
    private JComboBox<String> selectMaterial;
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


    private Material auxMaterial;

    public JPanelRealizarPrestamo(Usuario usuario, JMenuItem cancelar) {
        db = new Database();
        materialesDispon = getDBMatDispon();
        // Agrego al arrayList materialesDispon los materiales disponibles para prestar
        selectMaterial.addItem( "-- Seleccione un material --" );
        Material m;
        for ( int x = 0; x < materialesDispon.size(); x++ ) {
            m = materialesDispon.get(x);
            if ( m.getCantDispon() > 0 )
                selectMaterial.addItem( m.getNombre() );
        }
        // Evento del botón para agregar material
        selectMaterial.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if ( ( (String) selectMaterial.getSelectedItem()) != "-- Seleccione un material --" ){
                    Material c = buscarMaterial(materialesDispon, (String)selectMaterial.getSelectedItem());
                    System.out.println("TEngo " + c.getCantDispon());
                    c = buscarMaterial(materialesDispon, (String)selectMaterial.getSelectedItem());
                    if ( c.getCantDispon() > 0 && e.getStateChange() == ItemEvent.SELECTED ) {
                        JButton btnElim = new JButton("Eliminar");
                        // Evento del botón eliminar de cada material
                        btnElim.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // Verifico que haya más de un material
                                String y = ((JLabel)datos.getComponent(datos.getComponentZOrder(btnElim)-4)).getText();
                                System.out.println("estoy eb "+(datos.getComponentZOrder(btnElim))+" y soy "+  y);
                                actualizarCantidad( y );
                                datos.remove(datos.getComponentZOrder(btnElim)-4);
                                datos.remove(datos.getComponentZOrder(btnElim)-3);
                                datos.remove(datos.getComponentZOrder(btnElim)-2);
                                datos.remove(datos.getComponentZOrder(btnElim)-1);
                                datos.remove(datos.getComponentZOrder(btnElim));
                                datos.updateUI();
                                datos.repaint();
                                // Si no hay por lo menos dos materiales, le informo que el préstamo debe contener al menos uno
                                if ( datos.getComponentCount() < 1  ){
                                    JOptionPane.showMessageDialog(null, "El préstamo por lo menos debe" +
                                            " de tener un material.");
                                }
                            }
                        });
                        auxMaterial = buscarMaterial(materialesDispon,(String) selectMaterial.getSelectedItem());
                        JSpinner num = new JSpinner();

                        System.out.println((String) selectMaterial.getSelectedItem());

                        SpinnerNumberModel modelo = new SpinnerNumberModel(1, 1, auxMaterial.getCantDispon(), 1);
                        num.setModel( modelo );
                        actualizarCantidad((String) selectMaterial.getSelectedItem());
                        // Agrego al JPanel datos el nuevo material
                        agregarMat(new JLabel((String) selectMaterial.getSelectedItem()), num, btnElim);

                        // Actualizo la interfaz
                        //selectMaterial.removeItem( selectMaterial.getSelectedItem() );
                        selectMaterial.setSelectedItem( selectMaterial.getItemAt(0) );
                        datos.updateUI();
                        datos.repaint();
                    }
                }
            }
        });
        // Evento para el botón aceptar el cual hará el préstamo
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Material> materialesSeleccionados = new ArrayList<Material>();
                ArrayList<Integer> cuantosDeCadaUno = new ArrayList<Integer>();
                // Empiezo en el inicio del arreglo de los componentes del JPanel datos
                int pos = 0;
                // Recorro los JComboBox del JPanel datos
                while (pos < datos.getComponentCount()) {
                    //Agrego el material seleccionado al arrayList de materialesSeleccionados
                    materialesSeleccionados.add(buscarMaterial(materialesDispon, ( (JLabel) datos.getComponent( pos ) ).getText()));
                    // Aumento la posición en dos para llegar al JSpinner
                    pos += 2;
                    // Agrego el número de ejemplares a prestar del material
                    cuantosDeCadaUno.add( ( Integer ) ( ( JSpinner ) datos.getComponent( pos ) ).getValue() );
                    // Aumento la posición en 3 para llegar al siguiente JComboBox
                    pos += 3;
                }
                // Creo al objetoProfesor
                Profesor prof = buscarProfesor(Integer.parseInt(txtNumProfesor.getText()));
                // Si no existe ningún profesor con el número de trabajador ingresado, le informo al usuario
                if ( materialesSeleccionados.size() == 0 )
                    JOptionPane.showMessageDialog(null, "El préstamo debe contener al menos un material.");
                if ( prof == null )
                    JOptionPane.showMessageDialog(null, "El número de trabajador ingresado " +
                            "no existe. Inténtelo de nuevo.");
                // Si existe el profesor con el número de trabajador ingresado, se realiza el préstamo y regresa a consultar préstamos
                if ( materialesSeleccionados.size() != 0 && prof != null ) {
                    usuario.realizarPrestamo( prof, materialesSeleccionados, cuantosDeCadaUno );
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
    public void agregarMat( JLabel nomMaterial, JSpinner num, JButton eliminar ){
        gbc.gridx = 1;
        datos.add(nomMaterial, gbc);
        gbc.gridx = 2;
        datos.add(new JLabel("           "), gbc);
        gbc.gridx = 3;
        datos.add(num, gbc);
        gbc.gridx = 4;
        datos.add(new JLabel("           "), gbc);
        gbc.gridx = 5;
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
                System.out.println(res.getInt("cantidad"));
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

    public void actualizarCantidad ( String nombreMaterial ) {
        for ( int x = 0; x < materialesDispon.size(); x++ )
            if ( materialesDispon.get(x).getNombre() == nombreMaterial )
                materialesDispon.get(x).setCantDispon(materialesDispon.get(x).getCantDispon()*-1);
    }
}