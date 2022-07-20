/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contabilidad;

import entidades.Ejercicio;
import entidades.Empresa;
import general.HibernateUtil;
import general.UsuarioAdmin;
import static java.awt.Frame.NORMAL;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Propietario
 */

public class SeleccionarEmpresa extends javax.swing.JDialog {
    public boolean mod;
    public boolean wb_nuevo;
    private UsuarioAdmin usuarioAdmin;  //Id del usuario
    public Empresa empresaSel=new Empresa();
    public Ejercicio ejercicioSel;
    Vector tableData = new Vector();
    Vector<String> tableHeaders = new Vector<>();
    Date fec_ini_ejer,fec_fin_ejer;
    
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

    public UsuarioAdmin getUsuarioAdmin() {
        return usuarioAdmin;
    }

    public void setUsuarioAdmin(UsuarioAdmin usuarioAdmin) {
        this.usuarioAdmin = usuarioAdmin;
    }

    
    /**
     * Creates new form RubroDialog
     */
    public SeleccionarEmpresa(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        //Botón Default
        getRootPane().setDefaultButton(buAceptar);
        //Listener para cerrar la ventana con la tecla Escape
        addEscapeListener(this);
    }
    
    public SeleccionarEmpresa() {
        initComponents();
        //Botón Default
        getRootPane().setDefaultButton(buAceptar);
        //Listener para cerrar la ventana con la tecla Escape
        addEscapeListener(this);

        //Cargo lista de empresas y ejercicios 
        DefaultTreeModel arbol = (DefaultTreeModel) jTreeData.getModel();
        arbol=cargaEmpresas(arbol);
        jTreeData.setModel(arbol);
        jTreeData.setShowsRootHandles(true);
        expandeNodos();
        setDefaultCloseOperation(0);  //Deshabilita la X de la ventana
    }


     public void expandeNodos(){
        Enumeration<?> topLevelNodes= ((TreeNode)jTreeData.getModel().getRoot()).children();
        while(topLevelNodes.hasMoreElements()) {
            DefaultMutableTreeNode node= (DefaultMutableTreeNode)topLevelNodes.nextElement();
                jTreeData.expandPath(new TreePath(node.getPath()));
        }
    }
    
    //Listener para cerrar la ventana con la tecla Escape
    public static void addEscapeListener(final JDialog dialog) {
        ActionListener escListener = new ActionListener() 
            { 
              @Override
              public void actionPerformed(ActionEvent e) 
                { 
                    dialog.dispose();
                    System.exit(0);
                }
            }; 
        dialog.getRootPane().registerKeyboardAction(escListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW); 

    }

    //Carga lista de empresas en el Jtree
    public DefaultTreeModel cargaEmpresas(DefaultTreeModel arbol){
        List listaEmpresas=buscaListaEmpresas();
        DefaultMutableTreeNode nodoroot = new DefaultMutableTreeNode("Lista de empresas");
        arbol.setRoot(nodoroot);
        int i=0;
        for(Object o : listaEmpresas) {
            Empresa p = (Empresa)o;
            DefaultMutableTreeNode nodo =new DefaultMutableTreeNode(p);
            nodo=cargaEjercicios(nodo,p);
            arbol.insertNodeInto(nodo, nodoroot,i);  //Indice 0 para agregar al final 
            i++;
        }
        return arbol;
    }
    

    //Carga lista de ejercicios en el Jtree
    public DefaultMutableTreeNode cargaEjercicios(DefaultMutableTreeNode nodoEmpresa,Empresa e){
        List listaEjer=buscaListaEjercicios(e);
        for(Object o : listaEjer) {
            Ejercicio k= (Ejercicio) o;
            DefaultMutableTreeNode nodo =new DefaultMutableTreeNode(k);
            nodo.setAllowsChildren(false);
            nodoEmpresa.add(nodo);
        }
        return nodoEmpresa;
    }
    
    
    
    public void nuevoRegistro(){
        wb_nuevo=true;
    }
    
    public void editaRegistro(){
        wb_nuevo=false;
    }
    
