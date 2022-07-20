/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

import contabilidad.AsientoDialog;
import contabilidad.PrincipalFrame;
import entidades.CentroCosto;
import entidades.Cuenta;
import entidades.Plantilla;
import entidades.PlantillaDet;
import general.BeanBase;
import general.Conector;
import general.HibernateUtil;
import general.ListaDetalle;
import general.StringMD;
import general.UsuarioAdmin;
import impresion.ImpresionController;
import impresion.MayorDetalle;
import java.awt.BorderLayout;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
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
public class MayorCuentaCC extends javax.swing.JFrame {
    public boolean mod;
    public boolean wb_nuevo;
    public PlantillaDet cuentaSel=new PlantillaDet();

    public PlantillaDet getCuentaSel() {
        return cuentaSel;
    }

    public void setCuentaSel(PlantillaDet cuentaSel) {
        this.cuentaSel = cuentaSel;
    }

    /**
     * Creates new form CuentaDialog
     */
    public MayorCuentaCC() {
        initComponents();
        //Botón Default
        getRootPane().setDefaultButton(buAceptar);
        //Listener para cerrar la ventana con la tecla Escape
        addEscapeListener(this);
        
        comboCuenta.removeAllItems();
        comboCC.removeAllItems();
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
                    Logger.getLogger(MayorCuentaCC.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
    };
    jTCuenta.getDocument().addDocumentListener(documentListener);
    jTCuenta.requestFocus();

    }
    
    //Listener para cerrar la ventana con la tecla Escape
    public static void addEscapeListener(final JFrame dialog) {
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
    
    //Busca los movimientos de la cuenta seleccionada
    private ArrayList buscaListaMovimientos(Cuenta cuenta,CentroCosto centroCosto,Date fec_ini,Date fec_fin) {
        
    try {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q=session.createQuery("Select a.id,a.fecMov, a.idUsuario, a.descripcion,b.dc,b.valor "
                + "from Asiento a,AsientoDet b "
                + "where a.ejercicio = :ejercicioSel "
                + "and a.fecMov >= :fec_ini and a.fecMov <= :fec_fin "
                + "and b.asiento=a "
                + "and b.cuenta= :cuentaSel "
                + "and b.centroCosto= :centroCostoSel "
                + "order by a.fecMov");
        q.setParameter("ejercicioSel",PrincipalFrame.ejercicioSel);
        q.setParameter("cuentaSel",cuenta);
        q.setParameter("centroCostoSel",centroCosto);
        q.setParameter("fec_ini",fec_ini);
        q.setParameter("fec_fin",fec_fin);
        //List resultList = q.list();

        BeanBase bean= new BeanBase();
        UsuarioAdmin usuario;
        ArrayList listaMovimientos= new ArrayList();
        MayorDetalle item;
        
        List<Object[]> resultList=(List<Object[]>) q.list();
        for (Object[] datos : resultList) {
            item= new MayorDetalle();
            item.setAsiento((Integer) datos[0]);
            item.setFecha((Date) datos[1] );
            int li_usuario=(int) datos[2];
            usuario=bean.obtenerUsuario(li_usuario);
            item.setUsuario(usuario.getNombreCompletoUsuario());
            item.setDescripcion((String) datos[3]);
            String ls=String.valueOf((char) datos[4]);
            item.setDc(String.valueOf((char) datos[4]));            
            item.setValor((BigDecimal) datos[5]);
            listaMovimientos.add(item);
        }
        
        session.getTransaction().commit();
        return listaMovimientos;
        
    } catch (HibernateException he) {
        he.printStackTrace();
        return null;
    }
    
    }
    
    //Obtiene el saldo anterior de la cuenta
    public double obtenerSaldoAnterior(Cuenta cuenta,Date fecha){
        CallableStatement s=null;
        ResultSet r=null;
        int li_id;
        double ld_saldo = 0;
        
        //Conectamos a la base
        Conector conector = new Conector();  
        Connection conexion = conector.connect("contabilidad");
        
        try {

               s=conexion.prepareCall("{call sp_get_saldo_anterior ( ? , ? , ? )}",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
               s.setInt(1,PrincipalFrame.ejercicioSel.getId());
               s.setInt(2,cuenta.getId());
               java.sql.Date lda_fecha;
               lda_fecha=new java.sql.Date(fecha.getTime());
               s.setDate(3,lda_fecha);
               
               r=s.executeQuery();
               if (r.next())
                   ld_saldo=r.getDouble("saldo");
               return ld_saldo;
               
        } catch (SQLException e){
            e.getMessage();
            return 0;            
        } finally {
             try{
                if (conexion != null) conexion.close();   
                if (r != null) r.close();   
             }catch(Exception e)  {
                e.getMessage();
             }
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
        jTCuenta = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        buAceptar = new javax.swing.JButton();
        buSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jDateDesde = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jDateHasta = new com.toedter.calendar.JDateChooser();
        comboCC = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(" Mayor por Cuenta por Centro de Costo");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

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

        jTCuenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTCuentaActionPerformed(evt);
            }
        });

        buAceptar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        buAceptar.setText("Buscar");
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
                .addContainerGap())
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

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Desde:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Hasta:");

