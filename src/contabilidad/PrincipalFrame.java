/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package contabilidad;

import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;
import com.jtattoo.plaf.graphite.GraphiteLookAndFeel;
import entidades.Ejercicio;
import entidades.Empresa;
import general.BeanBase;
import general.ImagenFondo;
import general.ItemMenu;
import general.UsuarioAdmin;
import java.awt.Window;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import reportes.BalanceComprobacion;
import reportes.MayorCuenta;
import reportes.MayorCuentaCC;

/**
 *
 * @author rafael
 */
public class PrincipalFrame extends javax.swing.JFrame {
    public static Empresa empresaSel;  //Guarda la empresa seleccionada
    public static Ejercicio ejercicioSel;  //Guarda el ejercicio seleccionado
    public static UsuarioAdmin usuarioAdmin;  //Usuario
    public static boolean existeVentanaCuenta=false;
    public static boolean existeVentanaPlantilla=false;
    public static boolean existeVentanaAsiento=false;
    public static boolean existeVentanaCC=false;
    public static boolean existeVentanaEmpresa=false;
    public static boolean existeVentanaEjercicio=false;
    public static boolean existeVentanaMayor=false;
    public static boolean existeVentanaMayorCC=false;
    public static boolean existeVentanaBalance=false;
    CuentasFrame ventanaCuenta; 
    PlantillasFrame ventanaPlantilla;
    AsientosFrame ventanaAsiento;
    CentrosCostoFrame ventanaCC;
    EmpresasFrame ventanaEmpresa;
    EjerciciosFrame ventanaEjercicio;
    MayorCuenta ventanaMayor;
    MayorCuentaCC ventanaMayorCC;
    BalanceComprobacion ventanaBalance;
    public static Date fec_ini_ejer,fec_fin_ejer;
    
    public Empresa getEmpresaSel() {
        return empresaSel;
    }

    public void setEmpresaSel(Empresa empresaSel) {
        this.empresaSel = empresaSel;
    }

    public Ejercicio getEjercicioSel() {
        return ejercicioSel;
    }

    public void setEjercicioSel(Ejercicio ejercicioSel) {
        this.ejercicioSel = ejercicioSel;
    }

    public static boolean isExisteVentanaCuenta() {
        return existeVentanaCuenta;
    }

    public static void setExisteVentanaCuenta(boolean existeVentanaCuenta) {
        PrincipalFrame.existeVentanaCuenta = existeVentanaCuenta;
    }

    public CuentasFrame getVentanaCuenta() {
        return ventanaCuenta;
    }

    public void setVentanaCuenta(CuentasFrame ventanaCuenta) {
        this.ventanaCuenta = ventanaCuenta;
    }

    public UsuarioAdmin getUsuarioAdmin() {
        return usuarioAdmin;
    }

    public void setUsuarioAdmin(UsuarioAdmin usuarioAdmin) {
        PrincipalFrame.usuarioAdmin = usuarioAdmin;
    }

    public static Date getFec_ini_ejer() {
        return fec_ini_ejer;
    }

    public static void setFec_ini_ejer(Date fec_ini_ejer) {
        PrincipalFrame.fec_ini_ejer = fec_ini_ejer;
    }

    public static Date getFec_fin_ejer() {
        return fec_fin_ejer;
    }

    public static void setFec_fin_ejer(Date fec_fin_ejer) {
        PrincipalFrame.fec_fin_ejer = fec_fin_ejer;
    }

    
     
    /**
     * Creates new form principalFrame
     */
    public PrincipalFrame() {
        initComponents();
        ImagenFondo img= new ImagenFondo("/imagenes/logo.jpg");
        add(img);
        
        DefaultTreeModel arbol = (DefaultTreeModel) jTreeMenu.getModel();
        arbol=cargaMenu(arbol);
        jTreeMenu.setModel(arbol);
        jTreeMenu.setShowsRootHandles(true);
        expandeNodos();
    }
    
