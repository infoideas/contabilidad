/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contabilidad;

import entidades.Cuenta;
import entidades.Empresa;
import general.BeanBase;
import general.Conector;
import general.HibernateUtil;
import general.ListaDetalle;
import general.StringMD;
import general.UsuarioAdmin;
import java.awt.BorderLayout;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Propietario
 */
public class LogIn extends javax.swing.JDialog {
    public boolean mod;
    public boolean wb_nuevo;
    private UsuarioAdmin usuarioAdmin;
    BeanBase beanBase= new BeanBase();
    
    public UsuarioAdmin getUsuarioAdmin() {
        return usuarioAdmin;
    }

    public void setUsuarioAdmin(UsuarioAdmin usuarioAdmin) {
        this.usuarioAdmin = usuarioAdmin;
    }

   
    /**
     * Creates new form CuentaDialog
     */
    public LogIn(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        //Botón Default
        getRootPane().setDefaultButton(buAceptar);
        //Listener para cerrar la ventana con la tecla Escape
        addEscapeListener(this);
        
        beanBase.getConfiguracion().getProperty("puerto").trim();        
        jTUsuario.setText(beanBase.getConfiguracion().getProperty("puerto").trim());
        jPClave.requestFocus();
    }
    
    public LogIn() {
        initComponents();
        //Botón Default
        getRootPane().setDefaultButton(buAceptar);
        //Listener para cerrar la ventana con la tecla Escape
        addEscapeListener(this);
        beanBase.getConfiguracion().getProperty("puerto").trim();        
        jTUsuario.setText(beanBase.getConfiguracion().getProperty("usuario").trim());
        jPClave.requestFocus();
    }
    