        comboCC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        comboCC.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboCC.setAutoscrolls(true);
        comboCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCCActionPerformed(evt);
            }
        });
        comboCC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboCCKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Centro de costo:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(labelId)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(0, 92, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jDateDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTCuenta)
                                .addComponent(comboCuenta, 0, 365, Short.MAX_VALUE)
                                .addComponent(comboCC, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboCC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jDateHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(26, 26, 26)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buAceptarActionPerformed
        // TODO add your handling code here:
        
        ListaDetalle t= (ListaDetalle) comboCuenta.getSelectedItem();   
        if (t==null){
            JOptionPane.showMessageDialog(null,"Debe seleccionar una cuenta","Error",JOptionPane.WARNING_MESSAGE);
            return;
        }
        Cuenta cuenta= (Cuenta) buscaCuenta(t.getCodigo());
        
        ListaDetalle u= (ListaDetalle) comboCC.getSelectedItem();   
        if (u==null){
            JOptionPane.showMessageDialog(null,"Debe seleccionar un centro de costo","Error",JOptionPane.WARNING_MESSAGE);
            comboCC.requestFocus();
            return;
        }
        CentroCosto centroCosto= (CentroCosto) buscaCC(u.getCodigo());
        
        java.util.Date lda_fec_desde=jDateDesde.getDate();
        if (lda_fec_desde==null){
            JOptionPane.showMessageDialog(null,"Debe seleccionar una fecha de inicio","Error",JOptionPane.WARNING_MESSAGE);
            return;
        }
        java.util.Date lda_fec_hasta=jDateHasta.getDate();
        if (lda_fec_hasta==null){
            JOptionPane.showMessageDialog(null,"Debe seleccionar una fecha de fin","Error",JOptionPane.WARNING_MESSAGE);
            return;
        }
    
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
        fecha_hasta.set(Calendar.HOUR_OF_DAY,0);
        fecha_hasta.set(Calendar.MINUTE, 0);
        fecha_hasta.set(Calendar.SECOND, 0);
        fecha_hasta.set(Calendar.MILLISECOND, 0);
        java.util.Date lda_fecha_hasta = new java.sql.Date(fecha_hasta.getTimeInMillis());
        
        if (fecha_desde.getTime().compareTo(fecha_hasta.getTime()) > 0){
            JOptionPane.showMessageDialog(null, "Fecha de inicio no puede ser mayor que la fecha de fin","",JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Obtengo saldo anterior
        double ld_saldo_anterior=0;
        ld_saldo_anterior=obtenerSaldoAnterior(cuenta,lda_fecha_desde);
        
        //Intervalo de fechas
        TimeZone gmtZone = TimeZone.getTimeZone("America/Buenos_Aires");
        DateFormat destDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        destDateFormat.setTimeZone(gmtZone);
        String ls_fecha_desde=destDateFormat.format(lda_fecha_desde);
        String ls_fecha_hasta=destDateFormat.format(lda_fecha_hasta);
        String ls_intervalo="Desde " + ls_fecha_desde + " hasta " + ls_fecha_hasta;
        
        //Obtengo los movimientos
        ArrayList listaMovimientos =buscaListaMovimientos(cuenta,centroCosto,lda_fecha_desde, lda_fecha_hasta);

        //Genero reporte
        try {
            // TODO add your handling code here:
            ImpresionController imp= new ImpresionController();
            imp.generaReporteMayorCuentaCC(cuenta,centroCosto,listaMovimientos,ld_saldo_anterior,ls_intervalo);
        } catch (Exception ex) {
            Logger.getLogger(AsientoDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        PrincipalFrame.existeVentanaMayorCC=false;
    }//GEN-LAST:event_formWindowClosing

    private void comboCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCCActionPerformed

    private void comboCCKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboCCKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCCKeyPressed

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
            java.util.logging.Logger.getLogger(MayorCuentaCC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MayorCuentaCC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MayorCuentaCC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MayorCuentaCC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MayorCuentaCC().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buAceptar;
    private javax.swing.JButton buSalir;
    private javax.swing.JComboBox comboCC;
    private javax.swing.JComboBox comboCuenta;
    private com.toedter.calendar.JDateChooser jDateDesde;
    private com.toedter.calendar.JDateChooser jDateHasta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField jTCuenta;
    private javax.swing.JLabel labelId;
    // End of variables declaration//GEN-END:variables
}
