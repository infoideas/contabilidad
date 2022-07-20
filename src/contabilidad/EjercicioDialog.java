/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contabilidad;

import entidades.Ejercicio;
import entidades.Empresa;
import entidades.Periodo;
import general.BeanBase;
import general.Conector;
import general.HibernateUtil;
import general.ListaDetalle;
import java.awt.BorderLayout;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Propietario
 */
public class EjercicioDialog extends javax.swing.JDialog {
    private boolean mod;
    private boolean wb_nuevo;
    private Empresa empresaSel;
    private Ejercicio registroSel=new Ejercicio();
    private Ejercicio ultimoEjercicio=null;
        
    public Ejercicio getRegistroSel() {
        return registroSel;
    }

    public void setRegistroSel(Ejercicio registroSel) {
        this.registroSel = registroSel;
    }

    public Empresa getEmpresaSel() {
        return empresaSel;
    }

    public void setEmpresaSel(Empresa empresaSel) {
        this.empresaSel = empresaSel;
    }

    /**
     * Creates new form CuentaDialog
     */
    public EjercicioDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        //Botón Default
        getRootPane().setDefaultButton(buAceptar);
        //Listener para cerrar la ventana con la tecla Escape
        addEscapeListener(this);
        
        comboMesIni.removeAllItems();
        comboMesFin.removeAllItems();
        ListaDetalle e;
        e= new ListaDetalle();
        e.setCodigo(1);
        e.setNombre("Ene");
        comboMesIni.addItem(e);
        comboMesFin.addItem(e);
        
        e= new ListaDetalle();
        e.setCodigo(2);
        e.setNombre("Feb");
        comboMesIni.addItem(e);
        comboMesFin.addItem(e);

        e= new ListaDetalle();
        e.setCodigo(3);
        e.setNombre("Mar");
        comboMesIni.addItem(e);
        comboMesFin.addItem(e);

        e= new ListaDetalle();
        e.setCodigo(4);
        e.setNombre("Abr");
        comboMesIni.addItem(e);
        comboMesFin.addItem(e);
        
        e= new ListaDetalle();
        e.setCodigo(5);
        e.setNombre("May");
        comboMesIni.addItem(e);
        comboMesFin.addItem(e);
        
        e= new ListaDetalle();
        e.setCodigo(6);
        e.setNombre("Jun");
        comboMesIni.addItem(e);
        comboMesFin.addItem(e);
        
        e= new ListaDetalle();
        e.setCodigo(7);
        e.setNombre("Jul");
        comboMesIni.addItem(e);
        comboMesFin.addItem(e);
        
        e= new ListaDetalle();
        e.setCodigo(8);
        e.setNombre("Ago");
        comboMesIni.addItem(e);
        comboMesFin.addItem(e);

        e= new ListaDetalle();
        e.setCodigo(9);
        e.setNombre("Sep");
        comboMesIni.addItem(e);
        comboMesFin.addItem(e);
        
        e= new ListaDetalle();
        e.setCodigo(10);
        e.setNombre("Oct");
        comboMesIni.addItem(e);
        comboMesFin.addItem(e);
        
        e= new ListaDetalle();
        e.setCodigo(11);
        e.setNombre("Nov");
        comboMesIni.addItem(e);
        comboMesFin.addItem(e);
        
        e= new ListaDetalle();
        e.setCodigo(12);
        e.setNombre("Dic");
        comboMesIni.addItem(e);
        comboMesFin.addItem(e);
        
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
        setTitle("Nuevo ejercicio");
        wb_nuevo=true;
        comboMesIni.requestFocus();
        
