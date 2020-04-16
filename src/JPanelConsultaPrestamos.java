import javax.swing.*;
import java.awt.*;

public class JPanelConsultaPrestamos extends JPanel {

    private JLabel txt;

    public JPanelConsultaPrestamos(Usuario usuario ){
        setLayout(new FlowLayout());
        txt = new JLabel("Bienvenido " + usuario.getNomUsuario());
        add(txt);
    }

}
