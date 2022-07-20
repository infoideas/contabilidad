/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contabilidad;

import static contabilidad.CuentaDialog.addEscapeListener;
import entidades.Asiento;
import entidades.AsientoDet;
import entidades.Cuenta;
import entidades.Periodo;
import entidades.Plantilla;
import entidades.PlantillaDet;
import general.BeanBase;
import general.HibernateUtil;
import general.ListaDetalle;
import general.UsuarioAdmin;
import impresion.ImpresionController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
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
import javax.swing.text.NumberFormatter;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Propietario
 */
public class AsientoDialog extends javax.swing.JFrame {
    public boolean mod;
    public boolean wb_nuevo;
    public Asiento registroSel=new Asiento();
    Vector tableData = new Vector();
    Vector tableDataFooter = new Vector();
    Vector<String> tableHeaders = new Vector<>();
    Vector<String> tableHeadersFooter = new Vector<>();
    double totalDebe,totalHaber;
    private boolean asientoEditable=true;
    

    public Asiento getRegistroSel() {
        return registroSel;
    }

    
    /**
     * Creates new form PlantillaDialog
     */
    public void setRegistroSel(Asiento registroSel) {
        this.registroSel = registroSel;
    }

    public AsientoDialog() {
        initComponents();
        
        //Aceptar botón Default 
        getRootPane().setDefaultButton(buAceptar);
        
        //Listener para cerrar la ventana con la tecla Escape
        addEscapeListener(this);
        
        creaModelo();
        
        
        cargaListaPlantillas();     
        
        buEditar.setVisible(false);
        //Agrego listener para doble click en lista de cuentas
        listaCuentas.addMouseListener
        (
    	new MouseAdapter()
    	{
    		public void mouseClicked(MouseEvent evnt)
    		{
                  if (evnt.getClickCount() == 2 && asientoEditable)
                    {
                  	System.out.println("Doble Clic");
                        JTable target = (JTable)evnt.getSource();
                        int row = target.getSelectedRow(); // select a row
                        int column = target.getSelectedColumn(); // select a column
                        consultaRegistro();                        
                    }
    		}
    	}
        );
        
        
        
    }
    
