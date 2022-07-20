/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package contabilidad;

import entidades.Cuenta;
import general.HibernateUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author rafael
 */
public class CuentasFrame extends javax.swing.JFrame {
    Vector tableData = new Vector();
    Vector<String> tableHeaders = new Vector<>();

    /**
     * Creates new form provinciasFrame
     */
    public CuentasFrame() {
        initComponents();
        //Listener para cerrar la ventana con la tecla Escape
        addEscapeListener(this);

        //Cargo lista
        tableHeaders.add("Id"); 
        tableHeaders.add("Número");
        tableHeaders.add("Nivel");
        tableHeaders.add("Nombre");
        
        //Modelo de la tabla 
        DefaultTableCellRenderer AlinearIzquierda = new DefaultTableCellRenderer();
        AlinearIzquierda.setHorizontalAlignment(SwingConstants.LEFT);
        DefaultTableCellRenderer AlinearCentro = new DefaultTableCellRenderer();
        AlinearCentro.setHorizontalAlignment(SwingConstants.CENTER);  
        
        //Columnas no editables
        DefaultTableModel modelo = new DefaultTableModel(tableData,tableHeaders)
        {@Override
                public boolean isCellEditable (int fila, int columna) {
                if (columna >= 0)
                    return false;
                else
                    return true;
                }
        };
        
        listaData.setModel(modelo);
        //Alíneo las columnas necesarias
        listaData.getColumnModel().getColumn(1).setCellRenderer(AlinearIzquierda);
        listaData.getColumnModel().getColumn(2).setCellRenderer(AlinearCentro);        
        listaData.getColumnModel().getColumn(3).setCellRenderer(AlinearIzquierda);
        
        listaData.getColumnModel().getColumn(0).setPreferredWidth(0);
        listaData.getColumnModel().getColumn(0).setMinWidth(0);
        listaData.getColumnModel().getColumn(0).setMaxWidth(0);
        listaData.getColumnModel().getColumn(1).setPreferredWidth(80);
        listaData.getColumnModel().getColumn(2).setPreferredWidth(50);
        listaData.getColumnModel().getColumn(3).setPreferredWidth(300);
        listaData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Agrego listener para doble click en lista de asientos
        listaData.addMouseListener
        (
    	new MouseAdapter()
    	{
    		public void mouseClicked(MouseEvent evnt)
    		{
                  if (evnt.getClickCount() == 2)
                    {
                        JTable target = (JTable)evnt.getSource();
                        int row = target.getSelectedRow(); // select a row
                        int column = target.getSelectedColumn(); // select a column
                        consultaRegistro();                        
                    }
    		}
    	}
        );
        setTitle("Plan de Cuentas Ejercicio: " + PrincipalFrame.ejercicioSel );
        buscaLista();
    }
    
    //Listener para cerrar la ventana con la tecla Escape
    public static void addEscapeListener(final JFrame frame) {
        ActionListener escListener = new ActionListener() 
            { 
              @Override
              public void actionPerformed(ActionEvent e) 
                { 
                    frame.dispose();
                }
            }; 
        frame.getRootPane().registerKeyboardAction(escListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW); 
    }

