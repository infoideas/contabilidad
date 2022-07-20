/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package contabilidad;

import entidades.Asiento;
import general.BeanBase;
import general.HibernateUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class AsientosFrame extends javax.swing.JFrame {
    Vector tableData = new Vector();
    Vector<String> tableHeaders = new Vector<>();

    /**
     * Creates new form provinciasFrame
     */
    public AsientosFrame() {
        initComponents();
        
        //Buscar botón Default 
        getRootPane().setDefaultButton(buBuscar);        
        
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
        
        //Cargo lista de asientos
        //buscaLista();
    }
    
    
    public DefaultTableModel creaModelo(){
        //Cabecera
        tableHeaders.clear();
        tableHeaders.add("Id"); 
        tableHeaders.add("Descripción");
        tableHeaders.add("Fecha");
        
        //Modelo de la tabla de asientos
        DefaultTableCellRenderer AlinearIzquierda = new DefaultTableCellRenderer();
        AlinearIzquierda.setHorizontalAlignment(SwingConstants.LEFT);
        DefaultTableCellRenderer AlinearCentro = new DefaultTableCellRenderer();
        AlinearCentro.setHorizontalAlignment(SwingConstants.CENTER);  
        
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
        listaData.getColumnModel().getColumn(2).setCellRenderer(AlinearCentro);   
        
        listaData.getColumnModel().getColumn(0).setPreferredWidth(80);
        listaData.getColumnModel().getColumn(1).setPreferredWidth(300);
        listaData.getColumnModel().getColumn(2).setPreferredWidth(200);
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
        Asiento p = (Asiento) buscaRegistro(li_id);
        AsientoDialog dialog = new AsientoDialog();
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
        buBuscar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        panelDatos = new javax.swing.JScrollPane();
        listaData = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jDateDesde = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jDateHasta = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jTDesc = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Asientos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        buBuscar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        buBuscar.setText("Buscar");
        buBuscar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buBuscarActionPerformed(evt);
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
                .addComponent(buBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buBuscar, jButton2, jButton3, jButton4, jButton5});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {buBuscar, jButton2, jButton3, jButton4, jButton5});

        listaData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Descripción", "Fecha"
            }
        ));
        panelDatos.setViewportView(listaData);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Desde:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Hasta:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Descripción:");

        jTDesc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTDescActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jTDesc))
                    .addComponent(jDateHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jDateDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelDatos)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelDatos, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
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
        String ls_permiso;
        BeanBase bean=new BeanBase();        
        //Reviso si tiene permiso
        ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("EliminarAsiento");
        if (!bean.validarPermiso(PrincipalFrame.usuarioAdmin.getNombreUsuario(),ls_permiso,true))
            return;
        
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
                Asiento p = (Asiento) buscaRegistro(li_id);
                if (eliminaRegistro(p)){
                    DefaultTableModel modelo = (DefaultTableModel) listaData.getModel();
                    modelo.removeRow(listaData.getSelectedRow());
                    listaData.setModel(modelo);
                    
                    //Grabo la auditoría de la transacción
                    String ls_observaciones="";
                    ls_observaciones="Eliminación de asiento : " + String.valueOf(p.getId());
                    try {
                        boolean lb_resul=bean.grabaAuditoria(PrincipalFrame.usuarioAdmin.getId(),ls_permiso,ls_observaciones,bean.getNombreAplicacion(),bean.obtieneNombreEquipo());
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(AsientoDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
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
        Asiento p = (Asiento) buscaRegistro(li_id);
        AsientoDialog dialog = new AsientoDialog();
        dialog.setLocationRelativeTo(null);
        dialog.registroSel=p;
        dialog.editaRegistro();
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        AsientoDialog dialog = new AsientoDialog();
        dialog.setLocationRelativeTo(null);
        dialog.nuevoRegistro();
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void buBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buBuscarActionPerformed
        // TODO add your handling code here:
        buscaLista();
    }//GEN-LAST:event_buBuscarActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        PrincipalFrame.existeVentanaAsiento=false;
    }//GEN-LAST:event_formWindowClosing

    private void jTDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTDescActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTDescActionPerformed

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
            java.util.logging.Logger.getLogger(AsientosFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AsientosFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AsientosFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AsientosFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new AsientosFrame().setVisible(true);
            }
        });
    }
    
    private void buscaLista() {
    java.util.Date lda_fec_desde=jDateDesde.getDate();
    java.util.Date lda_fec_hasta=jDateHasta.getDate();
    
    java.util.Calendar fecha_desde = java.util.Calendar.getInstance();
    fecha_desde.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
    fecha_desde.setTime(lda_fec_desde);
    fecha_desde.set(Calendar.HOUR_OF_DAY, 0);
    fecha_desde.set(Calendar.MINUTE, 0);
    fecha_desde.set(Calendar.SECOND, 0);
    fecha_desde.set(Calendar.MILLISECOND, 0);
    java.util.Date lda_fecha_desde = new java.sql.Date(fecha_desde.getTimeInMillis());

    java.util.Calendar fecha_hasta = java.util.Calendar.getInstance();
    fecha_hasta.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
    fecha_hasta.setTime(lda_fec_hasta);
    fecha_hasta.set(Calendar.HOUR_OF_DAY, 0);
    fecha_hasta.set(Calendar.MINUTE, 0);
    fecha_hasta.set(Calendar.SECOND, 0);
    fecha_hasta.set(Calendar.MILLISECOND, 0);
    java.util.Date lda_fecha_hasta = new java.sql.Date(fecha_hasta.getTimeInMillis());
    
    Session session = HibernateUtil.getSessionFactory().openSession();        
    Query q;
    String ls_descripcion;
    String ls_query;
    ls_descripcion=jTDesc.getText();
    
    session = HibernateUtil.getSessionFactory().openSession();        
    if (ls_descripcion == null){
        ls_query="from Asiento a where a.ejercicio = :ejercicioSel "
                + "and a.fecMov >= :fec_desde "
                + "and a.fecMov <= :fec_hasta "
                + "order by a.fecMov";
        
        try {
            session.beginTransaction();
            q=session.createQuery(ls_query);
            q.setParameter("ejercicioSel",PrincipalFrame.ejercicioSel);
            q.setParameter("fec_desde",lda_fecha_desde);
            q.setParameter("fec_hasta",lda_fecha_hasta);
            List resultList = q.list();
            session.getTransaction().commit();        
            displayResult(resultList);
        
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        finally {
            session.close();
        }
        
    }
    else{
        ls_query="from Asiento a where a.ejercicio = :ejercicioSel "
                + "and a.fecMov >= :fec_desde "
                + "and a.fecMov <= :fec_hasta "
                + "and a.descripcion like :descripcion "
                + "order by a.fecMov";
        
    }
        ls_descripcion=ls_descripcion.trim();
        ls_descripcion="%".concat(ls_descripcion).concat("%");
        
        try {
            session.beginTransaction();
            q=session.createQuery(ls_query);
            q.setParameter("ejercicioSel",PrincipalFrame.ejercicioSel);
            q.setParameter("fec_desde",lda_fecha_desde);
            q.setParameter("fec_hasta",lda_fecha_hasta);
            q.setParameter("descripcion",ls_descripcion);
            List resultList = q.list();
            session.getTransaction().commit();        
            displayResult(resultList);
        
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        finally {
            session.close();
        }
    
    }
    
    private boolean grabaRegistro(Object o){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Asiento u= (Asiento) o;
 
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
       Asiento p=(Asiento) o;
       
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
        Asiento p= null;
        Session session=HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            p=(Asiento) session.get(Asiento.class,id);
            Hibernate.initialize(p.getAsientoDets());
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
        
    TimeZone gmtZone = TimeZone.getTimeZone("GMT");
    DateFormat destDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    destDateFormat.setTimeZone(gmtZone);        
    
    DefaultTableModel modelo=creaModelo();
    for(Object o : resultList) {
        Asiento p = (Asiento)o;
        Vector<Object> oneRow = new Vector<Object>();
        oneRow.add(p.getId());
        oneRow.add(p.getDescripcion());
        oneRow.add(destDateFormat.format(p.getFecMov()));
        modelo.addRow(oneRow);
    }
    
    listaData.setModel(modelo);
    
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buBuscar;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private com.toedter.calendar.JDateChooser jDateDesde;
    private com.toedter.calendar.JDateChooser jDateHasta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTDesc;
    private javax.swing.JTable listaData;
    private javax.swing.JScrollPane panelDatos;
    // End of variables declaration//GEN-END:variables
}
