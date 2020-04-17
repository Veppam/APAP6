import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class ModificarMaterial extends JPanel{

    private Database db;

    private JPanel Titulo;
    private JLabel textoTitulo;
    private final JTable Materiales;
    private JScrollPane Modificar;

    public ModificarMaterial(JPanel contenedor, Usuario user) {

        setLayout(new BorderLayout());

        db = new Database();

        Titulo = new JPanel();
        Titulo.setLayout(new GridBagLayout());

        textoTitulo = new JLabel("Modificar Material");

        Vector temp;
        Vector data = db.getDBMaterials();
        Vector dataUsed = new Vector();
        Vector columnas = new Vector<String>();

        Class[] tipoColumnas = new Class[]{ String.class, String.class, String.class, JButton.class, JButton.class};

        JButton mod = new JButton("Mod");
        JButton elim = new JButton("Elim");

        columnas.addElement("No. de Serie");
        columnas.addElement("Nombre");
        columnas.addElement("Cantidad");
        columnas.addElement("Modificar");
        columnas.addElement("Eliminar");

        for (int i = 0; i < data.size(); i++){
            temp = (Vector) data.get(i);
            temp.remove(3);
            temp.add(mod);
            temp.add(elim);
            dataUsed.addElement(temp);
        }

        Materiales = new JTable(dataUsed, columnas);
        Materiales.setModel(new DefaultTableModel(dataUsed, columnas){

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return tipoColumnas[columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return !(this.getColumnClass(column).equals(JButton.class));
            }

        });

        Materiales.setDefaultRenderer(JButton.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                return (Component) value;
            }
        });

        Materiales.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                int row = Materiales.rowAtPoint(e.getPoint());
                int column = Materiales.columnAtPoint(e.getPoint());

                if (Materiales.getModel().getColumnClass(column).equals(JButton.class)){
                    if (column == Materiales.getColumnCount() - 1){
                        int resp = JOptionPane.showConfirmDialog(null,
                                                                 "¿Estas seguro de eliminar el material? \n" +
                                                                 " No. de Serie: " + Materiales.getModel().getValueAt(row, 0) +
                                                                 "\nNombre: " + Materiales.getModel().getValueAt(row, 1),
                                                                "¿Estas Seguro?",
                                                                 JOptionPane.YES_NO_OPTION,
                                                                 JOptionPane.WARNING_MESSAGE);
                        if (resp == JOptionPane.YES_OPTION){
                            user.eliminarMaterial(Integer.parseInt((String) Materiales.getModel().getValueAt(row, 0)));
                        }
                    }else {
                        contenedor.removeAll();
                        contenedor.updateUI();
                        contenedor.repaint();
                        System.out.println((String) Materiales.getModel().getValueAt(row, 0));
                        contenedor.add (new AddMateriales(user, (String) Materiales.getModel().getValueAt(row, 0)));
                    }
                };
            }

        });

        Materiales.setRowHeight(35);

        Titulo.add(textoTitulo);
        Modificar = new JScrollPane(Materiales);
        add(Titulo, BorderLayout.NORTH);
        add(Modificar, BorderLayout.CENTER);

    }

}
