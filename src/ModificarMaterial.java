import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

//JPanel que contiene los elementos de consulta, modificaion y eliminacion de materiales del inventario
public class ModificarMaterial extends JPanel{

    private Database db;

    //Componentes que tendra el JPanel Modificar Material
    private JPanel Titulo;
    private JLabel textoTitulo;
    private JTable Materiales;
    private JScrollPane Modificar;

    //La clase es el panel en el que se puede consultar, modificar y eliminar los materiales del inventario, pide el Jpanel que lo va a contener y el usuario que lo podra hacer
    public ModificarMaterial(JPanel contenedor, Usuario user) {

        //Establece el layout de ModificarMAterial
        setLayout(new BorderLayout());

        //Inicializa la clase db para poder  obtener los datos de la Base de Datos
        db = new Database();

        //Establece el tecto titulo del JPanael
        Titulo = new JPanel();
        Titulo.setLayout(new GridBagLayout());
        textoTitulo = new JLabel("Modificar Material");
        textoTitulo.setFont(new Font("Arial", Font.PLAIN, 24));

        //Vectores usados para obtener los datos que van a ir en la tabla
        Vector temp;
        Vector data = db.getDBMaterials();
        Vector enUso = db.getMaterialOcc();
        Vector dataUsed = new Vector();
        Vector columnas = new Vector<String>();

        //Establece las clases de cada columna de la tabla
        Class[] tipoColumnas = new Class[]{ImageIcon.class, String.class, String.class, String.class, String.class, JButton.class, JButton.class};

        //Iconos de los materiales
        ImageIcon icono;
        Image tempImg;

        //Botones de modificacion eliminacion de material del inventario
        JButton mod = new JButton("");
        JButton elim = new JButton("");

        mod.setIcon(reajustarImg(new ImageIcon("img/edit.png"), 75, 55));
        elim.setIcon(reajustarImg(new ImageIcon("img/trash.jpg"), 75, 55));


        //Vector que contiene los encabezados de la columna de la tabla
        columnas.addElement("Imagen");
        columnas.addElement("No. de Serie");
        columnas.addElement("Nombre");
        columnas.addElement("Cantidad Total");
        columnas.addElement("En Uso");
        columnas.addElement("Modificar");
        columnas.addElement("Eliminar");

        //Agrega los datos necesarios para una consulta de materiales y agrega los botones de eliminar y editar materiales
        for (int i = 0; i < data.size(); i++){
            temp = (Vector) data.get(i);

            //Ajusta la imagen al tamaño de la celda
            icono = reajustarImg(new ImageIcon((String) temp.elementAt(3)), 75, 65);

            //Acomoda y agrega los elementos a la tabla conforme al orden de las columnas
            temp.insertElementAt(icono, 0);
            temp.insertElementAt(enUso.get(i), 4);
            temp.remove(temp.size() - 1);
            temp.add(mod);
            temp.add(elim);
            dataUsed.addElement(temp);
        }

        //Crea la tabla, le agrega los datos y establece la altura de las filas
        Materiales = new JTable(dataUsed, columnas);
        Materiales.setRowHeight(55);

        //Establece el modelo de la tabla
        Materiales.setModel(new DefaultTableModel(dataUsed, columnas){

            //Obtiene la clase de los datos de una columna
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return tipoColumnas[columnIndex];
            }

            //Permite que los botones en cada celda sean funcionales haciendolos editables
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//!(this.getColumnClass(column).equals(JButton.class));
            }

        });

        //Establece el renderizador de la tabla para poder poner botones funcionales en las celdas
        Materiales.setDefaultRenderer(JButton.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                return (Component) value;
            }
        });

        //Le agrega una respuesta al evento de hacer click en la tabla
        Materiales.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                //Obtiene la fila y columna donde se dio el click
                int row = Materiales.rowAtPoint(e.getPoint());
                int column = Materiales.columnAtPoint(e.getPoint());

                //Verifica si donde se dio el click es un boton
                if (Materiales.getModel().getColumnClass(column).equals(JButton.class)){
                    //Verifica que boton es, el primero es elimanr material
                    if (column == Materiales.getColumnCount() - 1){
                        //Muestra una ventana para preguntar si el usuario esta seguro de eliminar el material
                        int resp = JOptionPane.showConfirmDialog(null,
                                                                 "¿Estas seguro de eliminar el material? \n" +
                                                                 " No. de Serie: " + Materiales.getModel().getValueAt(row, 1) +
                                                                 "\nNombre: " + Materiales.getModel().getValueAt(row, 2),
                                                                "¿Estas Seguro?",
                                                                 JOptionPane.YES_NO_OPTION,
                                                                 JOptionPane.WARNING_MESSAGE);
                        //Verifica si el usuario de verdad quiere borrar el material
                        if (resp == JOptionPane.YES_OPTION){
                            user.eliminarMaterial(Integer.parseInt((String) Materiales.getModel().getValueAt(row, 1)));
                            //Actualiza la tabla de materiales que se muestra en pantalla
                            contenedor.removeAll();
                            contenedor.updateUI();
                            contenedor.repaint();
                            contenedor.setLayout(new BorderLayout());
                            contenedor.add(new ModificarMaterial(contenedor, user), BorderLayout.CENTER);

                        }
                    // Si es el otro boton, reemplaza el panel con el de agregar material, pero sin pedir el numero de serie
                    }else {



                        contenedor.removeAll();
                        contenedor.updateUI();
                        contenedor.repaint();
                        AddMateriales adM = new AddMateriales(contenedor ,user, new Material((String) Materiales.getModel().getValueAt(row, 2),
                                Integer.parseInt((String) Materiales.getModel().getValueAt(row, 1)),
                                Integer.parseInt((String) Materiales.getModel().getValueAt(row, 3)),
                                (ImageIcon) Materiales.getModel().getValueAt(row, 0)));
                        contenedor.add (adM.getPanela());
                    }
                };
            }

        });

        //Añade los elementos a ModificarMateriales
        Titulo.add(textoTitulo);
        Modificar = new JScrollPane(Materiales);
        add(Titulo, BorderLayout.NORTH);
        add(Modificar, BorderLayout.CENTER);
        setBackground(new Color(70, 191,200));

    }

    //Reajusta el tamaño de la imagen de un ImageIcon, recibe el ImageIcon  a reajustar, el nuevo ancho y la nueva altura
    private ImageIcon reajustarImg(ImageIcon imagen, int width, int height){

        Image tempImg = imagen.getImage();
        tempImg = tempImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resp = new ImageIcon(tempImg);

        return resp;

    }

}
