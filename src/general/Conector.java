package general;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author francisco
 */
public class Conector {
   Connection connect; 
   String servidor;
   String puerto;

    public Conector() {
        //Datos para la conexion
        BeanBase beanBase= new BeanBase();
        servidor=beanBase.getConfiguracion().getProperty("servidor").trim();        
        puerto=beanBase.getConfiguracion().getProperty("puerto").trim();        
    }
   
    public Connection connect(String baseDatos){
        try {
         connect = DriverManager.getConnection("jdbc:mysql://" + servidor + ":" + puerto + "/" + baseDatos + "?user=dba&password=Admin*Duende*2022&noAccessToProcedureBodies=true&zeroDateTimeBehavior=convertToNull&serverTimezone=America/Buenos_Aires");
         if (connect!=null) {
             //System.out.println("Conectado");
             return connect;
         }
        }catch (SQLException ex) {
            System.err.println("No se ha podido conectar a la base de datos\n"+ex.getMessage());
            return null;
        }
        return null;
    }
 
 
}
