/*
 * Reporte.java
 *
 * Created on 29 de mayo de 2007, 11:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package impresion;

import java.util.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import java.sql.*;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author rafaelg
 */

public class Reporte {
   
    /** Creates a new instance of Reporte */
    public Reporte() {
    }

    
    public void generaFormulario(String reporteJasper, Map<String,Object> params,ResultSet r)
    {
        try {
           r.beforeFirst();
           JRDataSource JRds= new JRResultSetDataSource(r);

           JasperPrint jasperPrint= JasperFillManager.fillReport(reporteJasper,params,JRds);
         
           //Mostramos el resultado en el Viewer del JasperReports
           JasperViewer.viewReport(jasperPrint, false);
           
          
        }
       catch(JRException ex)
       {
            ex.getMessage();

       }
       catch(Exception e)
       {
            e.getMessage();

       }

    }
    
    public void generaFormulario(String reporteJasper, Map<String,Object> params,ArrayList lista)
    {
        try {
           JRDataSource JRds= new JRBeanCollectionDataSource(lista);
           JasperPrint jasperPrint= JasperFillManager.fillReport(reporteJasper,params,JRds);
           System.out.println("Ok10");
           //Mostramos el resultado en el Viewer del JasperReports
           JasperViewer.viewReport(jasperPrint, false);
        }
       catch(JRException ex)
       {
            ex.getMessage();
            System.out.println("Error:" + ex.getMessage());

       }
       catch(Exception e)
       {
            e.getMessage();

       }

    }
    

    
    
        
        
        
   
    
}
    