    public DefaultTreeModel cargaMenu(DefaultTreeModel arbol){
            DefaultMutableTreeNode nodoroot = new DefaultMutableTreeNode("Menú principal");
            arbol.setRoot(nodoroot);

            ItemMenu k;
            DefaultMutableTreeNode nodoMenu,nodoMenuItem;
            
            //Menú Administrador
            k= new ItemMenu();
            k.setId(1);
            k.setNombre("Administrador");
            nodoMenu =new DefaultMutableTreeNode(k);
            
            k= new ItemMenu();
            k.setId(2);
            k.setNombre("Empresas");
            k.setVentana("EmpresasFrame");
            nodoMenuItem =new DefaultMutableTreeNode(k);
            nodoMenuItem.setAllowsChildren(false);
            nodoMenu.add(nodoMenuItem);
            
            k= new ItemMenu();
            k.setId(3);
            k.setNombre("Ejercicios");
            k.setVentana("EjerciciosFrame");
            nodoMenuItem =new DefaultMutableTreeNode(k);
            nodoMenuItem.setAllowsChildren(false);
            nodoMenu.add(nodoMenuItem);

            k= new ItemMenu();
            k.setId(4);
            k.setNombre("Cambiar empresa/ejercicio");
            k.setVentana("SeleccionarEmpresa");
            nodoMenuItem =new DefaultMutableTreeNode(k);
            nodoMenuItem.setAllowsChildren(false);
            nodoMenu.add(nodoMenuItem);
            
            arbol.insertNodeInto(nodoMenu,nodoroot,0);  //Indice 0 para agregar al final 

            //Menú ejercicio contable
            DefaultMutableTreeNode nodoMenuEjer;
            k= new ItemMenu();
            k.setId(5);
            k.setNombre("Ejercicio contable");
            nodoMenuEjer =new DefaultMutableTreeNode(k);
            
            k= new ItemMenu();
            k.setId(6);
            k.setNombre("Plan de cuentas");
            k.setVentana("CuentasFrame");
            nodoMenuItem =new DefaultMutableTreeNode(k);
            nodoMenuItem.setAllowsChildren(false);
            nodoMenuEjer.add(nodoMenuItem);
            
            k= new ItemMenu();
            k.setId(7);
            k.setNombre("Plantillas");
            k.setVentana("PlantillasFrame");
            nodoMenuItem =new DefaultMutableTreeNode(k);
            nodoMenuItem.setAllowsChildren(false);
            nodoMenuEjer.add(nodoMenuItem);

            k= new ItemMenu();
            k.setId(8);
            k.setNombre("Centros de costo");
            k.setVentana("CentrosCostoFrame");
            nodoMenuItem =new DefaultMutableTreeNode(k);
            nodoMenuItem.setAllowsChildren(false);
            nodoMenuEjer.add(nodoMenuItem);
            
            k= new ItemMenu();
            k.setId(9);
            k.setNombre("Períodos");
            k.setVentana("PeriodosFrame");
            nodoMenuItem =new DefaultMutableTreeNode(k);
            nodoMenuItem.setAllowsChildren(false);
            nodoMenuEjer.add(nodoMenuItem);
            
            arbol.insertNodeInto(nodoMenuEjer,nodoroot,1);  //Indice 0 para agregar al final 
            
            //Menú Asientos Contables
            k= new ItemMenu();
            k.setId(10);
            k.setNombre("Asientos");
            nodoMenu =new DefaultMutableTreeNode(k);
            
            k= new ItemMenu();
            k.setId(11);
            k.setNombre("Nuevo asiento");
            k.setVentana("AsientoDialog");
            nodoMenuItem =new DefaultMutableTreeNode(k);
            nodoMenuItem.setAllowsChildren(false);
            nodoMenu.add(nodoMenuItem);
            
            k= new ItemMenu();
            k.setId(12);
            k.setNombre("Buscar asientos por fecha / descripción");
            k.setVentana("AsientosFrame");
            nodoMenuItem =new DefaultMutableTreeNode(k);
            nodoMenuItem.setAllowsChildren(false);
            nodoMenu.add(nodoMenuItem);

            k= new ItemMenu();
            k.setId(13);
            k.setNombre("Buscar asiento por número");
            k.setVentana("BuscaAsientoId");
            nodoMenuItem =new DefaultMutableTreeNode(k);
            nodoMenuItem.setAllowsChildren(false);
            nodoMenu.add(nodoMenuItem);
            
            arbol.insertNodeInto(nodoMenu, nodoroot,2);  //Indice 0 para agregar al final 
            
            //Menú Reportes
            k= new ItemMenu();
            k.setId(14);
            k.setNombre("Reportes");
            nodoMenu =new DefaultMutableTreeNode(k);
            
            k= new ItemMenu();
            k.setId(15);
            k.setNombre("Mayor por cuenta");
            k.setVentana("MayorCuenta");
            nodoMenuItem =new DefaultMutableTreeNode(k);
            nodoMenuItem.setAllowsChildren(false);
            nodoMenu.add(nodoMenuItem);
            
            k= new ItemMenu();
            k.setId(151);
            k.setNombre("Mayor por cuenta por Centro de Costo");
            k.setVentana("MayorCuentaCC");
            nodoMenuItem =new DefaultMutableTreeNode(k);
            nodoMenuItem.setAllowsChildren(false);
            nodoMenu.add(nodoMenuItem);            
            
            k= new ItemMenu();
            k.setId(16);
            k.setNombre("Balance de comprobación");
            k.setVentana("BalanceComprobacion");
            nodoMenuItem =new DefaultMutableTreeNode(k);
            nodoMenuItem.setAllowsChildren(false);
            nodoMenu.add(nodoMenuItem);

            arbol.insertNodeInto(nodoMenu, nodoroot,3);  //Indice 0 para agregar al final 
            
            //Menú Ayuda
            k= new ItemMenu();
            k.setId(17);
            k.setNombre("Ayuda");
            nodoMenu =new DefaultMutableTreeNode(k);
            
            k= new ItemMenu();
            k.setId(18);
            k.setNombre("AcercaDe");
            nodoMenuItem =new DefaultMutableTreeNode(k);
            nodoMenuItem.setAllowsChildren(false);
            nodoMenu.add(nodoMenuItem);
            
            arbol.insertNodeInto(nodoMenu, nodoroot,4);  //Indice 0 para agregar al final 
            
            return arbol;
    }
    

