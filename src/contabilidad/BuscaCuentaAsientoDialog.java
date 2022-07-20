/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contabilidad;

import entidades.AsientoDet;
import entidades.CentroCosto;
import entidades.Cuenta;
import entidades.Plantilla;
import entidades.PlantillaDet;
import general.HibernateUtil;
import general.ListaDetalle;
import java.awt.BorderLayout;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Propietario
 */
public class BuscaCuentaAsientoDialog extends javax.swing.JDialog {
    public boolean mod=false;
    public boolean wb_nuevo;
    public AsientoDet cuentaSel=new AsientoDet();

    public AsientoDet getCuentaSel() {
        return cuentaSel;
    }

    public void setCuentaSel(AsientoDet cuentaSel) {
        this.cuentaSel = cuentaSel;
    }

    
    /**
     * Creates new form CuentaDialog
     */
    public BuscaCuentaAsientoDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        //Botón Default
        getRootPane().setDefaultButton(buAceptar);
        //Listener para cerrar la ventana con la tecla Escape
        addEscapeListener(this);
        
        comboDC.removeAllItems();
        ListaDetalle e;
        e= new ListaDetalle();
        e.setCodigo(1);
        e.setNombre("D");
        comboDC.addItem(e);
        
        e= new ListaDetalle();
        e.setCodigo(2);
        e.setNombre("C");
        comboDC.addItem(e);
        
        comboCuenta.removeAllItems();
        
        //Cargo lista de centros de costo
        buscaListaCC();
        