    public void creaModelo(){
        tableData = new Vector();
        tableDataFooter = new Vector();
        tableHeaders = new Vector<>();
        tableHeadersFooter = new Vector<>();
        
         //Cargo header
        tableHeaders.add("Objeto");
        tableHeaders.add("Número");
        tableHeaders.add("Cuenta"); 
        tableHeaders.add("D/C"); 
        tableHeaders.add("CC");         
        tableHeaders.add("Débito");
        tableHeaders.add("Crédito");
        
        //Cargo header footer
        tableHeadersFooter.add("Objeto");        
        tableHeadersFooter.add("Número");
        tableHeadersFooter.add("Cuenta"); 
        tableHeadersFooter.add("D/C");         
        tableHeadersFooter.add("CC");         
        tableHeadersFooter.add("Débito");
        tableHeadersFooter.add("Crédito");
        
        //Modelo de la tabla de cuentas
        DefaultTableCellRenderer AlinearDerecha = new DefaultTableCellRenderer();
        AlinearDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        DefaultTableCellRenderer AlinearCentro = new DefaultTableCellRenderer();
        AlinearCentro.setHorizontalAlignment(SwingConstants.CENTER);
        
        DefaultTableModel modelo = new DefaultTableModel(tableData,tableHeaders)
        {@Override
                public boolean isCellEditable (int fila, int columna) {
                if (columna >= 0)
                    return false;
                
                    return true;
                }
        };     
        
        //Especifico ancho de columnas de JTable de cuentas
        //Los campor de Id lo pongo para que no se vean
        listaCuentas.setModel(modelo);
        listaCuentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaCuentas.getColumnModel().getColumn(0).setPreferredWidth(0);
        listaCuentas.getColumnModel().getColumn(0).setMinWidth(0);
        listaCuentas.getColumnModel().getColumn(0).setMaxWidth(0);
        listaCuentas.getColumnModel().getColumn(1).setPreferredWidth(100);
        listaCuentas.getColumnModel().getColumn(1).setMinWidth(10);
        listaCuentas.getColumnModel().getColumn(1).setMaxWidth(100);
        listaCuentas.getColumnModel().getColumn(2).setPreferredWidth(200);
        listaCuentas.getColumnModel().getColumn(3).setPreferredWidth(30);
        listaCuentas.getColumnModel().getColumn(4).setPreferredWidth(30);
        listaCuentas.getColumnModel().getColumn(5).setPreferredWidth(50);
        listaCuentas.getColumnModel().getColumn(6).setPreferredWidth(50);
        
        //Alineo las columnas necesarias
        listaCuentas.getColumnModel().getColumn(3).setCellRenderer(AlinearCentro);
        listaCuentas.getColumnModel().getColumn(4).setCellRenderer(AlinearCentro);        
        listaCuentas.getColumnModel().getColumn(5).setCellRenderer(AlinearDerecha);
        listaCuentas.getColumnModel().getColumn(6).setCellRenderer(AlinearDerecha);        
        
        //Modelo de la tabla de footer
        DefaultTableModel modeloFooter = new DefaultTableModel(tableDataFooter,tableHeadersFooter)
        {@Override
                public boolean isCellEditable (int fila, int columna) {
                if (columna >= 0)
                    return false;
                
                    return true;
                }
        };            
        footerAsiento.setModel(modeloFooter);
        footerAsiento.getColumnModel().getColumn(0).setPreferredWidth(0);
        footerAsiento.getColumnModel().getColumn(0).setMinWidth(0);
        footerAsiento.getColumnModel().getColumn(0).setMaxWidth(0);
        footerAsiento.getColumnModel().getColumn(1).setPreferredWidth(50);
        footerAsiento.getColumnModel().getColumn(1).setMinWidth(10);
        footerAsiento.getColumnModel().getColumn(1).setMaxWidth(50);
        footerAsiento.getColumnModel().getColumn(2).setPreferredWidth(200);
        footerAsiento.getColumnModel().getColumn(3).setPreferredWidth(30);
        footerAsiento.getColumnModel().getColumn(4).setPreferredWidth(30);
        footerAsiento.getColumnModel().getColumn(5).setPreferredWidth(50);
        footerAsiento.getColumnModel().getColumn(6).setPreferredWidth(50); 
        
        footerAsiento.getColumnModel().getColumn(3).setCellRenderer(AlinearCentro);
        footerAsiento.getColumnModel().getColumn(4).setCellRenderer(AlinearCentro);
        footerAsiento.getColumnModel().getColumn(5).setCellRenderer(AlinearDerecha);
        footerAsiento.getColumnModel().getColumn(6).setCellRenderer(AlinearDerecha);    
        footerAsiento.setTableHeader(null);
        
        //Agrego listener para el cambio de item seleccionado en Provincia
        comboPlantilla.removeAllItems(); 
        comboPlantilla.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                            if (comboPlantilla.getItemCount() > 0 && wb_nuevo) {
                                ListaDetalle o;
                                o = (ListaDetalle) comboPlantilla.getSelectedItem();
                                if (o.getCodigo() > 0){
                                    Plantilla p= (Plantilla) buscaPlantilla(o.getCodigo());
                                    //Carga lista de cuentas en asiento
                                    cargaListaCuentas(p);
                                }
                            }
			}
		});
        
         cargaListaPlantillas();     
    }

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
    
    private void muestraListaCuentas() {
        //DecimalFormat df = new DecimalFormat( "#,##0.##", new DecimalFormatSymbols(new Locale("es", "AR")));     
        DecimalFormat df = new DecimalFormat( "#,###.00");   
        String ls_valor_formateado;
        tableData.clear();
        DefaultTableModel modelo = (DefaultTableModel) listaCuentas.getModel();
        Iterator i= registroSel.getAsientoDets().iterator();
        while (i.hasNext()){
            AsientoDet p= (AsientoDet) i.next();
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(p);            
            oneRow.add(p.getCuenta().getCuentaNumero());
            oneRow.add(p.getCuenta().getCuentaNombre());
            oneRow.add(p.getDc());
            oneRow.add(p.getCentroCosto()!=null ?  p.getCentroCosto().getAbrev() : "");
            ls_valor_formateado=df.format(p.getValor());
            oneRow.add(p.getDc()== 'D' ? ls_valor_formateado : "0,00");
            oneRow.add(p.getDc()== 'C' ? ls_valor_formateado : "0,00");
            modelo.addRow(oneRow);
            totalDebe=totalDebe + (p.getDc()== 'D' ? p.getValor().doubleValue() : 0);
            totalHaber=totalHaber + (p.getDc()== 'C' ? p.getValor().doubleValue() : 0);
        }
        
        listaCuentas.setModel(modelo);
        
        //Pongo los totales en el footer
        DefaultTableModel modeloFooter = (DefaultTableModel) footerAsiento.getModel();
        tableDataFooter.clear();
        Vector<Object> rowfooter = new Vector<Object>();
        rowfooter.add(" ");
        rowfooter.add(" ");
        rowfooter.add(" ");
        rowfooter.add(" ");        
        rowfooter.add("Totales:");
        rowfooter.add(df.format(totalDebe));
        rowfooter.add(df.format(totalHaber));
        modeloFooter.addRow(rowfooter);
        footerAsiento.setModel(modeloFooter);
        
    }
    
    private void cargaListaCuentas(Plantilla plantilla) {
        //DecimalFormat df = new DecimalFormat( "#,##0.##", new DecimalFormatSymbols(new Locale("es", "AR")));     
        DecimalFormat df = new DecimalFormat( "#,###.00");   
        String ls_valor_formateado;
        
        registroSel.getAsientoDets().clear();
        tableData.clear();
        DefaultTableModel modelo = (DefaultTableModel) listaCuentas.getModel();
        Iterator i=  plantilla.getPlantillaDets().iterator();
        while (i.hasNext()){
            PlantillaDet p= (PlantillaDet) i.next();
            AsientoDet q= new AsientoDet();
            q.setCuenta(p.getCuenta());
            q.setDc(p.getDc());
            q.setValor(BigDecimal.ZERO);
            q.setAsiento(registroSel);
            registroSel.getAsientoDets().add(q);

            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(q);            
            oneRow.add(p.getCuenta().getCuentaNumero());
            oneRow.add(p.getCuenta().getCuentaNombre());
            oneRow.add(p.getDc());
            oneRow.add("");                        
            oneRow.add("0,00");
            oneRow.add("0,00");
            modelo.addRow(oneRow);
            totalDebe=0;
            totalHaber=0;
        }
        
        listaCuentas.setModel(modelo);
        
        //Pongo los totales en el footer
        DefaultTableModel modeloFooter = (DefaultTableModel) footerAsiento.getModel();
        tableDataFooter.clear();
        Vector<Object> rowfooter = new Vector<Object>();
        rowfooter.add(" ");
        rowfooter.add(" ");
        rowfooter.add(" ");
        rowfooter.add(" ");
        rowfooter.add("Totales:");
        rowfooter.add(df.format(totalDebe));
        rowfooter.add(df.format(totalHaber));
        modeloFooter.addRow(rowfooter);
        footerAsiento.setModel(modeloFooter);
        
    }
    
    //Actualiza totales de Debe y Haber
    private void actualizaTotales(){
        totalDebe=0.00;
        totalHaber=0.00;
        Iterator i= registroSel.getAsientoDets().iterator();
        while (i.hasNext()){
            AsientoDet p= (AsientoDet) i.next();
            totalDebe=totalDebe + (p.getDc()== 'D' ? p.getValor().doubleValue() : 0);
            totalHaber=totalHaber + (p.getDc()== 'C' ? p.getValor().doubleValue() : 0);
        }
        totalDebe=Math.round(totalDebe*100d)/100d;
        totalHaber=Math.round(totalHaber*100d)/100d;
        
    }
    
    
    private void cargaListaPlantillas() {
        Session session = HibernateUtil.getSessionFactory().openSession();        
        try {
            session.beginTransaction();
            Query q=session.createQuery("from Plantilla a where a.ejercicio = :ejercicioSel "
                    + "order by nombre");
            q.setParameter("ejercicioSel",PrincipalFrame.ejercicioSel);
            List resultList = q.list();
            session.getTransaction().commit();
        
            comboPlantilla.removeAllItems();        
            ListaDetalle e;
            e= new ListaDetalle();
            e.setCodigo(0);
            e.setNombre("(Ninguna)");
            comboPlantilla.addItem(e);
        
            for(Object o : resultList) {
                Plantilla p = (Plantilla)o;
                e= new ListaDetalle();
                e.setCodigo(p.getId());
                e.setNombre(p.getNombre());
                comboPlantilla.addItem(e);
            }   
        } catch (HibernateException he) {
            he.printStackTrace();
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

        jPanel3 = new javax.swing.JPanel();
        jPanelCab = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTDescripcion = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTId = new javax.swing.JTextField();
        jDateFecha = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTFechaCarga = new javax.swing.JTextField();
        comboPlantilla = new javax.swing.JComboBox();
        jtUsuario = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        buAceptar = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        buAgregar = new javax.swing.JButton();
        buEliminar = new javax.swing.JButton();
        buEditar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaCuentas = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        footerAsiento = new javax.swing.JTable();

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Asiento contable");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Descripción:");

        jTDescripcion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setLabelFor(comboPlantilla);
        jLabel2.setText("Plantilla:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Id:");

        jTId.setEditable(false);
        jTId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Fecha:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Usuario:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Fecha de carga:");

        jTFechaCarga.setEditable(false);

        comboPlantilla.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        comboPlantilla.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboPlantilla.setName(""); // NOI18N
        comboPlantilla.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboPlantillaItemStateChanged(evt);
            }
        });
        comboPlantilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboPlantillaActionPerformed(evt);
            }
        });

        jtUsuario.setEditable(false);
        jtUsuario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanelCabLayout = new javax.swing.GroupLayout(jPanelCab);
        jPanelCab.setLayout(jPanelCabLayout);
        jPanelCabLayout.setHorizontalGroup(
            jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTDescripcion)
                    .addGroup(jPanelCabLayout.createSequentialGroup()
                        .addGroup(jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(comboPlantilla, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelCabLayout.createSequentialGroup()
                                .addComponent(jTId, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtUsuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFechaCarga, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelCabLayout.setVerticalGroup(
            jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCabLayout.createSequentialGroup()
                .addGroup(jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCabLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jLabel6)
                                .addComponent(jTFechaCarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCabLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jDateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)))
                .addGroup(jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboPlantilla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanelCabLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(66, 66, 66))
        );

        jPanelCabLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel3, jLabel4, jLabel5});

        buAceptar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        buAceptar.setText("Aceptar");
        buAceptar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buAceptarActionPerformed(evt);
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

        buAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        buAgregar.setText("Agregar cuenta");
        buAgregar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buAgregarActionPerformed(evt);
            }
        });

        buEliminar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        buEliminar.setText("Eliminar cuenta");
        buEliminar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buEliminarActionPerformed(evt);
            }
        });

        buEditar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        buEditar.setText("Editar cuenta");
        buEditar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buEditarActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setText("Imprimir");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buEditar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 246, Short.MAX_VALUE)
                .addComponent(buAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buAgregar, buEditar, buEliminar, jButton1});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(buAgregar)
                        .addComponent(buEliminar)
                        .addComponent(buEditar)
                        .addComponent(jButton1))
                    .addComponent(buAceptar, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {buAceptar, buAgregar, buEditar, buEliminar, jButton1, jButton5});

        listaCuentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Número", "Cuenta", "D/C", "Valor Debe", "Valor Haber"
            }
        ));
        jScrollPane2.setViewportView(listaCuentas);

        footerAsiento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "", "", "", "", ""
            }
        ));
        footerAsiento.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(footerAsiento);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelCab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanelCab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void buAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buAgregarActionPerformed
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat( "#,###.00");   
        BuscaCuentaAsientoDialog dialog = new BuscaCuentaAsientoDialog(this, true);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        
        DefaultTableCellRenderer AlinearDerecha = new DefaultTableCellRenderer();
        AlinearDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        
        if (dialog.mod){
            
        AsientoDet p= dialog.getCuentaSel();
        if (p.getCuenta() != null){

            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(p);        
            oneRow.add(p.getCuenta().getCuentaNumero());
            oneRow.add(p.getCuenta().getCuentaNombre());
            oneRow.add(p.getDc());
            oneRow.add(p.getCentroCosto()!=null ?  p.getCentroCosto().getAbrev() : "");
            oneRow.add(p.getDc()== 'D' ? df.format(p.getValor()) : "0,00");
            oneRow.add(p.getDc()== 'C' ? df.format(p.getValor()) : "0,00");
            
            DefaultTableModel modelo = (DefaultTableModel) listaCuentas.getModel();
            modelo.addRow(oneRow);
            listaCuentas.setModel(modelo);
        
            p.setAsiento(registroSel);
            registroSel.getAsientoDets().add(p);
            
            //Pongo los totales en el footer
            DefaultTableModel modeloFooter = (DefaultTableModel) footerAsiento.getModel();
            tableDataFooter.clear();
            Vector<Object> rowfooter = new Vector<Object>();
            rowfooter.add(" ");
            rowfooter.add(" ");
            rowfooter.add(" ");
            rowfooter.add(" ");
            rowfooter.add(" ");
            totalDebe=totalDebe + (p.getDc()== 'D' ? p.getValor().doubleValue() : 0);
            totalHaber=totalHaber + (p.getDc()== 'C' ? p.getValor().doubleValue() : 0);
            rowfooter.add(df.format(totalDebe));
            rowfooter.add(df.format(totalHaber));
            modeloFooter.addRow(rowfooter);
            footerAsiento.setModel(modeloFooter);
        }
        }
            
    }//GEN-LAST:event_buAgregarActionPerformed

    private void buAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buAceptarActionPerformed
        // TODO add your handling code here:
         grabaRegistro();
    }//GEN-LAST:event_buAceptarActionPerformed

    private void buEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buEliminarActionPerformed
        // TODO add your handling code here:
        Object[] opciones = {"Sí","No"};  //Opciones de la ventana de confirmación
        int li_fila_sel=listaCuentas.getSelectedRow();  //Fila seleccionada
        if (li_fila_sel == -1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro","",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int li_respuesta=JOptionPane.showOptionDialog(new JFrame(),"Está seguro de eliminar?","Eliminar rubro",JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE , null,opciones,opciones[0]);
            if (li_respuesta == JOptionPane.YES_OPTION)
            {
                
            AsientoDet p= (AsientoDet) listaCuentas.getValueAt(li_fila_sel,0);
            
            if (registroSel.getAsientoDets().remove(p)){
                DefaultTableModel modelo = (DefaultTableModel) listaCuentas.getModel();
                modelo.removeRow(li_fila_sel);
                listaCuentas.setModel(modelo);
                
                //Pongo los totales en el footer
                DecimalFormat df = new DecimalFormat( "#,###.00");               
                DefaultTableModel modeloFooter = (DefaultTableModel) footerAsiento.getModel();
                tableDataFooter.clear();
                Vector<Object> rowfooter = new Vector<Object>();
                rowfooter.add(" ");
                rowfooter.add(" ");
                rowfooter.add(" ");
                rowfooter.add(" ");
                rowfooter.add(" ");
            
                totalDebe=totalDebe - (p.getDc()== 'D' ? p.getValor().doubleValue() : 0);
                totalHaber=totalHaber - (p.getDc()== 'C' ? p.getValor().doubleValue() : 0);
                rowfooter.add(df.format(totalDebe));
                rowfooter.add(df.format(totalHaber));
                modeloFooter.addRow(rowfooter);
                footerAsiento.setModel(modeloFooter);
                
            }
                
            }
        
        
    }//GEN-LAST:event_buEliminarActionPerformed

    private void buEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buEditarActionPerformed
        // TODO add your handling code here:
        Object[] opciones = {"Sí","No"};  //Opciones de la ventana de confirmación
        int li_id=0,li_id_cuenta=0;
        int li_fila_sel=listaCuentas.getSelectedRow();  //Fila seleccionada
        if (li_fila_sel == -1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro","",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        AsientoDet p= (AsientoDet) listaCuentas.getValueAt(li_fila_sel,0);
        
        BuscaCuentaAsientoDialog dialog = new BuscaCuentaAsientoDialog(this, true);
        dialog.setLocationRelativeTo(null);
        dialog.setCuentaSel(p);
        dialog.editaRegistro();
        dialog.setVisible(true);

        p=dialog.getCuentaSel();
       
        if (registroSel.getAsientoDets().add(p));
        {
                
                //Modifico registro en datatable
                DefaultTableModel modelo = (DefaultTableModel) listaCuentas.getModel();
                //Modifico Jtable
                DecimalFormat df = new DecimalFormat( "#,###.00");   
                modelo.setValueAt(p,li_fila_sel,0);
                modelo.setValueAt(p.getCuenta().getCuentaNumero(),li_fila_sel,1);
                modelo.setValueAt(p.getCuenta().getCuentaNombre(),li_fila_sel,2);
                modelo.setValueAt(p.getDc(),li_fila_sel,3);
                modelo.setValueAt(p.getCentroCosto()!=null ?  p.getCentroCosto().getAbrev() : "",li_fila_sel,4);
                modelo.setValueAt(p.getDc()== 'D' ? df.format(p.getValor()) : "0,00",li_fila_sel,5);
                modelo.setValueAt(p.getDc()== 'C' ? df.format(p.getValor()) : "0,00",li_fila_sel,6);      
                
                //Si se modifica la cuenta de la posición 1 y solo hay 2 cuentas
                //se actualiza automáticamente el valor de la cuenta 2 si no lleva centro de costo
                if (li_fila_sel==0 && listaCuentas.getRowCount()==2){
                    li_fila_sel=1;
                    AsientoDet q= (AsientoDet) listaCuentas.getValueAt(li_fila_sel,0);
                    //Si la segunda cuenta no lleva centro de costo pongo el valor automáticamente
                    if (q.getCuenta().getCc()=='0'){
                        q.setValor(p.getValor());      
                        modelo.setValueAt(q,li_fila_sel,0);
                        modelo.setValueAt(q.getCuenta().getCuentaNumero(),li_fila_sel,1);
                        modelo.setValueAt(q.getCuenta().getCuentaNombre(),li_fila_sel,2);
                        modelo.setValueAt(q.getDc(),li_fila_sel,3);
                        modelo.setValueAt(q.getCentroCosto()!=null ?  q.getCentroCosto().getAbrev() : "",li_fila_sel,4);
                        modelo.setValueAt(q.getDc()== 'D' ? df.format(p.getValor()) : "0,00",li_fila_sel,5);
                        modelo.setValueAt(q.getDc()== 'C' ? df.format(p.getValor()) : "0,00",li_fila_sel,6);            
                    }
                    
                }

                listaCuentas.setModel(modelo);
                listaCuentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
                //Actulizo totales de Debe y Haber
                actualizaTotales();
                DefaultTableModel modeloFooter = (DefaultTableModel) footerAsiento.getModel();
                tableDataFooter.clear();
                Vector<Object> rowfooter = new Vector<Object>();
                rowfooter.add(" ");
                rowfooter.add(" ");
                rowfooter.add(" ");
                rowfooter.add(" ");
                rowfooter.add("Totales:");
                rowfooter.add(df.format(totalDebe));
                rowfooter.add(df.format(totalHaber));
                modeloFooter.addRow(rowfooter);
                footerAsiento.setModel(modeloFooter);
            
            }
            
            

    }//GEN-LAST:event_buEditarActionPerformed

    
    
    
    private void comboPlantillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboPlantillaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboPlantillaActionPerformed

    private void comboPlantillaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboPlantillaItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_comboPlantillaItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (totalDebe != totalHaber){
              JOptionPane.showMessageDialog(null, "Asiento descuadrado","Datos incompletos",JOptionPane.WARNING_MESSAGE);
              return;
        }
        try {
            // TODO add your handling code here:
            ImpresionController imp= new ImpresionController();
            imp.generaFormularioAsiento(registroSel);
        } catch (Exception ex) {
            Logger.getLogger(AsientoDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    public void consultaRegistro(){
        if (!asientoEditable) return;  //Si no es modificable return
        
        Object[] opciones = {"Sí","No"};  //Opciones de la ventana de confirmación
        int li_id=0,li_id_cuenta=0;
        int li_fila_sel=listaCuentas.getSelectedRow();  //Fila seleccionada
        if (li_fila_sel == -1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro","",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        AsientoDet p= (AsientoDet) listaCuentas.getValueAt(li_fila_sel,0);
        totalDebe=totalDebe + (p.getDc()== 'D' ? p.getValor().doubleValue() : 0);
        totalHaber=totalHaber + (p.getDc()== 'C' ? p.getValor().doubleValue() : 0);
        
        BuscaCuentaAsientoDialog dialog = new BuscaCuentaAsientoDialog(this, true);
        dialog.setLocationRelativeTo(null);
        dialog.setCuentaSel(p);
        dialog.editaRegistro();
        dialog.setVisible(true);
        
        p=dialog.getCuentaSel();
 
        if (dialog.mod){
            
        if (registroSel.getAsientoDets().add(p));
        {
                //Modifico registro en datatable
                DefaultTableModel modelo = (DefaultTableModel) listaCuentas.getModel();
                //Modifico Jtable
                DecimalFormat df = new DecimalFormat( "#,###.00");   
                modelo.setValueAt(p,li_fila_sel,0);
                modelo.setValueAt(p.getCuenta().getCuentaNumero(),li_fila_sel,1);
                modelo.setValueAt(p.getCuenta().getCuentaNombre(),li_fila_sel,2);
                modelo.setValueAt(p.getDc(),li_fila_sel,3);
                modelo.setValueAt(p.getCentroCosto()!=null ?  p.getCentroCosto().getAbrev() : "",li_fila_sel,4);                
                modelo.setValueAt(p.getDc()== 'D' ? df.format(p.getValor()) : "0,00",li_fila_sel,5);
                modelo.setValueAt(p.getDc()== 'C' ? df.format(p.getValor()) : "0,00",li_fila_sel,6);            
                
                //Si se modifica la cuenta de la posición 1 y solo hay 2 cuentas
                //se actualiza automáticamente el valor de la cuenta 2 si no lleva centro de costo
                if (li_fila_sel==0 && listaCuentas.getRowCount()==2){
                    li_fila_sel=1;
                    AsientoDet q= (AsientoDet) listaCuentas.getValueAt(li_fila_sel,0);
                    
                    if (q.getCuenta().getCc()=='0'){
                        q.setValor(p.getValor());                        
                        modelo.setValueAt(q,li_fila_sel,0);
                        modelo.setValueAt(q.getCuenta().getCuentaNumero(),li_fila_sel,1);
                        modelo.setValueAt(q.getCuenta().getCuentaNombre(),li_fila_sel,2);
                        modelo.setValueAt(q.getDc(),li_fila_sel,3);
                        modelo.setValueAt(p.getCentroCosto()!=null ?  p.getCentroCosto().getAbrev() : "",li_fila_sel,4);
                        modelo.setValueAt(q.getDc()== 'D' ? df.format(p.getValor()) : "0,00",li_fila_sel,5);
                        modelo.setValueAt(q.getDc()== 'C' ? df.format(p.getValor()) : "0,00",li_fila_sel,6);            
                    }
                }

                listaCuentas.setModel(modelo);
                listaCuentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
                //Actulizo totales de Debe y Haber
                actualizaTotales();
                DefaultTableModel modeloFooter = (DefaultTableModel) footerAsiento.getModel();
                tableDataFooter.clear();
                Vector<Object> rowfooter = new Vector<Object>();
                rowfooter.add(" ");
                rowfooter.add(" ");
                rowfooter.add(" ");
                rowfooter.add(" ");
                rowfooter.add("Totales:");
                rowfooter.add(df.format(totalDebe));
                rowfooter.add(df.format(totalHaber));
                modeloFooter.addRow(rowfooter);
                footerAsiento.setModel(modeloFooter);
            
            }
            
            
        
        }
        
        
    }
    
    
    public void nuevoRegistro(){
        setTitle("Nuevo asiento");
        wb_nuevo=true;
        tableData.clear();
        registroSel.getAsientoDets().clear();
        
        //Fecha y hora de carga del asiento
        TimeZone gmtZone = TimeZone.getTimeZone("America/Buenos_Aires");
        DateFormat destDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        destDateFormat.setTimeZone(gmtZone);
        Date lda_fec_carga = new Date();
        String fecha_carga=destDateFormat.format(lda_fec_carga);
        jTFechaCarga.setText(fecha_carga);
        
        //Fecha y hora del movimiento por defaul la actual
        java.util.Calendar fecha_mov = java.util.Calendar.getInstance();
        fecha_mov.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
        fecha_mov.setTime(new Date());
        fecha_mov.set(Calendar.HOUR_OF_DAY, 0);
        fecha_mov.set(Calendar.MINUTE, 0);
        fecha_mov.set(Calendar.SECOND, 0);
        fecha_mov.set(Calendar.MILLISECOND, 0);
        jDateFecha.setDate(fecha_mov.getTime());
        //Solo usuarios autorizados pueden cambiarla
        String ls_permiso;
        BeanBase bean= new BeanBase();
        ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("AsientaFechaAnterior");
        bean= new BeanBase();
        if (!bean.validarPermiso(PrincipalFrame.usuarioAdmin.getNombreUsuario(),ls_permiso,false))
            jDateFecha.setEnabled(false);
        
        totalDebe=0;
        totalHaber=0;        
        jtUsuario.setText(PrincipalFrame.usuarioAdmin.getNombreCompletoUsuario());  //Cambiar por nombre completo
        comboPlantilla.requestFocus();
        
    }
    
    public void editaRegistro(){
        //buEditar.setEnabled(false);
        //buAgregar.setEnabled(false);
        //buEliminar.setEnabled(false);
        setTitle("Editar asiento");
        TimeZone gmtZone = TimeZone.getTimeZone("America/Buenos_Aires");
        DateFormat destDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        destDateFormat.setTimeZone(gmtZone);        
        
        wb_nuevo=false;
        jTId.setText(Integer.toString(registroSel.getId()));
        jTFechaCarga.setText(destDateFormat.format(registroSel.getFecCarga()));

        jDateFecha.setDate(registroSel.getFecMov());
        jTDescripcion.setText(registroSel.getDescripcion());
        
        //Obtener usuario en base a Id
        BeanBase beanBase= new BeanBase();
        UsuarioAdmin usuario=beanBase.obtenerUsuario(registroSel.getIdUsuario());
        jtUsuario.setText(usuario.getNombreCompletoUsuario());
        
        //Solo usuarios autorizados pueden cambiarla
        String ls_permiso;
        ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("AsientaFechaAnterior");
        beanBase= new BeanBase();
        if (!beanBase.validarPermiso(PrincipalFrame.usuarioAdmin.getNombreUsuario(),ls_permiso,false))
            jDateFecha.setEnabled(false);

        ListaDetalle l;
        Plantilla p=registroSel.getPlantilla();
        if (p==null)
            l= new ListaDetalle(0,"(Ninguna)");
        else
            l= new ListaDetalle(registroSel.getPlantilla().getId(),String.valueOf(registroSel.getPlantilla().getNombre()));
        
        comboPlantilla.setSelectedItem(l);
        totalDebe=0;
        totalHaber=0;
        muestraListaCuentas();
        
        //Reviso estado del período
        java.util.Calendar fecha_mov = java.util.Calendar.getInstance();
        fecha_mov.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
        fecha_mov.setTime(registroSel.getFecMov());
        fecha_mov.set(Calendar.HOUR_OF_DAY, 0);
        fecha_mov.set(Calendar.MINUTE, 0);
        fecha_mov.set(Calendar.SECOND, 0);
        fecha_mov.set(Calendar.MILLISECOND, 0);
        
        //Obtengo año y mes de la fecha del movimiento para ver si el períoodo está abierto
        //y se puede grabar
        short li_anio = (short) fecha_mov.get(Calendar.YEAR);
        short li_mes = (short) fecha_mov.get(Calendar.MONTH);
        li_mes++;

        //Veo si tiene permiso para modificar asientos
        boolean lb_modificar_asiento=true;
        ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("ModificarAsiento");
        beanBase= new BeanBase();
        if (!beanBase.validarPermiso(PrincipalFrame.usuarioAdmin.getNombreUsuario(),ls_permiso,false))
            lb_modificar_asiento=false;
        //No se puede modificar asiento    
        lb_modificar_asiento=false;
        
        char ls_estado='N';
        ls_estado=buscaPeriodo(li_anio,li_mes); 
        if (ls_estado=='C' || !lb_modificar_asiento){
            //Período del asiento está cerrado o no tiene permiso para modificar un asiento
            //Asiento no puede ser modificado
            //asientoEditable=false;
            jDateFecha.setEnabled(false);
            //jTDescripcion.setEditable(false);
            //buAgregar.setEnabled(false);
            //buEditar.setEnabled(false);
            //buEliminar.setEnabled(false);
            //comboPlantilla.setEnabled(false);
            //buAceptar.setEnabled(false);
        }

        
    }
    
    public boolean grabaRegistro()
    {
        
        //Actualizo totales
        actualizaTotales();
        
        String ls_desc;
        ListaDetalle t= (ListaDetalle) comboPlantilla.getSelectedItem();
        Plantilla plantilla= (Plantilla) buscaPlantilla(t.getCodigo());
        
        ls_desc=jTDescripcion.getText().trim();
        if (ls_desc==null || ls_desc.equals("")){
          JOptionPane.showMessageDialog(null, "Debe ingresar la descripción","Datos incompletos",JOptionPane.WARNING_MESSAGE);
          jTDescripcion.requestFocus();
          return false;
        }

        //Fecha del asiento
        java.util.Date lda_fec_mov=jDateFecha.getDate();
        if (lda_fec_mov==null ){
          JOptionPane.showMessageDialog(null, "Debe ingresar la fecha del asiento","Datos incompletos",JOptionPane.WARNING_MESSAGE);
          jDateFecha.requestFocus();
          return false;
        }
        
        if (lda_fec_mov.compareTo(PrincipalFrame.fec_ini_ejer) < 0 || lda_fec_mov.compareTo(PrincipalFrame.fec_fin_ejer) > 0  ){
          JOptionPane.showMessageDialog(null,"Fecha del asiento incorrecta. Intervalo del ejercicio: " + PrincipalFrame.ejercicioSel,"Error",JOptionPane.WARNING_MESSAGE);
          jDateFecha.requestFocus();
          return false;
            
        }
        
        if (totalDebe != totalHaber){
              JOptionPane.showMessageDialog(null, "Asiento descuadrado","Datos incompletos",JOptionPane.WARNING_MESSAGE);
              jDateFecha.requestFocus();
              return false;
        }

        if (totalDebe == 0 ){
              JOptionPane.showMessageDialog(null, "Valor en Debe tiene que ser mayor a cero","Datos incompletos",JOptionPane.WARNING_MESSAGE);
              jDateFecha.requestFocus();
              return false;
        }

        if (totalHaber == 0 ){
              JOptionPane.showMessageDialog(null, "Valor en Haber tiene que ser mayor a cero","Datos incompletos",JOptionPane.WARNING_MESSAGE);
              jDateFecha.requestFocus();
              return false;
        }
        
        //Fecha y hora de carga actual
        TimeZone gmtZone = TimeZone.getTimeZone("America/Buenos_Aires");
        DateFormat destDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        destDateFormat.setTimeZone(gmtZone);
        Date lda_fec_carga = new Date();

        java.util.Calendar fecha_mov = java.util.Calendar.getInstance();
        fecha_mov.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
        fecha_mov.setTime(lda_fec_mov);
        fecha_mov.set(Calendar.HOUR_OF_DAY, 0);
        fecha_mov.set(Calendar.MINUTE, 0);
        fecha_mov.set(Calendar.SECOND, 0);
        fecha_mov.set(Calendar.MILLISECOND, 0);
        
        //Obtengo año y mes de la fecha del movimiento para ver si el períoodo está abierto
        //y se puede grabar
        short li_anio = (short) fecha_mov.get(Calendar.YEAR);
        short li_mes = (short) fecha_mov.get(Calendar.MONTH);
        li_mes++;
        
        char ls_estado='N';
        ls_estado=buscaPeriodo(li_anio,li_mes); 
        
        if (ls_estado=='C')
        {
            JOptionPane.showMessageDialog(null, "El período correspondiente a la fecha del asiento se encuentra cerrado","",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (fecha_mov.getTime().compareTo(lda_fec_carga) > 0){
            JOptionPane.showMessageDialog(null, "Fecha del movimiento no puede ser mayor a fecha actual","",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        Asiento p= this.getRegistroSel();
        p.setEjercicio(PrincipalFrame.ejercicioSel);
        p.setPlantilla(plantilla);
        //Si es un asiento nuevo grabo la fecha de carga y el usuario
        if (wb_nuevo){
            p.setFecCarga(lda_fec_carga);
            p.setFecMov(fecha_mov.getTime());
            p.setIdUsuario(PrincipalFrame.usuarioAdmin.getId());
        }

        p.setDescripcion(ls_desc);
        
        //Grabo en la base
        if (actualizaBaseDatos(p)){
            JOptionPane.showMessageDialog(null, "Actualización exitosa","",JOptionPane.INFORMATION_MESSAGE);
            if (wb_nuevo){
                jTId.setText(Integer.toString(p.getId()));
                buAceptar.setEnabled(false);
            }
            
            //Grabo la auditoría de la transacción
            String ls_observaciones="";
            String ls_permiso="";
            if (wb_nuevo){
                jTId.setText(Integer.toString(p.getId()));
                ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("RealizarAsiento");
                ls_observaciones="Asiento : " + String.valueOf(p.getId()+  " " + ls_desc);
                wb_nuevo=false;
            }
            else
            {
                ls_permiso=ResourceBundle.getBundle("general/Permisos").getString("ModificarAsiento");
                ls_observaciones="Modificación de Asiento : " + String.valueOf(p.getId()+  " " + ls_desc);
            }
            
            BeanBase bean= new BeanBase();
            try {
                boolean lb_resul=bean.grabaAuditoria(PrincipalFrame.usuarioAdmin.getId(),ls_permiso,ls_observaciones,bean.getNombreAplicacion(),bean.obtieneNombreEquipo());
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(AsientoDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            buAceptar.setEnabled(false);
            buEditar.setEnabled(false);
            buAgregar.setEnabled(false);
            buEliminar.setEnabled(false);
            comboPlantilla.setEnabled(false);
            jDateFecha.setEnabled(false);
            jTDescripcion.setEditable(false);
            return true;    
        }
        else
            return false;
        
    }    
    
    private boolean actualizaBaseDatos(Object o){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Asiento u= (Asiento) o;
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
    
    public Object buscaPlantilla(int id){
        Plantilla p= null;
        Session session=HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            p=(Plantilla) session.get(Plantilla.class,id);
            Hibernate.initialize(p.getPlantillaDets());
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
    
    public Object buscaAsientoDet(int id){
        AsientoDet p= null;
        Session session=HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            p=(AsientoDet) session.get(AsientoDet.class,id);
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
    
    
    private char buscaPeriodo(short anio,short mes) {
        char ls_estado = 'N';
        Session session = HibernateUtil.getSessionFactory().openSession();        
        try {

            session.beginTransaction();
            Query q=session.createQuery("from Periodo a where a.ejercicio = :ejercicioSel "
                + "and a.anio = :anioSel "
                + "and a.mes  = :mesSel ");
            q.setParameter("ejercicioSel",PrincipalFrame.ejercicioSel);
            q.setParameter("anioSel",anio);
            q.setParameter("mesSel",mes);
            List resultList = q.list();
            session.getTransaction().commit();    
        
            if (resultList.size()==1){
                Periodo per=(Periodo) resultList.get(0);
                ls_estado=per.getEstado();
            }

        
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        finally {
            session.close();
        }
        return ls_estado;
    }
    
    
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
            java.util.logging.Logger.getLogger(AsientoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AsientoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AsientoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AsientoDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AsientoDialog().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buAceptar;
    private javax.swing.JButton buAgregar;
    private javax.swing.JButton buEditar;
    private javax.swing.JButton buEliminar;
    private javax.swing.JComboBox comboPlantilla;
    private javax.swing.JTable footerAsiento;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private com.toedter.calendar.JDateChooser jDateFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelCab;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTDescripcion;
    private javax.swing.JTextField jTFechaCarga;
    private javax.swing.JTextField jTId;
    private javax.swing.JTextField jtUsuario;
    private javax.swing.JTable listaCuentas;
    // End of variables declaration//GEN-END:variables
}
