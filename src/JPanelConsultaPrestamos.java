import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
        Class[] tiposColumnas = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                Date.class,
                JButton.class
        };
        JLabel title = new JLabel("Consulta de Préstamos");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("TimesRoman", Font.PLAIN, 24));
        tabla = new JTable();
        tabla.setModel(new DefaultTableModel(vectorCols, 0) {
            Class[] tipos = tiposColumnas;

            @Override
            public Class getColumnClass(int columnIndex) {
                return tipos[columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return !(this.getColumnClass(column).equals(JButton.class));
            }
        });
        tabla.setDefaultRenderer(JButton.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object objeto, boolean estaSeleccionado, boolean tieneElFoco, int fila, int columna) {
                return (Component) objeto;
            }
        });
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.rowAtPoint(e.getPoint());
                int columna = tabla.columnAtPoint(e.getPoint());
                if (tabla.getModel().getColumnClass(columna).equals(JButton.class)) {
                    String prof = (String) tabla.getModel().getValueAt(fila, 0);
                    String[] nombreProf = prof.split(" ");
                    String campo2 = (String) tabla.getModel().getValueAt(fila, 1);
                    String[] materiales = campo2.split("\n");
                    ArrayList<String> ids_mat = new ArrayList<String>();
                    for (int x=0; x < materiales.length; x++)
                        ids_mat.add(materiales[x].substring(0,3));
                    String cons1 = "";
                    if(nombreProf.length < 4)
                        cons1 = "SELECT id_profesor from profesor WHERE nombre='"+nombreProf[0]+"' AND ap_paterno='"+nombreProf[1]+"' AND ap_materno='"+nombreProf[2]+"'";
                    else
                        cons1 = "SELECT id_profesor from profesor WHERE nombre='"+nombreProf[0]+" "+nombreProf[1]+"' AND ap_paterno='"+nombreProf[2]+"' AND ap_materno='"+nombreProf[3]+"'";

                    ResultSet res1 = base.makeSqlCons(cons1);
                    try {
                        if(res1.next()){
                            String cons2 = "SELECT id_prestamo FROM prestamo where id_profesor="+res1.getInt("id_profesor");
                            ResultSet res2 = base.makeSqlCons(cons2);
                            if(res2.next()){
                                for (int n=0; n<ids_mat.size(); n++){
                                    String upd1 = "UPDATE cantidad FROM material SET cantidad+=(SELECT cantidad FROM material_prestado WHERE id_prestamo="+
                                            res2.getInt("id_prestamo")+" AND id_material="+Integer.parseInt(ids_mat.get(n))+") WHERE id_material="+Integer.parseInt(ids_mat.get(n));
                                    base.exeSql(upd1);
                                    String del2 = "DELETE FROM material_prestado WHERE id_prestamo="+res2.getInt("id_prestamo")+" AND id_material="+Integer.parseInt(ids_mat.get(n));
                                    base.exeSql(del2);
                                }
                                String del1 = "DELETE FROM prestamo WHERE id_prestamo="+res2.getInt("id_prestamo");
                                base.exeSql(del1);
                                consultarPrestamos();
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
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