        //Agrego listener para cuando escriba la cuenta
        DocumentListener documentListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent documentEvent) {
                printIt(documentEvent);
            }
            public void insertUpdate(DocumentEvent documentEvent) {
                printIt(documentEvent);
            }
            public void removeUpdate(DocumentEvent documentEvent) {
                printIt(documentEvent);
            }
            
            private void printIt(DocumentEvent documentEvent) {
                DocumentEvent.EventType type = documentEvent.getType();
                String typeString = null;
                if (type.equals(DocumentEvent.EventType.CHANGE)) {
                    typeString = "Change";
                }  else if (type.equals(DocumentEvent.EventType.INSERT)) {
                    typeString = "Insert";
                }  else if (type.equals(DocumentEvent.EventType.REMOVE)) {
                    typeString = "Remove";
                }
                System.out.print("Type : " + typeString);
                Document source = documentEvent.getDocument();
                int length = source.getLength();
                System.out.println("Length: " + length);
                
                try {
                    System.out.println(source.getText(0, length));
                    buscaListaCuentas(source.getText(0, length));
                } catch (BadLocationException ex) {
                    Logger.getLogger(BuscaCuentaAsientoDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
    };
        
    comboCC.setVisible(false);
    labelCC.setVisible(false);    
        
    //Listener para el cambio de item seleccionado en Provincia
    comboCuenta.addItemListener( new ItemListener(){
    @Override
    public void itemStateChanged(ItemEvent e) {
                    if (comboCuenta.getItemCount() > 0) {
                        ListaDetalle o;
                        o = (ListaDetalle) comboCuenta.getSelectedItem();
                        Cuenta p= (Cuenta) buscaCuenta(o.getCodigo());
                        if (p.getCc()=='1'){
                            comboCC.setVisible(true);
                            labelCC.setVisible(true);
                        }
                        else{
                            comboCC.setVisible(false);
                            labelCC.setVisible(false);
                        }
                    }
            }  
    });           
         
         
    jTCuenta.getDocument().addDocumentListener(documentListener);
    jTCuenta.requestFocus();

    }
    
    public void editaRegistro(){
        setTitle("Editar cuenta");
        wb_nuevo=false;
        
        //Cargo combo de cuentas        
        buscaListaCuentas(cuentaSel.getCuenta().getCuentaNumero());
        jTCuenta.setText(cuentaSel.getCuenta().getCuentaNumero());
        jFTValor.setValue(cuentaSel.getValor());
        
        ListaDetalle p;
        p= new ListaDetalle(cuentaSel.getCuenta().getId(),cuentaSel.getCuenta().getCuentaNumero().concat(" - ").concat(cuentaSel.getCuenta().getCuentaNombre()));
        comboCuenta.setSelectedItem(p);

        if (cuentaSel.getDc()=='D')
            p= new ListaDetalle(1,"D");
        else
            p= new ListaDetalle(2,"C");
        comboDC.setSelectedItem(p);
        
        //Centro de costo si lo tiene
        if(cuentaSel.getCentroCosto()!= null){
            ListaDetalle r;
            r= new ListaDetalle(cuentaSel.getCentroCosto().getId(),cuentaSel.getCentroCosto().getNombre());
            comboCC.setSelectedItem(r);
        }
        jFTValor.requestFocusInWindow();
        jFTValor.selectAll();
        
        
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

    
    private void buscaListaCuentas(String patron) {
    if (patron==null)
        patron="";
    String ls_par_patron="";
    if (esNumero(patron))
      ls_par_patron=patron.concat("%");
    else
      ls_par_patron="%".concat(patron).concat("%");        
    
    try {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q=session.createQuery("from Cuenta a where a.ejercicio = :ejercicioSel "
                + "and (a.cuentaNumero like :cuentaPatron  or a.cuentaNombre like :cuentaPatron ) "
                + "and imputable='1' "
                + "order by cuentaNumero");
        q.setParameter("ejercicioSel",PrincipalFrame.ejercicioSel);
        q.setParameter("cuentaPatron",ls_par_patron);
        List resultList = q.list();
        session.getTransaction().commit();
        
        comboCuenta.removeAllItems();        
        for(Object o : resultList) {
            Cuenta p = (Cuenta)o;
            ListaDetalle e;
            e= new ListaDetalle();
            e.setCodigo(p.getId());
            e.setNombre(p.getCuentaNumero().concat(" - ").concat(p.getCuentaNombre()));
            comboCuenta.addItem(e);
        }   
        
        
    } catch (HibernateException he) {
        he.printStackTrace();
    }
    }
    
    
    //Carga lista de centros de costo
    private void buscaListaCC() {
    try {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q=session.createQuery("from CentroCosto a where a.ejercicio = :ejercicioSel "
                + "order by nombre");
        q.setParameter("ejercicioSel",PrincipalFrame.ejercicioSel);
        List resultList = q.list();
        session.getTransaction().commit();
        
        comboCC.removeAllItems();        
        for(Object o : resultList) {
            CentroCosto p = (CentroCosto)o;
            ListaDetalle e;
            e= new ListaDetalle();
            e.setCodigo(p.getId());
            e.setNombre(p.getNombre());
            comboCC.addItem(e);
        }   
        
        
    } catch (HibernateException he) {
        he.printStackTrace();
    }
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
    
    public boolean grabaRegistro()
    {
        //Cuenta seleccionada
        
        ListaDetalle t= (ListaDetalle) comboCuenta.getSelectedItem();   
        if (t==null){
            JOptionPane.showMessageDialog(null,"Debe seleccionar una cuenta","Error",JOptionPane.WARNING_MESSAGE);
            jTCuenta.requestFocus();
            return false;
        }
        
        Object ls_valor=jFTValor.getValue();
        if (ls_valor == null){
            JOptionPane.showMessageDialog(null,"Debe ingresar un valor","Error",JOptionPane.WARNING_MESSAGE);
            jFTValor.requestFocus();            
            return false;
        }
        
        Cuenta c= (Cuenta) buscaCuenta(t.getCodigo());
        cuentaSel.setCuenta(c);
        
        //Verifico si la cuenta lleva centro de costo
        if (c.getCc()=='1'){
            ListaDetalle u= (ListaDetalle) comboCC.getSelectedItem();   
            if (u==null){
                JOptionPane.showMessageDialog(null,"Debe seleccionar un centro de costo","Error",JOptionPane.WARNING_MESSAGE);
                comboCC.requestFocus();
                return false;
            }
            CentroCosto d= (CentroCosto) buscaCC(u.getCodigo());
            cuentaSel.setCentroCosto(d);
        }
        
        
        ListaDetalle dc= (ListaDetalle) comboDC.getSelectedItem();                
        cuentaSel.setDc(dc.getNombre().equals("D") ? 'D' : 'C' );   
        
        double ld_valor;
        ld_valor=Double.valueOf(jFTValor.getValue().toString());
        ld_valor=Math.round(ld_valor*100d)/100d;
        cuentaSel.setValor(BigDecimal.valueOf(ld_valor));
        mod=true;
        return true;
    }
    
    public Object buscaCuenta(int id){
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
    
    public Object buscaCC(int id){
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
    
    //Verifica si la entrada es un número
    public boolean esNumero(String valor){
        boolean isNumeric = true;
        for (int i = 0; i < valor.length(); i++) {
            if (!Character.isDigit(valor.charAt(i))) {
                isNumeric = false;
            }
        }
        return isNumeric;
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
        jLabel2 = new javax.swing.JLabel();
        comboCuenta = new javax.swing.JComboBox();
        comboDC = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTCuenta = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        buAceptar = new javax.swing.JButton();
        buSalir = new javax.swing.JButton();
        jFTValor = new javax.swing.JFormattedTextField();
        comboCC = new javax.swing.JComboBox();
        labelCC = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Cuenta:");

        comboCuenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        comboCuenta.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboCuenta.setAutoscrolls(true);
        comboCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCuentaActionPerformed(evt);
            }
        });
        comboCuenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboCuentaKeyPressed(evt);
            }
        });

        comboDC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        comboDC.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("D/C:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Valor:");

        jTCuenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTCuentaActionPerformed(evt);
            }
        });

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(124, 124, 124))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buAceptar, buSalir});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buAceptar, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(buSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jFTValor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));
        jFTValor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        comboCC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        comboCC.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        labelCC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelCC.setText("Centro de costo:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jFTValor, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(labelCC)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboCC, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(comboCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelId, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(122, 122, 122)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboDC, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {comboCuenta, jTCuenta});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(labelId)
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFTValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCC)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboDC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buAceptarActionPerformed
        // TODO add your handling code here:
        if (grabaRegistro())
           dispose();
    }//GEN-LAST:event_buAceptarActionPerformed

    private void buSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buSalirActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_buSalirActionPerformed

    private void comboCuentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboCuentaKeyPressed
        // TODO add your handling code here:
        //Busca cuentas cuando escribe
        
    }//GEN-LAST:event_comboCuentaKeyPressed

    private void comboCuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCuentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCuentaActionPerformed

    private void jTCuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTCuentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTCuentaActionPerformed

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
            java.util.logging.Logger.getLogger(BuscaCuentaAsientoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuscaCuentaAsientoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuscaCuentaAsientoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuscaCuentaAsientoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                    BuscaCuentaAsientoDialog dialog = new BuscaCuentaAsientoDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox comboCC;
    private javax.swing.JComboBox comboCuenta;
    private javax.swing.JComboBox comboDC;
    private javax.swing.JFormattedTextField jFTValor;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField jTCuenta;
    private javax.swing.JLabel labelCC;
    private javax.swing.JLabel labelId;
    // End of variables declaration//GEN-END:variables
}
