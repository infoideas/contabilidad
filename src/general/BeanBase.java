/*
 * BeanBase.java
 *
 * Created on 10 de noviembre de 2005, 10:00
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package general;
import contabilidad.LogIn;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 *
 * @author rafaelg
 */

//Bean b�sico con m�todos para la conexi�n a las bases de datos

public class BeanBase {
    private String servidor;
    private String puerto;
    private Properties configuracion = new Properties();
    public static final String nombreAplicacion="Contabilidad";
    public static final String  URI_BACKEND="http://elduende.com.ar/estanciaBackend/webresources";    
    //public static final String  URI_BACKEND="http://localhost/estanciaBackend/webresources";    
    public static final String archivoConfig="c:/aplicacionesDuende/config/Configuracion.properties";

    public Properties getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Properties configuracion) {
        this.configuracion = configuracion;
    }

        
    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getNombreAplicacion() {
        return nombreAplicacion;
    }
    
    public String getURI_BACKEND() {
        return URI_BACKEND;
    }

    public static String getArchivoConfig() {
        return archivoConfig;
    }

    
    /** Creates a new instance of BeanBase */
    public BeanBase() {
         leeConfiguracion();
    }

    public void leeConfiguracion(){
        InputStream is = null;
        try {
            is=new FileInputStream(archivoConfig);
            configuracion.load(is);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String mesLetras(int mes,String formato){
        String ls_mes = null;
        switch(mes){
            case 1:
                if(formato.equals("A"))
                    ls_mes="Ene";
                else
                    ls_mes="Enero";
                break;
            case 2:
                if(formato.equals("A"))
                    ls_mes="Feb";
                else
                    ls_mes="Febrero";
                break;
            case 3:
                if(formato.equals("A"))
                    ls_mes="Mar";
                else
                    ls_mes="Marzo";
                break;
            case 4:
                if(formato.equals("A"))
                    ls_mes="Abr";
                else
                    ls_mes="Abril";
                break;
            case 5:
                if(formato.equals("A"))
                    ls_mes="May";
                else
                    ls_mes="Mayo";
                break;
            case 6:
                if(formato.equals("A"))
                    ls_mes="Jun";
                else
                    ls_mes="Junio";
                break;
            case 7:
                if(formato.equals("A"))
                    ls_mes="Jul";
                else
                    ls_mes="Julio";
                break;
            case 8:
                if(formato.equals("A"))
                    ls_mes="Ago";
                else
                    ls_mes="Agosto";
                break;
            case 9:
                if(formato.equals("A"))
                    ls_mes="Sep";
                else
                    ls_mes="Septiembre";
                break;
            case 10:
                if(formato.equals("A"))
                    ls_mes="Oct";
                else
                    ls_mes="Octubre";
                break;
            case 11:
                if(formato.equals("A"))
                    ls_mes="Nov";
                else
                    ls_mes="Noviembre";
                break;
            case 12:
                if(formato.equals("A"))
                    ls_mes="Dic";
                else
                    ls_mes="Diciembre";
                break;
                
            default:
                ls_mes="";

        }
            
        return ls_mes;
    }
    
    
    //Obtiene datos del usuario
    public UsuarioAdmin obtenerUsuario(int idUsuario){
        CallableStatement s=null;
        ResultSet r=null;
        int li_id;
        String ls_nombreUsuario,ls_clave,ls_apellido,ls_nombre,ls_tipo,ls_estado;
        StringMD md5=new StringMD();
        
        UsuarioAdmin usuario= new UsuarioAdmin();
        //Conectamos a la base
        Conector conector = new Conector();  
        Connection conexion = conector.connect("administracion");
        
        try {

               s=conexion.prepareCall("{call sp_get_def_usuario_admin_id ( ? )}",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
               s.setInt(1,idUsuario);
               
               r=s.executeQuery();
               if (r.next()){
                   li_id=r.getInt("id");
                   ls_nombreUsuario=r.getString("nombreUsuario");
                   ls_clave=r.getString("clave");
                   
                   ls_apellido=r.getString("apellido");
                   if (ls_apellido==null)
                       ls_apellido="";
                   
                   ls_nombre=r.getString("nombre");
                   if (ls_nombre==null)
                       ls_nombre="";
                   
                   ls_estado=r.getString("estado");
                   ls_tipo=r.getString("tipo");
                   
                   usuario.setId(li_id);
                   usuario.setNombreUsuario(ls_nombreUsuario);
                   usuario.setNombreCompletoUsuario(ls_apellido + " " + ls_nombre);
                   usuario.setEstado(ls_estado);
                   
               }
               s.close(); 
               return usuario;
        } catch (SQLException e){
            e.getMessage();
            return null;            
        } finally {
             try{
                if (conexion != null) conexion.close();   
             }catch(Exception e)  {
                e.getMessage();
             }
        }
    
    }
    
    //Verifica permiso del usuario para la transacción
    public boolean validarPermiso(String nombreUsuario,String permiso,boolean mostrarMensaje){
        
        CallableStatement s=null;
        ResultSet r=null;
        boolean lb_autorizacion=false;
        
        //Conectamos a la base
        Conector conector = new Conector();  
        Connection conexion = conector.connect("administracion");
        
        try {      
            
             //Los parámetros que tienen blanco o nulo son porque no los actualiza
             s=conexion.prepareCall("{call sp_valida_permiso ( ? , ? , ? )}"); 
             s.setString(1,nombreUsuario); 
             s.setString(2,permiso);
             s.registerOutParameter(3,java.sql.Types.BOOLEAN);
             s.execute();
             
             //Obtengo el resultado
             lb_autorizacion=s.getBoolean(3);

        }catch (SQLException e){
            JOptionPane.showMessageDialog(null,"Error al validar permiso: " + e.getMessage(),"Error",JOptionPane.WARNING_MESSAGE);                                                              
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error al validar permiso: " + e.getMessage(),"Error",JOptionPane.WARNING_MESSAGE);                                                              
        }finally {
            try{
                s.close();                            
                if (conexion != null) conexion.close();   
                if (r != null) r.close();   
             }catch(Exception e)  {
                e.getMessage();
             }
        }
        
        if (!lb_autorizacion && mostrarMensaje ){
              JOptionPane.showMessageDialog(null,"Transacción no autorizada para este usuario","Módulo de seguridad",JOptionPane.WARNING_MESSAGE);
        }
        
        return lb_autorizacion;
        
    }
    
    //Graba auditoría de la transaccción
    public boolean grabaAuditoria(int idUsuario,String codigoPermiso,String observaciones,String aplicacion,String nombreEquipo) throws UnsupportedEncodingException{
        
        observaciones = URLEncoder.encode(observaciones, "UTF-8");        
        aplicacion = URLEncoder.encode(aplicacion, "UTF-8");        
        nombreEquipo = URLEncoder.encode(nombreEquipo, "UTF-8");                
        
        JAXBContext jc;
        InputStream xml;
        String uri = String.format(getURI_BACKEND() + "/seguridad/grabaAuditoria?idUsuarioTrans=%1s&codigoPermiso=%1s&observacionesTrans=%1s&aplicacionTrans=%1s&nombreEquipoTrans=%1s&tipoRespuesta=XML",idUsuario,codigoPermiso,observaciones,aplicacion,nombreEquipo);
        System.out.println("URI:" + uri);
        String ls_resultado=null;
        URL url;
        
        try {
            url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
             
            if (connection.getResponseCode() != 200) {
                    throw new RuntimeException("Error: HTTP error code : " + connection.getResponseCode());
            }else
            {
                jc = JAXBContext.newInstance(Auditoria.class);
                xml = connection.getInputStream();
                Auditoria auditoria = new Auditoria();
                auditoria = (Auditoria) jc.createUnmarshaller().unmarshal(xml);
                System.out.println("Usuario:" +  auditoria.getObservaciones());

                if (auditoria.getResultado()==1){
                    return true;
                }
                else{
                    JOptionPane.showMessageDialog(null,auditoria.getObservaciones(),"Error",JOptionPane.WARNING_MESSAGE);            
                    return false;                            
                }
                    
            }
            
         } catch (MalformedURLException ex) {
             Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
             return false;
         } catch (IOException ex) {
             Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
             return false;
         } catch (JAXBException ex) {
             Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
             return false;
         }
        
        
        
        
    }
    
    //Obtiene el nombre de la PC
    public String obtieneNombreEquipo() {
        java.net.InetAddress localMachine; 
        try {
            localMachine = java.net.InetAddress.getLocalHost();
            System.out.println("Hostname of local machine: " + localMachine.getHostName());            
            return localMachine.getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(BeanBase.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        
    }
    
}