    //Listener para cerrar la ventana con la tecla Escape
    public static void addEscapeListener(final JDialog dialog) {
        ActionListener escListener = new ActionListener() 
            { 
              @Override
              public void actionPerformed(ActionEvent e) 
                { 
                    dialog.dispose();
                }
            }; 
        dialog.getRootPane().registerKeyboardAction(escListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW); 
    }

    
    
    
    private boolean actualizaBaseDatos(Object o){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Cuenta u= (Cuenta) o;
        try{
            session.beginTransaction();
            session.saveOrUpdate(u);
            session.getTransaction().commit();
            return true;
            
        }
        catch (HibernateException e){
            session.getTransaction().rollback();
            JOptionPane.showMessageDialog(null,e.getLocalizedMessage(),"Error",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        finally {
            session.close();
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        labelId = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPClave = new javax.swing.JPasswordField();
        jTUsuario = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        buAceptar = new javax.swing.JButton();
        buSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Inicio de sesión");
        setModal(true);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Usuario:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Clave:");

        jPClave.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTUsuario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addComponent(labelId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPClave, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                    .addComponent(jTUsuario))
                .addGap(46, 46, 46))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(labelId))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jPClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        buAceptar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        buAceptar.setText("Aceptar");
        buAceptar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buAceptarActionPerformed(evt);
            }
        });

        buSalir.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        buSalir.setText("Salir");
        buSalir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addComponent(buAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(buSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(101, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buAceptar, buSalir});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(buSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(buAceptar))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {buAceptar, buSalir});

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buAceptarActionPerformed
        // TODO add your handling code here:
        String ls_nombreUsuario,ls_claveUsuario,ls_mensaje="";
        ls_nombreUsuario=jTUsuario.getText().trim();
        if (ls_nombreUsuario==null || ls_nombreUsuario.equals("")){
          JOptionPane.showMessageDialog(null, "Debe ingresar el usuario","Datos incompletos",JOptionPane.WARNING_MESSAGE);
          jTUsuario.requestFocus();
          return;
        }
        
        ls_claveUsuario=String.valueOf(jPClave.getPassword());
        if (ls_claveUsuario==null || ls_claveUsuario.equals("")){
          JOptionPane.showMessageDialog(null, "Debe ingresar la clave","Datos incompletos",JOptionPane.WARNING_MESSAGE);
          jPClave.requestFocus();
          return;
        }
        
        //Reviso si tiene permiso
        String ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("IngresarModulo");//Permiso para ingresar al sistema         
        BeanBase bean= new BeanBase();
        if (!bean.validarPermiso(ls_nombreUsuario,ls_permiso,true))
            return;
        
        //Valido el usuario y password
        if (validarLogin(ls_nombreUsuario,ls_claveUsuario)){
            //Grabo el último usuario conectado
            BeanBase beanBase= new BeanBase();
            beanBase.getConfiguracion().setProperty("usuario",ls_nombreUsuario);
            try {
                beanBase.getConfiguracion().store(new FileWriter(beanBase.getArchivoConfig()),ls_nombreUsuario);
            } catch (IOException ex) {
                Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
            }
            

            SeleccionarEmpresa dialog = new SeleccionarEmpresa();
            dialog.setLocationRelativeTo(null);
            dialog.setUsuarioAdmin(usuarioAdmin);
            dialog.setVisible(true);
            dispose();
        }
        
    }//GEN-LAST:event_buAceptarActionPerformed

    private boolean validarLoginws(String nombreUsuario,String claveUsuario){
        JAXBContext jc;
        InputStream xml;
        String uri = String.format("http://localhost/estanciaBackend/webresources" + "/seguridad/validaLoginUsuarioAdmin?nombreUsuario=%1s&clave=%1s&tipoRespuesta=XML",nombreUsuario,claveUsuario);
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
                jc = JAXBContext.newInstance(UsuarioAdmin.class);
                xml = connection.getInputStream();
                UsuarioAdmin usuario = new UsuarioAdmin();
                usuario = (UsuarioAdmin) jc.createUnmarshaller().unmarshal(xml);
                System.out.println("Usuario:" +  usuario.getObservaciones());

                if (usuario.getEstado().equals("HABILITADO")){
                    setUsuarioAdmin(usuario);
                    return true;
                }
                else{
                    JOptionPane.showMessageDialog(null,usuario.getObservaciones(),"Error",JOptionPane.WARNING_MESSAGE);            
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
    
    public boolean validarLogin(String nombreUsuario,String claveUsuario){
        CallableStatement s=null;
        ResultSet r=null;
        int li_id;
        String ls_nombreUsuario,ls_clave,ls_apellido,ls_nombre,ls_tipo,ls_estado;
        StringMD md5=new StringMD();
        String ls_clave_MD5;
        
        //Encripto la clave en MD5    
        ls_clave_MD5= md5.getStringMessageDigest(claveUsuario,"MD5");
        
        UsuarioAdmin usuario= new UsuarioAdmin();
        //Conectamos a la base
        Conector conector = new Conector();  
        Connection conexion = conector.connect("administracion");
        
        try {

               s=conexion.prepareCall("{call sp_get_def_usuario_admin ( ? )}",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
               s.setString(1,nombreUsuario);
               
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
                   
                   if (ls_clave.equals(ls_clave_MD5)){
                       usuario.setId(li_id);
                       usuario.setNombreUsuario(ls_nombreUsuario);
                       usuario.setNombreCompletoUsuario(ls_apellido + " " + ls_nombre);
                       usuario.setEstado(ls_estado);
                       setUsuarioAdmin(usuario);
                       if (ls_estado.equals("1"))
                           return true;
                       else{
                           JOptionPane.showMessageDialog(null,"Usuario no habilitado","Error",JOptionPane.WARNING_MESSAGE);                                       
                           return false;
                       }
                           
                   }
                   else{
                       JOptionPane.showMessageDialog(null,"Clave incorrecta","Error",JOptionPane.WARNING_MESSAGE);                                                              
                       return false;
                   }
                       
               }
               else{
                       JOptionPane.showMessageDialog(null,"Usuario no existe","Error",JOptionPane.WARNING_MESSAGE);                                                              
                       return false;
               }
               
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage(),"Exception SQL",JOptionPane.WARNING_MESSAGE);                                                              
            return false;    
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage(),"Exception",JOptionPane.WARNING_MESSAGE);                                                              
            return false;    
            
        } finally {
             try{
                if (conexion != null) conexion.close();   
                if (r != null) r.close();   
             }catch(Exception e)  {
                e.getMessage();
             }
        }

    }
    
    
    
    private void buSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buSalirActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_buSalirActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        Locale.setDefault(new Locale("es", "AR"));
        
        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                    LogIn dialog = new LogIn(new javax.swing.JFrame(), true);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            System.exit(0);
                        }
                    });
                    dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buAceptar;
    private javax.swing.JButton buSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField jPClave;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField jTUsuario;
    private javax.swing.JLabel labelId;
    // End of variables declaration//GEN-END:variables
}
