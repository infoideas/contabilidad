/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import javax.swing.JOptionPane;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author Propietario
 */

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author rafael
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    
    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            BeanBase beanBase= new BeanBase();
            String ls_servidor=(String) beanBase.getConfiguracion().getProperty("servidor").trim();
            String  ls_puerto=(String) beanBase.getConfiguracion().getProperty("puerto").trim();
            String URL="jdbc:mysql://" + ls_servidor + ":" + ls_puerto + "/contabilidad?zeroDateTimeBehavior=convertToNull&serverTimezone=America/Buenos_Aires";
            
            sessionFactory=new Configuration( )
             .addResource ("entidades/Asiento.hbm.xml")
             .addResource ("entidades/AsientoDet.hbm.xml")  
             .addResource ("entidades/Banco.hbm.xml") 
             .addResource ("entidades/CarteraCheques.hbm.xml")                    
             .addResource ("entidades/CategoriaPercepcion.hbm.xml")
             .addResource ("entidades/CategoriaRetencion.hbm.xml")                    
             .addResource ("entidades/CentroCosto.hbm.xml")
             .addResource ("entidades/Concepto.hbm.xml")                    
             .addResource ("entidades/Cuenta.hbm.xml")                    
             .addResource ("entidades/CuentaFondos.hbm.xml")    
             .addResource ("entidades/Ejercicio.hbm.xml")                        
             .addResource ("entidades/Empresa.hbm.xml")    
             .addResource ("entidades/FormaPago.hbm.xml")                        
             .addResource ("entidades/IndiceInflacion.hbm.xml")   
             .addResource ("entidades/Iva.hbm.xml")   
             .addResource ("entidades/Moneda.hbm.xml")                       
             .addResource ("entidades/MovCuenta.hbm.xml")                       
             .addResource ("entidades/MovCuentaDet.hbm.xml")   
             .addResource ("entidades/MovCuentaRet.hbm.xml")                       
             .addResource ("entidades/MovPeriodo.hbm.xml")    
             .addResource ("entidades/Periodo.hbm.xml")                        
             .addResource ("entidades/Plantilla.hbm.xml")    
             .addResource ("entidades/PlantillaDet.hbm.xml")  
             .addResource ("entidades/PlantillaParam.hbm.xml")                      
             .addResource ("entidades/Recibo.hbm.xml")                      
             .addResource ("entidades/ReciboRetencion.hbm.xml")                      
             .addResource ("entidades/ReciboSol.hbm.xml")                      
             .addResource ("entidades/Solicitud.hbm.xml")   
             .addResource ("entidades/SolicitudIva.hbm.xml")                                          
             .addResource ("entidades/SolicitudPercepcion.hbm.xml")                                          
             .addResource ("entidades/SubcategoriaPercepcion.hbm.xml")                                          
             .addResource ("entidades/SubcategoriaRetencion.hbm.xml")     
             .setProperty("hibernate.connection.driver_class","com.mysql.cj.jdbc.Driver")                                        
	     .setProperty("hibernate.connection.url",URL)
             .setProperty("hibernate.connection.username","dba")                    
             .setProperty("hibernate.connection.password","Admin*Duende*2022")                                        
             .setProperty("hibernate.dialect","org.hibernate.dialect.MySQLDialect")     
             .setProperty("hibernate.show_sql","true")         
             .setProperty("hibernate.connection.characterEncoding","utf8")        
	     .buildSessionFactory();
            
        } catch (Throwable ex) {
            // Log the exception. 
            JOptionPane.showMessageDialog(null,"Initial SessionFactory creation failed." + ex,"",JOptionPane.WARNING_MESSAGE);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
