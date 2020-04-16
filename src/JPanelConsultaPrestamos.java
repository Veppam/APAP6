import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class JPanelConsultaPrestamos extends JPanel {

    private JTable tabla;
    private Database base;

    public JPanelConsultaPrestamos(Usuario usuario ){
        setLayout(new BorderLayout());
        JScrollPane panel = new JScrollPane();
        base = new Database();
        String[] columnas = {"Profesor",
                "Material",
                "Fecha",
                "Estado"};
        Vector<Object> vectorCols = new Vector<>();
        for(String col : columnas)
            vectorCols.add(col);
        JLabel title = new JLabel("Consulta de Préstamos");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("TimesRoman", Font.PLAIN,24));
        DefaultTableModel modelo = new DefaultTableModel(vectorCols,0);
        tabla = new JTable(modelo);
        tabla.setEnabled(false);
        panel.add(tabla);
        panel.setViewportView(tabla);
        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        consultarPrestamos();
    }
    public void consultarPrestamos(){
        String consulta = "SELECT id_profesor, fecha FROM prestamo";
        Vector<Vector<Object>> datos = new Vector<>();
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        try {
            ResultSet results = base.makeSqlCons(consulta);
            while(results.next()){
                String consulta2 = "SELECT id_profesor, nombre, ap_paterno, ap_materno FROM profesor WHERE id_profesor = "+results.getInt("id_profesor");
                ResultSet results2 = base.makeSqlCons(consulta2);
                if(results2.next()){
                    String nombre = results2.getNString("nombre")+
                            " "+ results2.getNString("ap_paterno")+
                            " "+ results2.getNString("ap_materno");
                    Vector datos_fila = new Vector();
                    datos_fila.add(nombre);
                    datos_fila.add("material");
                    datos_fila.add(results.getDate("fecha"));
                    modelo.insertRow(modelo.getRowCount(), datos_fila);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