    private boolean actualizaBaseDatos(Object o){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Empresa u= (Empresa) o;
        try{
            session.beginTransaction();
            session.saveOrUpdate(u);
            session.getTransaction().commit();
            return true;
            
        }
        catch (HibernateException e){
            session.getTransaction().rollback();
            return false;
        }
        finally {
            session.close();
        }
    }
    
    public boolean grabaRegistro()
    {
        String ls_nombre;
        Empresa p= this.getEmpresaSel();
        //p.setNombre(ls_nombre);
        
        //Grabo en la base
        if (actualizaBaseDatos(p)){
            JOptionPane.showMessageDialog(null, "Actualización exitosa","",JOptionPane.INFORMATION_MESSAGE);
//            if (wb_nuevo)
//                jTId.setText(Integer.toString(p.getId()));
            
            return true;    
        }
        else
            return false;
        
    }
    
    //Busca lista de empresas
    private List buscaListaEmpresas() {
        List resultList=null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q=session.createQuery("from Empresa order by nombre");
            resultList = q.list();
            //displayResult(resultList);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            JOptionPane.showMessageDialog(null,he.getMessage(),"Error",JOptionPane.WARNING_MESSAGE);            
            return null;    
        }
        return resultList;    
    
    }

    //Busca lista de ejercicios contables de una empresa
    private List buscaListaEjercicios(Empresa e) {
        List resultList=null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q=session.createQuery("from Ejercicio a where a.empresa = :empresaSel order by id");
            q.setParameter("empresaSel",e);
            resultList = q.list();
            session.getTransaction().commit();
        } catch (HibernateException he) {
            JOptionPane.showMessageDialog(null,he.getMessage(),"Error",JOptionPane.WARNING_MESSAGE);            
        }
        return resultList;    
    
    }

    private void displayResult(List resultList) {
        tableData.clear();

        for(Object o : resultList) {
            Empresa p = (Empresa)o;
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(p.getId());
            oneRow.add(p.getNombre());
            tableData.add(oneRow);
        }
//        listaData.setModel(new DefaultTableModel(tableData, tableHeaders));
//        listaData.getColumnModel().getColumn(0).setPreferredWidth(10);
//        listaData.getColumnModel().getColumn(1).setPreferredWidth(50);
    }
    
    public Object buscaRegistro(int id){
        Empresa p= null;
        Session session=HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            p=(Empresa) session.get(Empresa.class,id);
            Hibernate.initialize(p.getEjercicios());
            session.getTransaction().commit();
        }
        catch (HibernateException e){
            session.getTransaction().rollback();
            return false;
       }
        finally {
            session.close();
            return p;
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

        jPanel2 = new javax.swing.JPanel();
        buAceptar = new javax.swing.JButton();
        buSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTreeData = new javax.swing.JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Módulo de Contabiidad --- Seleccionar empresa y ejercicio");
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        buAceptar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        buAceptar.setText("Continuar");
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(355, Short.MAX_VALUE)
                .addComponent(buAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buAceptar, buSalir});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buAceptar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jTreeData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTreeDataMousePressed(evt);
            }
        });
        jTreeData.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTreeDataValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTreeData);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buAceptarActionPerformed
        // TODO add your handling code here:
        PrincipalFrame p = new PrincipalFrame();
        p.setUsuarioAdmin(usuarioAdmin);
        if (ejercicioSel == null){
//            JOptionPane.showMessageDialog(null, "Debe seleccionar un ejercicio","",JOptionPane.INFORMATION_MESSAGE);
//            return;
        }
        else{
            p.setEmpresaSel(empresaSel);
            p.setEjercicioSel(ejercicioSel);
            p.setTitle("Módulo de Contabilidad   -- Empresa: "  + empresaSel.getNombre()+ "    Ejercicio: " + ejercicioSel  + " --");
        }

        dispose();            
        p.setVisible(true);
        p.setLocationRelativeTo(null);
        p.setExtendedState(p.MAXIMIZED_BOTH);
        
    }//GEN-LAST:event_buAceptarActionPerformed

    private void buSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buSalirActionPerformed
        // TODO add your handling code here:
        System.exit(0);
        
    }//GEN-LAST:event_buSalirActionPerformed

    //Detecta ejercicio contable seleccionado
    private void jTreeDataValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTreeDataValueChanged
        // TODO add your handling code here:
        DefaultMutableTreeNode nseleccionado = (DefaultMutableTreeNode) jTreeData.getLastSelectedPathComponent();
        if (nseleccionado.getLevel()==2){
            //Se trata de un ejercicio seleccionado
            ejercicioSel= (Ejercicio) nseleccionado.getUserObject();
            empresaSel = (Empresa) buscaRegistro(ejercicioSel.getEmpresa().getId());
            
            //Fecha de inicio del ejercicio
            java.util.Calendar fec_ini = java.util.Calendar.getInstance();
            fec_ini.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
            fec_ini.set(Calendar.DAY_OF_MONTH,1);
            fec_ini.set(Calendar.MONTH,ejercicioSel.getMesInicio() - 1);
            fec_ini.set(Calendar.YEAR,ejercicioSel.getAnioInicio());
            fec_ini.set(Calendar.HOUR_OF_DAY, 0);
            fec_ini.set(Calendar.MINUTE, 0);
            fec_ini.set(Calendar.SECOND, 0);
            fec_ini.set(Calendar.MILLISECOND, 0);
            fec_ini_ejer=fec_ini.getTime();
            
            //Fecha de fin del ejercicio
            java.util.Calendar fec_fin = java.util.Calendar.getInstance();
            fec_fin.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
            fec_fin.set(Calendar.MONTH,ejercicioSel.getMesFin()- 1);
            fec_fin.set(Calendar.YEAR,ejercicioSel.getAnioFin());
            Calendar cal = GregorianCalendar.getInstance();
            cal.set(ejercicioSel.getAnioFin(),ejercicioSel.getMesFin()- 1,1);
            fec_fin.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));            
            
            fec_fin.set(Calendar.HOUR_OF_DAY, 0);
            fec_fin.set(Calendar.MINUTE, 0);
            fec_fin.set(Calendar.SECOND, 0);
            fec_fin.set(Calendar.MILLISECOND, 0);
            fec_fin_ejer=fec_fin.getTime();

            
        }
        else{
            ejercicioSel=null;
            empresaSel=null;
        }
            
        
    }//GEN-LAST:event_jTreeDataValueChanged

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosed

    private void jTreeDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTreeDataMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount()==2){
            DefaultMutableTreeNode nseleccionado = (DefaultMutableTreeNode) jTreeData.getLastSelectedPathComponent();
            if (nseleccionado.getLevel()==2){
                //Doble click
                System.out.println("Doble click");
                ejercicioSel= (Ejercicio) nseleccionado.getUserObject();
                empresaSel = (Empresa) buscaRegistro(ejercicioSel.getEmpresa().getId());
                
                PrincipalFrame p = new PrincipalFrame();
                p.setEmpresaSel(empresaSel);
                p.setEjercicioSel(ejercicioSel);
                p.setFec_ini_ejer(fec_ini_ejer);
                p.setFec_fin_ejer(fec_fin_ejer);
                p.setUsuarioAdmin(usuarioAdmin);
                p.setTitle("Módulo de Contabilidad   -- Empresa: "  + empresaSel.getNombre()+ "    Ejercicio: " + ejercicioSel  + " --");
                dispose();            
                p.setVisible(true);
                p.setLocationRelativeTo(null);
                p.setExtendedState(p.MAXIMIZED_BOTH);
            }
            
        }
        
    }//GEN-LAST:event_jTreeDataMousePressed

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
            java.util.logging.Logger.getLogger(SeleccionarEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SeleccionarEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SeleccionarEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SeleccionarEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SeleccionarEmpresa dialog = new SeleccionarEmpresa(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTreeData;
    // End of variables declaration//GEN-END:variables
}