    public void expandeNodos(){
        Enumeration<?> topLevelNodes= ((TreeNode)jTreeMenu.getModel().getRoot()).children();
        while(topLevelNodes.hasMoreElements()) {
            DefaultMutableTreeNode node= (DefaultMutableTreeNode)topLevelNodes.nextElement();
                jTreeMenu.expandPath(new TreePath(node.getPath()));
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

        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTreeMenu = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();

        jMenuItem11.setText("jMenuItem11");

        jMenuItem2.setText("jMenuItem2");

        jMenuItem4.setText("jMenuItem4");

        jMenu4.setText("jMenu4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Módulo de Contabilidad");

        jTreeMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTreeMenuMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTreeMenu);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LOGO.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 696, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(195, 195, 195)
                    .addComponent(jLabel1)
                    .addContainerGap(53, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(98, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addContainerGap(98, Short.MAX_VALUE)))
        );

        jMenu1.setText("Archivo");

        jMenuItem1.setText("Salir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu5.setText("Ayuda");

        jMenuItem12.setText("Acerca de..");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem12);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jTreeMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTreeMenuMousePressed
        // TODO add your handling code here:
        String ls_permiso;
        BeanBase bean;
        if (evt.getClickCount()==2){
            DefaultMutableTreeNode nseleccionado = (DefaultMutableTreeNode) jTreeMenu.getLastSelectedPathComponent();
            if (nseleccionado.getLevel()==2){
                //Doble click
                System.out.println("Doble click");
                ItemMenu itemMenu=(ItemMenu) nseleccionado.getUserObject();
                
                if (itemMenu.getVentana() != null){
                    switch (itemMenu.getVentana()){
                    case "EmpresasFrame":
                        //Reviso si tiene permiso
                        ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("DefinicionEmpresas");
                        bean= new BeanBase();
                        if (!bean.validarPermiso(PrincipalFrame.usuarioAdmin.getNombreUsuario(),ls_permiso,true))
                        return;
                        
                        if (existeVentanaEmpresa==false){
                            ventanaEmpresa= new EmpresasFrame();
                            existeVentanaEmpresa=true;
                        }
                        ventanaEmpresa.setVisible(true);
                        ventanaEmpresa.setLocationRelativeTo(null);
                        ventanaEmpresa.setState(NORMAL);
                        break;
                        
                    case "EjerciciosFrame":    
                        //Reviso si tiene permiso
                        ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("EjerciciosContables");
                        bean= new BeanBase();
                        if (!bean.validarPermiso(PrincipalFrame.usuarioAdmin.getNombreUsuario(),ls_permiso,true))
                        return;
                        
                        if (existeVentanaEjercicio==false){
                            ventanaEjercicio= new EjerciciosFrame();
                            existeVentanaEjercicio=true;
                        }
                        ventanaEjercicio.setVisible(true);
                        ventanaEjercicio.setLocationRelativeTo(null);
                        ventanaEjercicio.setState(NORMAL);
                        break;
                        
                    case "SeleccionarEmpresa":
                        if (existeVentanaCuenta){
                            existeVentanaCuenta=false;
                            ventanaCuenta.dispose();
                        }
        
                        if (existeVentanaPlantilla){
                            existeVentanaPlantilla=false;
                            ventanaPlantilla.dispose();
                        }        
        
                        if (existeVentanaEjercicio){
                            existeVentanaEjercicio=false;
                            ventanaEjercicio.dispose();
                        }        
        
                        if (existeVentanaCC){
                            existeVentanaCC=false;
                            ventanaCC.dispose();
                        }        

                        if (existeVentanaAsiento){
                            existeVentanaAsiento=false;
                            ventanaAsiento.dispose();
                        }        
                        dispose();
        
                        SeleccionarEmpresa dialog = new SeleccionarEmpresa();
                        dialog.setLocationRelativeTo(null);
                        dialog.setUsuarioAdmin(usuarioAdmin);
                        dialog.setVisible(true);
                        break;
                        
                    case "CuentasFrame":    
                        //Reviso si tiene permiso
                        ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("PlanCuentas");
                        bean= new BeanBase();
                        if (!bean.validarPermiso(PrincipalFrame.usuarioAdmin.getNombreUsuario(),ls_permiso,true))
                        return;
                        
                       if (existeVentanaCuenta==false){
                            ventanaCuenta= new CuentasFrame();
                            existeVentanaCuenta=true;
                        }
                        ventanaCuenta.setVisible(true);
                        ventanaCuenta.setLocationRelativeTo(null);
                        ventanaCuenta.setState(NORMAL); 
                        break;
                    
                    case "PlantillasFrame":           
                        //Reviso si tiene permiso
                        ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("Plantillas");
                        bean= new BeanBase();
                        if (!bean.validarPermiso(PrincipalFrame.usuarioAdmin.getNombreUsuario(),ls_permiso,true))
                        return;
                        
                        if (existeVentanaPlantilla==false){
                            ventanaPlantilla= new PlantillasFrame();
                            existeVentanaPlantilla=true;
                        }
                        ventanaPlantilla.setVisible(true);
                        ventanaPlantilla.setLocationRelativeTo(null);
                        ventanaPlantilla.setState(NORMAL);
                        break;
                        
                    case "CentrosCostoFrame" :
                        //Reviso si tiene permiso
                        ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("CentrosCosto");
                        bean= new BeanBase();
                        if (!bean.validarPermiso(PrincipalFrame.usuarioAdmin.getNombreUsuario(),ls_permiso,true))
                        return;

                        if (existeVentanaCC==false){
                            ventanaCC= new CentrosCostoFrame();
                            existeVentanaCC=true;
                        }
                        ventanaCC.setVisible(true);
                        ventanaCC.setLocationRelativeTo(null);
                        ventanaCC.setState(NORMAL);
                        break;
                        
                        
                    case "PeriodosFrame":
                        //Reviso si tiene permiso
                        ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("Periodos");
                        bean= new BeanBase();
                        if (!bean.validarPermiso(PrincipalFrame.usuarioAdmin.getNombreUsuario(),ls_permiso,true))
                        return;

                        PeriodosFrame periodo = new PeriodosFrame();
                        periodo.setLocationRelativeTo(null);
                        periodo.setVisible(true);
                        break;
                        
                    case "AsientoDialog":
                        //Reviso si tiene permiso
                        ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("RealizarAsiento");
                        bean= new BeanBase();
                        if (!bean.validarPermiso(PrincipalFrame.usuarioAdmin.getNombreUsuario(),ls_permiso,true))
                        return;
                        
                        AsientoDialog asiento= new AsientoDialog();
                        asiento.nuevoRegistro();
                        asiento.setVisible(true);
                        asiento.setLocationRelativeTo(null);
                        break;
                        
                    case "AsientosFrame":
                        //Reviso si tiene permiso
                        ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("BuscarAsientoFecha");
                        bean= new BeanBase();
                        if (!bean.validarPermiso(PrincipalFrame.usuarioAdmin.getNombreUsuario(),ls_permiso,true))
                        return;
                        
                        if (existeVentanaAsiento==false){
                            ventanaAsiento= new AsientosFrame();
                            existeVentanaAsiento=true;
                        }
                        ventanaAsiento.setVisible(true);
                        ventanaAsiento.setLocationRelativeTo(null);
                        ventanaAsiento.setState(NORMAL);
                        break;
                        
                    case "BuscaAsientoId":
                        //Reviso si tiene permiso
                        ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("BuscarAsientoNumero");
                        bean= new BeanBase();
                        if (!bean.validarPermiso(PrincipalFrame.usuarioAdmin.getNombreUsuario(),ls_permiso,true))
                        return;
                        
                        BuscaAsientoId asientoId = new BuscaAsientoId();
                        asientoId.setLocationRelativeTo(null);
                        asientoId.setVisible(true);
                        break;
                        
                    case "MayorCuenta":
                        if (existeVentanaMayor==false){
                            ventanaMayor= new MayorCuenta();
                            existeVentanaMayor=true;
                        }
                        ventanaMayor.setVisible(true);
                        ventanaMayor.setLocationRelativeTo(null);
                        ventanaMayor.setState(NORMAL);
                        break;
                        
                    case "MayorCuentaCC":
                        if (existeVentanaMayorCC==false){
                            ventanaMayorCC= new MayorCuentaCC();
                            existeVentanaMayorCC=true;
                        }
                        ventanaMayorCC.setVisible(true);
                        ventanaMayorCC.setLocationRelativeTo(null);
                        ventanaMayorCC.setState(NORMAL);
                        break;
                        
                    case "BalanceComprobacion":
                        if (existeVentanaBalance==false){
                            ventanaBalance= new BalanceComprobacion();
                            existeVentanaBalance=true;
                        }
                        ventanaBalance.setVisible(true);
                        ventanaBalance.setLocationRelativeTo(null);
                        ventanaBalance.setState(NORMAL);   
                        break;
                        
                    } 

                    
                }
                
                
                
            }
            
        }
    }//GEN-LAST:event_jTreeMenuMousePressed

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
            java.util.logging.Logger.getLogger(PrincipalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrincipalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrincipalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrincipalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrincipalFrame().setVisible(true);
                
                }
        });
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTreeMenu;
    // End of variables declaration//GEN-END:variables
}
