import javax.swing.*;
import java.awt.*;

public class LogIn extends JFrame{
    public LogIn(String modMensaje){
        //Ventana Modal inicio de sesión.
        JTextField nombre = new JTextField(20);
        JPasswordField pass = new JPasswordField (20);
        JLabel nomLa = new JLabel("Nombre:");
        JLabel passLab = new JLabel("Contraseña:");
        JPanel data = new JPanel();

        data.setLayout(new GridBagLayout());
        GridBagConstraints dataCons = new GridBagConstraints();
        //Acomodar inputs nombre y contraseña
        dataCons.fill = GridBagConstraints.HORIZONTAL;
        dataCons.gridx = 0;
        dataCons.gridy = 0;
        data.add (nomLa, dataCons);

        dataCons.fill = GridBagConstraints.HORIZONTAL;
        dataCons.gridx = 1;
        dataCons.gridy = 0;
        data.add (nombre, dataCons);

        dataCons.fill = GridBagConstraints.HORIZONTAL;
        dataCons.gridx = 0;
        dataCons.gridy = 1;
        data.add (passLab, dataCons);

        dataCons.fill = GridBagConstraints.HORIZONTAL;
        dataCons.gridx = 1;
        dataCons.gridy = 1;
        data.add (pass, dataCons);
        //Agregar data a Ventana Modal
        int ret = JOptionPane.showConfirmDialog(null, new JScrollPane(data), modMensaje, JOptionPane.OK_CANCEL_OPTION);

        if (ret == JOptionPane.OK_OPTION){
            Database db = new Database();
            String pssText = new String (pass.getPassword());
            if(db.valUsuario(nombre.getText(), pssText)){
                new MenuPrincipal( new Usuario(nombre.getText(), pssText));
            }else{
                LogIn niuN = new LogIn("LogIn (usuario y/o contraseña no coinciden)");
            }
        }
    }
}
