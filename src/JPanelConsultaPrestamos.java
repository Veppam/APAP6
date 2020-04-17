import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class JPanelConsultaPrestamos extends JPanel {

    private JTable tabla;
    private Database base;

    public JPanelConsultaPrestamos(Usuario usuario ) {
        setLayout(new BorderLayout());
        JScrollPane panel = new JScrollPane();
        base = new Database();
        String[] columnas = {"Profesor",
                "Material",
                "Fecha",
                "Estado"};
        Vector<Object> vectorCols = new Vector<>();
        for (String col : columnas)
            vectorCols.add(col);
        JLabel title = new JLabel("Consulta de Préstamos");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("TimesRoman", Font.PLAIN, 24));
        DefaultTableModel modelo = new DefaultTableModel(vectorCols, 0);
        tabla = new JTable(modelo);
        tabla.setEnabled(false);
        tabla.getColumn("Material").setCellRenderer(new TextAreaRenderer());
        tabla.getColumn("Profesor").setCellRenderer(new TextAreaRenderer());
        tabla.setRowHeight(120);
        panel.add(tabla);
        panel.setViewportView(tabla);
        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        consultarPrestamos();
    }
    public void consultarPrestamos(){
        ArrayList<Profesor> profesores = new ArrayList<Profesor>();
        String consulta = "SELECT id_prestamo, id_profesor, fecha FROM prestamo";
        Vector<Vector<Object>> datos = new Vector<>();
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        try {
            ResultSet results = base.makeSqlCons(consulta);
            while(results.next()){
                String consulta2 = "SELECT id_profesor, nombre, ap_paterno, ap_materno FROM profesor WHERE id_profesor = "+results.getInt("id_profesor");
                ResultSet results2 = base.makeSqlCons(consulta2);
                if(results2.next()){
                    profesores.add(new Profesor(results2.getInt("id_profesor"),results2.getNString("nombre"),
                            results2.getNString("ap_paterno"),results2.getNString("ap_materno")));
                    String nombre = results2.getNString("nombre")+
                            " "+ results2.getNString("ap_paterno")+
                            " "+ results2.getNString("ap_materno");
                    Vector datos_fila = new Vector();
                    datos_fila.add(nombre);
                    String consulta3 = "SELECT id_material FROM material_prestado WHERE id_prestamo = "+results.getInt("id_prestamo");
                    ResultSet results3 = base.makeSqlCons(consulta3);
                    String materiales = "";
                    while (results3.next()){
                        String consulta4 = "SELECT id_material, nombre FROM material WHERE id_material = "+results3.getInt("id_material");
                        ResultSet results4 = base.makeSqlCons(consulta4);
                        if (results4.next()) {
                            materiales += results4.getInt("id_material")+" "+results4.getNString("nombre")+"\n";
                        }
                    }
                    datos_fila.add(materiales);
                    datos_fila.add(results.getDate("fecha"));
                    JButton estado = new JButton("Finalizar prestamo");
                    estado.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                        }
                    });
                    datos_fila.add(estado);
                    modelo.insertRow(modelo.getRowCount(), datos_fila);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public class TextAreaRenderer extends JTextArea implements TableCellRenderer {
        public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
        }
        public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((String)obj);
            return this;
        }
    }
}
