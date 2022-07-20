/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contabilidad;

import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Propietario
 */
public class Contabilidad {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here

            Properties props = new Properties();
            props.put("logoString", "El Duende");
            props.put("selectionBackgroundColor", "51 199 255"); 
            props.put("menuSelectionBackgroundColor", "51 199 255"); 
            
            props.put("windowTitleForegroundColor", "255 255 255");
            props.put("windowTitleBackgroundColor", "51 199 255"); 
            
            props.put("windowTitleColorLight", "51 199 255"); 
            props.put("windowTitleColorDark", "51 199 255"); 
            
            props.put("rolloverColor", "51 255 255"); 
            props.put("rolloverColorLight", "51 255 255"); 
            props.put("rolloverColorDark", "180 240 197"); 
            
            AluminiumLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Contabilidad.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Contabilidad.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Contabilidad.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Contabilidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Ventana de inicio de sesión
        LogIn dialog = new LogIn();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
              
    }
    
}