    private void consultaRegistro(){
        int li_fila_sel=listaData.getSelectedRow();  //Fila seleccionada
        if (li_fila_sel == -1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro","",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int li_id=Integer.parseInt(listaData.getValueAt(li_fila_sel,0).toString());
        Cuenta p = (Cuenta) buscaRegistro(li_id);
        CuentaDialog dialog = new CuentaDialog(this, false);
        dialog.setLocationRelativeTo(null);
        dialog.registroSel=p;
        dialog.editaRegistro();
        dialog.setVisible(true);
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
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        listaDatos = new javax.swing.JScrollPane();
        listaData = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTCuenta = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Plan de Cuentas");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setText("Nuevo");
        jButton2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton3.setText("Editar");
        jButton3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton4.setText("Eliminar");
        jButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton5.setText("Salir");
        jButton5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton6.setText("Insertar sub cuenta");
        jButton6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(131, 131, 131)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        listaData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        listaDatos.setViewportView(listaData);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Cuenta:");

        jButton7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton7.setText("Buscar");
        jButton7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setText("Actualizar ");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listaDatos)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jTCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(listaDatos, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        Object[] opciones = {"Sí","No"};  //Opciones de la ventana de confirmación
        int li_id=0;
        int li_fila_sel=listaData.getSelectedRow();  //Fila seleccionada
        if (li_fila_sel == -1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro","",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        li_id=Integer.parseInt(listaData.getValueAt(li_fila_sel,0).toString());        
        if (li_id > 0){
            int li_respuesta=JOptionPane.showOptionDialog(new JFrame(),"Está seguro de eliminar?","Eliminar rubro",JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE , null,opciones,opciones[0]);
            if (li_respuesta == JOptionPane.YES_OPTION)
            {
                Cuenta p = (Cuenta) buscaRegistro(li_id);
                if (eliminaRegistro(p)){
                    DefaultTableModel modelo = (DefaultTableModel) listaData.getModel();
                    modelo.removeRow(listaData.getSelectedRow());
                    listaData.setModel(modelo);
                }
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int li_fila_sel=listaData.getSelectedRow();  //Fila seleccionada
        if (li_fila_sel == -1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro","",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int li_id=Integer.parseInt(listaData.getValueAt(li_fila_sel,0).toString());
        Cuenta p = (Cuenta) buscaRegistro(li_id);
        CuentaDialog dialog = new CuentaDialog(this, false);
        dialog.setLocationRelativeTo(null);
        dialog.registroSel=p;
        dialog.editaRegistro();
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        CuentaDialog dialog = new CuentaDialog(this, true);
        dialog.setLocationRelativeTo(null);
        dialog.nuevoRegistro();
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        buscaLista();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        PrincipalFrame.existeVentanaCuenta=false;
    }//GEN-LAST:event_formWindowClosing

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        int li_fila_sel=listaData.getSelectedRow();  //Fila seleccionada
        if (li_fila_sel == -1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro","",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int li_id=Integer.parseInt(listaData.getValueAt(li_fila_sel,0).toString());
        Cuenta p = (Cuenta) buscaRegistro(li_id);
        String ls_cuenta="";
        try {
           Session session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           Query q=session.createQuery("select max(a.cuentaNumero) from Cuenta a where a.ejercicio = :ejercicioSel "
                   + "and a.cuentaNumero like :cuentaPadre "
                   + "order by cuentaNumero");
           q.setParameter("ejercicioSel",PrincipalFrame.ejercicioSel);
           q.setParameter("cuentaPadre",p.getCuentaNumero() + "%");
           List resultList = q.list();
           if (resultList.size()==1)
               ls_cuenta=(String) resultList.get(0);
           session.getTransaction().commit();
        } catch (HibernateException he) {
           he.printStackTrace();
        }
        
        short li_nivel=0;
        long ll_cuenta=0;
        if (!ls_cuenta.isEmpty()){
            ll_cuenta=Long.parseLong(ls_cuenta);
            ll_cuenta++;
            ls_cuenta=String.valueOf(ll_cuenta);
            if (p.getImputable()=='1')
               li_nivel=p.getNivel();
            else
               li_nivel=(short) (p.getNivel() + 1);
        }
        
        Cuenta q= new Cuenta();
        q.setEjercicio(PrincipalFrame.ejercicioSel);
        q.setCuentaNumero(ls_cuenta);
        CuentaDialog dialog = new CuentaDialog(this, false);
        dialog.setLocationRelativeTo(null);
        dialog.registroSel=q;
        dialog.insertarSubCuenta(ls_cuenta,li_nivel);
        dialog.setVisible(true);
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        String ls_cuenta;
        String ls_numero,ls_nombre;
        int li_fila_ini=0;
        
        int li_fila_sel=listaData.getSelectedRow();  //Fila seleccionada
        if (li_fila_sel > 0) li_fila_ini=li_fila_sel + 1;
        
        if(jTCuenta.getText().isEmpty() || jTCuenta.getText()==null)
            return;
        
        ls_cuenta=jTCuenta.getText().trim().toUpperCase();
        for(int i=li_fila_ini; i < listaData.getRowCount(); i++){
           ls_nombre=((String) listaData.getValueAt(i,3)).toUpperCase();
           if(ls_nombre.startsWith(ls_cuenta)){
               listaData.changeSelection(i,1,false,false);
               return;
           }

           ls_numero=((String) listaData.getValueAt(i,1)).trim();                       
           if(ls_numero.startsWith(ls_cuenta)){
               listaData.changeSelection(i,1,false,false);
               return;
           }
        }
        int li_id=Integer.parseInt(listaData.getValueAt(li_fila_sel,0).toString());
        
    }//GEN-LAST:event_jButton7ActionPerformed

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
            java.util.logging.Logger.getLogger(CuentasFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CuentasFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CuentasFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CuentasFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new CuentasFrame().setVisible(true);
            }
        });
    }
    
    private void buscaLista() {
    try {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q=session.createQuery("from Cuenta a where a.ejercicio = :ejercicioSel order by cuentaNumero ");
        q.setParameter("ejercicioSel",PrincipalFrame.ejercicioSel);
        List resultList = q.list();
        displayResult(resultList);
        session.getTransaction().commit();
    } catch (HibernateException he) {
        he.printStackTrace();
    }
    }
    
    private boolean grabaRegistro(Object o){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Cuenta u= (Cuenta) o;
 
        try{
            session.beginTransaction();
            session.saveOrUpdate(u);
            session.getTransaction().commit();
        }
        catch (HibernateException e){
            session.getTransaction().rollback();
            return false;
        }
        finally {
            session.close();
            return true;
        }
    }
    
    public boolean eliminaRegistro(Object o) {
       Session session = HibernateUtil.getSessionFactory().openSession();
       Cuenta p=(Cuenta) o;
       
       try{
           session.beginTransaction();
           session.delete(p);
           session.getTransaction().commit();
           JOptionPane.showMessageDialog(null, "Eliminación exitosa","",JOptionPane.INFORMATION_MESSAGE);
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
    
    public Object buscaRegistro(int id){
        Cuenta p= null;
        Session session=HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            p=(Cuenta) session.get(Cuenta.class,id);
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
    
    
    
    private void displayResult(List resultList) {
    
    tableData.clear();
    DefaultTableModel modelo = (DefaultTableModel) listaData.getModel();
    for(Object o : resultList) {
        Cuenta p = (Cuenta)o;
        Vector<Object> oneRow = new Vector<Object>();
        oneRow.add(p.getId());
        short li_nivel=p.getNivel();
        String ls_cuenta="";
        for (short i = 1; i <= li_nivel; ++i) {
            ls_cuenta= ls_cuenta + "    ";
        }
        ls_cuenta=ls_cuenta + p.getCuentaNumero();
        oneRow.add(ls_cuenta);
        oneRow.add(p.getNivel());
        oneRow.add(p.getCuentaNombre());
        modelo.addRow(oneRow);
    }
    
    listaData.setModel(modelo);
}
    
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTCuenta;
    private javax.swing.JTable listaData;
    private javax.swing.JScrollPane listaDatos;
    // End of variables declaration//GEN-END:variables
}
