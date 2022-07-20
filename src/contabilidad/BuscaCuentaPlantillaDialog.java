/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contabilidad;

import entidades.CentroCosto;
import entidades.Cuenta;
import entidades.PlantillaParam;
import entidades.Plantilla;
import entidades.PlantillaDet;
import general.HibernateUtil;
import general.ListaDetalle;
import java.awt.BorderLayout;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
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
public class BuscaCuentaPlantillaDialog extends javax.swing.JDialog {
    public boolean mod;
    public boolean wb_nuevo;
    public PlantillaDet cuentaSel=new PlantillaDet();
    public Plantilla plantillaSel= new Plantilla();

    public PlantillaDet getCuentaSel() {
        return cuentaSel;
    }

    public void setCuentaSel(PlantillaDet cuentaSel) {
        this.cuentaSel = cuentaSel;
    }

    public Plantilla getPlantillaSel() {
        return plantillaSel;
    }

    public void setPlantillaSel(Plantilla plantillaSel) {
        this.plantillaSel = plantillaSel;
    }
    
    

    /**
     * Creates new form CuentaDialog
     */
    public BuscaCuentaPlantillaDialog(java.awt.Dialog parent, boolean modal) {
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
                    Logger.getLogger(BuscaCuentaPlantillaDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
    };
    jTCuenta.getDocument().addDocumentListener(documentListener);
    jTCuenta.requestFocus();

    DefaultListModel modelOper = new DefaultListModel<>();
    modelOper.addElement("+");
    modelOper.addElement("-");
    modelOper.addElement("*");
    modelOper.addElement("/");
    
    listaOperadores.setModel(modelOper);
    //se cambia la orientación de presentación y el ajuste
    listaOperadores.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    
    //Agrego listener para doble click en lista de operadores
    listaOperadores.addMouseListener
        (
    	new MouseAdapter()
    	{
    		public void mouseClicked(MouseEvent evnt)
    		{
                  if (evnt.getClickCount() == 2)
                    {
                  	String ls_formula=jTFormula.getText();
                        if (ls_formula==null)
                            ls_formula="";
        
                        String ls_oper=(String)listaOperadores.getSelectedValue();        
                        if (!ls_formula.equals(""))
                            ls_oper=" " + ls_oper + " ";
        
                        ls_formula= ls_formula + ls_oper;
                        jTFormula.setText(ls_formula);

                    }
    		}
    	}
        );
    
    
    //Agrego listener para doble click en lista de parámetros
    listaPar.addMouseListener
        (
    	new MouseAdapter()
    	{
    		public void mouseClicked(MouseEvent evnt)
    		{
                  if (evnt.getClickCount() == 2)
                    {
                  	String ls_formula=jTFormula.getText();
                        if (ls_formula==null)
                            ls_formula="";
        
                        String ls_par="{" + (String)listaPar.getSelectedValue() + "}";
                        if (!ls_formula.equals(""))
                            ls_par=" " + ls_par + " ";
                        ls_formula= ls_formula + ls_par;
                        jTFormula.setText(ls_formula);
                    }
    		}
    	}
        );
    
    
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

    
    public void nuevoRegistro(){
        setTitle("Nueva cuenta");
        wb_nuevo=true;
        jTCuenta.requestFocus();
        
        DefaultListModel modelPar = new DefaultListModel<>();
        for (PlantillaParam q : plantillaSel.getPlantillaParams()) {
            modelPar.addElement(q.getNombre());
        }
        listaPar.setModel(modelPar);
    }
    
    public void editaRegistro(){
        setTitle("Editar cuenta");
        wb_nuevo=false;
        
        //Cargo combo de cuentas        
        buscaListaCuentas(cuentaSel.getCuenta().getCuentaNumero());
        jTCuenta.setText(cuentaSel.getCuenta().getCuentaNumero());
        
        ListaDetalle p;
        p= new ListaDetalle(cuentaSel.getCuenta().getId(),cuentaSel.getCuenta().getCuentaNumero().concat(" - ").concat(cuentaSel.getCuenta().getCuentaNombre()));
        comboCuenta.setSelectedItem(p);

        if (cuentaSel.getDc()=='D')
            p= new ListaDetalle(1,"D");
        else
            p= new ListaDetalle(2,"C");
        comboDC.setSelectedItem(p);
        
        jTFormula.setText(cuentaSel.getFormula());
        
        DefaultListModel modelPar = new DefaultListModel<>();
        for (PlantillaParam q : plantillaSel.getPlantillaParams()) {
            modelPar.addElement(q.getNombre());
        }
        listaPar.setModel(modelPar);
    
        
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
        String ls_formula;
        ListaDetalle t= (ListaDetalle) comboCuenta.getSelectedItem();   
        if (t==null){
            JOptionPane.showMessageDialog(null,"Debe seleccionar una cuenta","Error",JOptionPane.WARNING_MESSAGE);
            jTCuenta.requestFocus();
            return false;
        }
        
        Cuenta c= (Cuenta) buscaCuenta(t.getCodigo());
        cuentaSel.setCuenta(c);
        
        ListaDetalle dc= (ListaDetalle) comboDC.getSelectedItem();                
        cuentaSel.setDc(dc.getNombre().equals("D") ? 'D' : 'C' );   

        ls_formula=jTFormula.getText();
        cuentaSel.setFormula(ls_formula);
        
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jTFormula = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jTCuenta = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaPar = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaOperadores = new javax.swing.JList();
        buAceptar = new javax.swing.JButton();
        buSalir = new javax.swing.JButton();
        labelPar = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

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

        jTFormula.setColumns(20);
        jTFormula.setRows(5);
        jScrollPane2.setViewportView(jTFormula);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Fórmula:");

        jTCuenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTCuentaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 383, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
        );

        listaPar.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listaPar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listaParMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(listaPar);

        listaOperadores.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listaOperadores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listaOperadoresMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(listaOperadores);

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

        labelPar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelPar.setText("Parámetros:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Operadores:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(labelId)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(0, 37, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTCuenta)
                            .addComponent(comboDC, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboCuenta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelPar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buAceptar, buSalir});

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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(comboDC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(126, 126, 126)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelPar)
                            .addComponent(jLabel4))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(buAceptar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void listaParMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaParMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_listaParMousePressed

    private void listaOperadoresMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaOperadoresMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_listaOperadoresMousePressed

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
            java.util.logging.Logger.getLogger(BuscaCuentaPlantillaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuscaCuentaPlantillaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuscaCuentaPlantillaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuscaCuentaPlantillaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                    BuscaCuentaPlantillaDialog dialog = new BuscaCuentaPlantillaDialog(new javax.swing.JDialog(), true);
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
    private javax.swing.JComboBox comboCuenta;
    private javax.swing.JComboBox comboDC;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTCuenta;
    private javax.swing.JTextArea jTFormula;
    private javax.swing.JLabel labelId;
    private javax.swing.JLabel labelPar;
    private javax.swing.JList listaOperadores;
    private javax.swing.JList listaPar;
    // End of variables declaration//GEN-END:variables
}