        //Obtengo el último ejercicio para poner el que sigue
        ultimoEjercicio=obtenerUltimoEjericicio(empresaSel);
        if (ultimoEjercicio != null){
            //Obtengo el período siguiente al último
            int li_mes,li_anio_ini = 0,li_mes_ini,li_mes_fin,li_anio_fin;
            li_mes=ultimoEjercicio.getMesFin();
            if (li_mes <= 11){
               li_mes_ini=li_mes +1;
               li_anio_ini=ultimoEjercicio.getAnioFin();
               //Período de fin
               li_mes_fin=li_mes;               
               li_anio_fin=li_anio_ini + 1;
            }
            else{
                li_mes_ini=1;
                li_anio_ini=ultimoEjercicio.getAnioFin() + 1;
                li_mes_fin=12;
                li_anio_fin=li_anio_ini;
            }
            
            ListaDetalle e;
            e= new ListaDetalle();
            e.setCodigo(li_mes_ini);
            e.setNombre(new BeanBase().mesLetras(li_mes_ini,"A"));
            comboMesIni.setSelectedItem(e);
            jSpinnerAnioIni.setValue(li_anio_ini);
            comboMesIni.setEnabled(false);
            jSpinnerAnioIni.setEnabled(false);
            
            //Sumo 12 meses para el período final
            e= new ListaDetalle();
            e.setCodigo(li_mes_fin);
            e.setNombre(new BeanBase().mesLetras(li_mes_fin,"A"));
            comboMesFin.setSelectedItem(e);
            jSpinnerAnioFin.setValue(li_anio_fin);
           
            
        }
        
        
    }
    
    private Ejercicio obtenerUltimoEjericicio(Empresa empresa) {
        Session session = HibernateUtil.getSessionFactory().openSession();        
        try {
            //Obtengo el id del último ejercicio de la empresa

            session.beginTransaction();
            Query q=session.createQuery("select max(a.id) from Ejercicio a where a.empresa = :empresaSel");
            q.setParameter("empresaSel",empresa);
            List resultList = q.list();
            
            Ejercicio ultimoEjercicio = null;
            int idUltimoEjer=0;
            if (resultList.get(0) != null){
                //Hay un ejercicio anterior
                idUltimoEjer=((Integer) resultList.get(0)).intValue();
                q=session.createQuery("from Ejercicio a where a.id= :idEjercicio");
                q.setParameter("idEjercicio",idUltimoEjer);
                resultList = q.list();
                if (resultList.size() == 1){
                    ultimoEjercicio=(Ejercicio) resultList.get(0);
                }
                session.getTransaction().commit();
            }
        
            return ultimoEjercicio;
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        finally {
            session.close();
        }
        return null;
        
    }
    
    //Graba ejercicicio con los períodos
    private boolean actualizaBaseDatos(Object o,List<Periodo> listaPeriodos) throws SQLException{
        Session session = HibernateUtil.getSessionFactory().openSession();
        Ejercicio u= (Ejercicio) o;
        try{
            session.beginTransaction();
            session.saveOrUpdate(u);
            
            for(Periodo p : listaPeriodos) {
                try{
                    session.saveOrUpdate(p);
                }
                catch (HibernateException e){
                    session.getTransaction().rollback();
                    JOptionPane.showMessageDialog(null,e.getLocalizedMessage(),"Error",JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
            
            session.getTransaction().commit();            
            
            //Copio plan de cuentas
            if(ultimoEjercicio != null){
                if (copiaPlanCuentas(ultimoEjercicio.getId(),u.getId())){
                    return true;
                }
                else{
                    return false;
                }    
            }
            else{
                return true;
            }
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
    
    //Copia plan de cuentas y plantillas
    public boolean copiaPlanCuentas(int idEjercicioAnt,int idEjercicioActual) throws SQLException{
        
        CallableStatement s=null;
        ResultSet r=null;
        int li_resultado=0;
        
        //Conectamos a la base
        Conector conector = new Conector();  
        Connection conexion = conector.connect("contabilidad");
        
        try {      
            
             //Los parámetros que tienen blanco o nulo son porque no los actualiza
             s=conexion.prepareCall("{call sp_copiar_ejercicio ( ? , ? , ? )}"); 
             s.setInt(1,idEjercicioAnt); 
             s.setInt(2,idEjercicioActual);
             s.registerOutParameter(3,java.sql.Types.INTEGER);
             s.executeUpdate();
             
             //Obtengo el id del registro insertado
             li_resultado=s.getInt(3);

        }catch (SQLException e){
            System.out.println("Error al copiar plan de cuentas SQL: " + e.getMessage() );
        }catch (Exception e){
            System.out.println("Error al copiar plan de cuentas: " + e.getMessage() );
        }finally {
            s.close();
            conexion.close();
        }
        return li_resultado == 1;
        
    }
    
    
    
    public boolean grabaRegistro() throws SQLException
    {
        short li_anio_ini,li_mes_ini,li_anio_fin,li_mes_fin;
        
        li_anio_ini= ((Integer) jSpinnerAnioIni.getValue()).shortValue();
        if (li_anio_ini <= 0){
          JOptionPane.showMessageDialog(null, "Debe ingresar el año de inicio","Datos incompletos",JOptionPane.WARNING_MESSAGE);
          jSpinnerAnioIni.requestFocus();
          return false;
        }
        
        ListaDetalle u= (ListaDetalle) comboMesIni.getSelectedItem();   
        if (u==null){
            JOptionPane.showMessageDialog(null,"Debe seleccionar el mes de inicio","Error",JOptionPane.WARNING_MESSAGE);
                comboMesIni.requestFocus();
                return false;
        }
        li_mes_ini=(short) u.getCodigo();
        
        
        li_anio_fin= ((Integer) jSpinnerAnioFin.getValue()).shortValue();
        if (li_anio_fin <= 0){
          JOptionPane.showMessageDialog(null, "Debe ingresar el año de fin","Datos incompletos",JOptionPane.WARNING_MESSAGE);
          jSpinnerAnioFin.requestFocus();
          return false;
        }

        u= (ListaDetalle) comboMesFin.getSelectedItem();   
        if (u==null){
            JOptionPane.showMessageDialog(null,"Debe seleccionar el mes de fin","Error",JOptionPane.WARNING_MESSAGE);
                comboMesFin.requestFocus();
                return false;
        }
        li_mes_fin=(short) u.getCodigo();
        
        if (li_anio_fin < li_anio_ini){
            JOptionPane.showMessageDialog(null,"Período de fin debe ser mayor al período de inicio","Error",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (li_anio_fin == li_anio_ini){
            if (li_mes_fin <= li_mes_ini){
                JOptionPane.showMessageDialog(null,"Período de fin debe ser mayor al período de inicio","Error",JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
       
        Ejercicio p= this.getRegistroSel();
        p.setEmpresa(empresaSel);
        p.setAnioInicio(li_anio_ini);
        p.setMesInicio(li_mes_ini);
        p.setAnioFin(li_anio_fin);
        p.setMesFin(li_mes_fin);
        
        //Genero lista de períodos
        List<Periodo> listaPeriodos= new ArrayList<Periodo>();
        boolean lb_fin=false;
        short li_anio,li_mes;
        char ls_inicial='1';
        li_anio=li_anio_ini;
        li_mes=li_mes_ini;
        while (lb_fin==false){
            if(li_mes==li_mes_fin && li_anio==li_anio_fin)
                lb_fin=true;

            Periodo per= new Periodo();
            per.setAnio(li_anio);
            per.setMes(li_mes);
            per.setEjercicio(p);
            per.setEstado('A');
            per.setInicial(ls_inicial);
            listaPeriodos.add(per);            
            
            li_mes=(short) (li_mes + 1) ;
            if(li_mes > 12){
                li_mes=1;
                li_anio=(short) (li_anio + 1);
            }
            ls_inicial='0';
            
        }
        
        //Grabo en la base
        if (actualizaBaseDatos(p,listaPeriodos)){
            JOptionPane.showMessageDialog(null, "Actualización exitosa","",JOptionPane.INFORMATION_MESSAGE);
            if (wb_nuevo)
                jTId.setText(Integer.toString(p.getId()));
            return true;    
        }
        else
            return false;
        
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
        jTId = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        comboMesIni = new javax.swing.JComboBox();
        jSpinnerAnioIni = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        comboMesFin = new javax.swing.JComboBox();
        jSpinnerAnioFin = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        buAceptar = new javax.swing.JButton();
        buSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);

        jTId.setEditable(false);
        jTId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTIdActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Id.:");

        comboMesIni.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        comboMesIni.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jSpinnerAnioIni.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jSpinnerAnioIni.setModel(new javax.swing.SpinnerNumberModel(2020, 2000, 3000, 1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Período de inicio:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Período de fin:");

        comboMesFin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        comboMesFin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jSpinnerAnioFin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jSpinnerAnioFin.setModel(new javax.swing.SpinnerNumberModel(2020, 2020, 3000, 1));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelId)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jTId, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(296, 296, 296))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(comboMesFin, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSpinnerAnioFin, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(comboMesIni, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSpinnerAnioIni, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                            .addComponent(jTId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboMesIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jSpinnerAnioIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboMesFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jSpinnerAnioFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        try {
            // TODO add your handling code here:
            if (grabaRegistro())
                dispose();
        } catch (SQLException ex) {
            Logger.getLogger(EjercicioDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buAceptarActionPerformed

    private void buSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buSalirActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_buSalirActionPerformed

    private void jTIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTIdActionPerformed

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
            java.util.logging.Logger.getLogger(EjercicioDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EjercicioDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EjercicioDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EjercicioDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                    EjercicioDialog dialog = new EjercicioDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox comboMesFin;
    private javax.swing.JComboBox comboMesIni;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSpinner jSpinnerAnioFin;
    private javax.swing.JSpinner jSpinnerAnioIni;
    private javax.swing.JTextField jTId;
    private javax.swing.JLabel labelId;
    // End of variables declaration//GEN-END:variables
}
