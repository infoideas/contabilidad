/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package contabilidad;

import entidades.CentroCosto;
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
public class CentrosCostoFrame extends javax.swing.JFrame {
    Vector tableData = new Vector();
    Vector<String> tableHeaders = new Vector<>();

    /**
     * Creates new form provinciasFrame
     */
    public CentrosCostoFrame() {
        initComponents();
        //Listener para cerrar la ventana con la tecla Escape
        addEscapeListener(this);
        
        //Modelo de Table
        creaModelo();
        
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
        setTitle("Centros de Costo Ejercicio: " + PrincipalFrame.ejercicioSel );
        buscaLista();
    }
    
    public DefaultTableModel creaModelo(){
        //Cargo lista
        tableHeaders.clear();
        tableHeaders.add("Id"); 
        tableHeaders.add("Nombre");
        tableHeaders.add("Nombre abreviado");
        
        DefaultTableCellRenderer AlinearIzquierda = new DefaultTableCellRenderer();
        AlinearIzquierda.setHorizontalAlignment(SwingConstants.LEFT);
        DefaultTableCellRenderer AlinearCentro = new DefaultTableCellRenderer();
        AlinearCentro.setHorizontalAlignment(SwingConstants.CENTER);  
        DefaultTableCellRenderer AlinearDerecha = new DefaultTableCellRenderer();
        AlinearDerecha.setHorizontalAlignment(SwingConstants.CENTER);  
        
        tableData.clear();
        //Columnas no editables
        DefaultTableModel modelo = new DefaultTableModel(tableData,tableHeaders)
        {@Override
                public boolean isCellEditable (int fila, int columna) {
                if (columna >= 0)
                    return false;
                
                    return true;
                }
        };
        
        listaData.setModel(modelo);
        //Alíneo las columnas necesarias
        listaData.getColumnModel().getColumn(0).setCellRenderer(AlinearCentro);
        listaData.getColumnModel().getColumn(1).setCellRenderer(AlinearIzquierda);
        listaData.getColumnModel().getColumn(2).setCellRenderer(AlinearIzquierda);        
        listaData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return modelo;
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

    public void consultaRegistro(){
        int li_fila_sel=listaData.getSelectedRow();  //Fila seleccionada
        if (li_fila_sel == -1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro","",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int li_id=Integer.parseInt(listaData.getValueAt(li_fila_sel,0).toString());
        CentroCosto p = (CentroCosto) buscaRegistro(li_id);
        CentroCostoDialog dialog = new CentroCostoDialog(this, false);
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        listaDatos = new javax.swing.JScrollPane();
        listaData = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Centros de costo");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setText("Buscar");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 330, Short.MAX_VALUE)
                .addComponent(jButton5))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2, jButton3, jButton4, jButton5});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton2, jButton3, jButton4, jButton5});

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listaDatos)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(listaDatos, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                CentroCosto p = (CentroCosto) buscaRegistro(li_id);
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
        CentroCosto p = (CentroCosto) buscaRegistro(li_id);
        CentroCostoDialog dialog = new CentroCostoDialog(this, false);
        dialog.setLocationRelativeTo(null);
        dialog.registroSel=p;
        dialog.editaRegistro();
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        CentroCostoDialog dialog = new CentroCostoDialog(this, true);
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
        PrincipalFrame.existeVentanaCC=false;
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger(CentrosCostoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CentrosCostoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CentrosCostoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CentrosCostoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new CentrosCostoFrame().setVisible(true);
            }
        });
    }
    
    private void buscaLista() {
    try {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q=session.createQuery("from CentroCosto a where a.ejercicio = :ejercicioSel order by nombre");
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
        CentroCosto u= (CentroCosto) o;
 
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
       CentroCosto p=(CentroCosto) o;
       
       try{
           session.beginTransaction();
           session.delete(p);
           session.getTransaction().commit();
           JOptionPane.showMessageDialog(null, "Eliminación exitosa","",JOptionPane.INFORMATION_MESSAGE);
           return true;
       }
       catch (HibernateException e){
            session.getTransaction().rollback();
            JOptionPane.showMessageDialog(null,"Error:" + e.getMessage() ,"",JOptionPane.ERROR_MESSAGE);
            return false;
       }
       finally {
            session.close();
            
        }
    }
    
    public Object buscaRegistro(int id){
        CentroCosto p= null;
        Session session=HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            p=(CentroCosto) session.get(CentroCosto.class,id);
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
    DefaultTableModel modelo=creaModelo();
    for(Object o : resultList) {
        CentroCosto p = (CentroCosto)o;
        Vector<Object> oneRow = new Vector<Object>();
        oneRow.add(p.getId());
        oneRow.add(p.getNombre());
        oneRow.add(p.getAbrev());
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTable listaData;
    private javax.swing.JScrollPane listaDatos;
    // End of variables declaration//GEN-END:variables
}
