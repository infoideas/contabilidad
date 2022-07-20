/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contabilidad;

import static contabilidad.CuentaDialog.addEscapeListener;
import entidades.AsientoDet;
import entidades.Cuenta;
import entidades.Ejercicio;
import entidades.Empresa;
import entidades.PlantillaParam;
import entidades.Plantilla;
import entidades.PlantillaDet;
import general.HibernateUtil;
import general.ListaDetalle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
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
 * @author Propietario
 */
public class PlantillaDialog extends javax.swing.JDialog {
    public boolean mod;
    public boolean wb_nuevo;
    public Plantilla registroSel=new Plantilla();
    Vector tableData = new Vector();
    Vector tableDataPar = new Vector();
    Vector<String> tableHeaders = new Vector<>();
    Vector<String> tableHeadersPar = new Vector<>();

    public Plantilla getRegistroSel() {
        return registroSel;
    }

    public void setRegistroSel(Plantilla registroSel) {
        this.registroSel = registroSel;
    }
    

    /**
     * Creates new form PlantillaDialog
     */
    public PlantillaDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
                //Botón Default
        getRootPane().setDefaultButton(buAceptar);
        //Listener para cerrar la ventana con la tecla Escape
        addEscapeListener(this);
        
        //Cabecera Cuentas
        tableHeaders.add("Objeto");
        tableHeaders.add("Cuenta"); 
        tableHeaders.add("Nombre");
        tableHeaders.add("D/C");
        tableHeaders.add("Fórmula");

        //Cabecera Parámetros
        tableHeadersPar.add("Objeto");
        tableHeadersPar.add("Orden"); 
        tableHeadersPar.add("Nombre del parámetro"); 
        
        //Modelo de la tabla de cuentas
        DefaultTableCellRenderer AlinearDerecha = new DefaultTableCellRenderer();
        AlinearDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        DefaultTableCellRenderer AlinearCentro = new DefaultTableCellRenderer();
        AlinearCentro.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer AlinearIzquierda = new DefaultTableCellRenderer();
        AlinearIzquierda.setHorizontalAlignment(SwingConstants.LEFT);
                
        
        DefaultTableModel modelo = new DefaultTableModel(tableData,tableHeaders)
        {@Override
                public boolean isCellEditable (int fila, int columna) {
                if (columna >= 0)
                    return false;
                
                    return true;
                }
        };     

        DefaultTableModel modeloPar = new DefaultTableModel(tableDataPar,tableHeadersPar)
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
        listaCuentas.getColumnModel().getColumn(0).setPreferredWidth(0);
        listaCuentas.getColumnModel().getColumn(0).setMinWidth(0);
        listaCuentas.getColumnModel().getColumn(0).setMaxWidth(0);
        listaCuentas.getColumnModel().getColumn(1).setPreferredWidth(30);
        listaCuentas.getColumnModel().getColumn(2).setPreferredWidth(200);
        listaCuentas.getColumnModel().getColumn(3).setPreferredWidth(30);
        listaCuentas.getColumnModel().getColumn(4).setPreferredWidth(250);
        
        
        //Alineo las columnas necesarias
        listaCuentas.getColumnModel().getColumn(1).setCellRenderer(AlinearIzquierda);
        listaCuentas.getColumnModel().getColumn(2).setCellRenderer(AlinearIzquierda);
        listaCuentas.getColumnModel().getColumn(3).setCellRenderer(AlinearCentro);
        listaCuentas.getColumnModel().getColumn(4).setCellRenderer(AlinearCentro);   
        listaCuentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Especifico ancho de columnas de JTable de parámetros
        //Los campor de Id lo pongo para que no se vean
        listaPar.setModel(modeloPar);
        listaPar.getColumnModel().getColumn(0).setPreferredWidth(0);
        listaPar.getColumnModel().getColumn(0).setMinWidth(0);
        listaPar.getColumnModel().getColumn(0).setMaxWidth(0);
        listaPar.getColumnModel().getColumn(1).setPreferredWidth(50);
        listaPar.getColumnModel().getColumn(2).setPreferredWidth(200);
        
        //Alineo las columnas necesarias
        listaPar.getColumnModel().getColumn(2).setCellRenderer(AlinearIzquierda);
        listaPar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Agrego listener para doble click en lista de cuentas
        listaCuentas.addMouseListener
        (
    	new MouseAdapter()
    	{
    		public void mouseClicked(MouseEvent evnt)
    		{
                  if (evnt.getClickCount() == 2)
                    {
                  	System.out.println("Doble Clic");
                        JTable target = (JTable)evnt.getSource();
                        int row = target.getSelectedRow(); // select a row
                        int column = target.getSelectedColumn(); // select a column
                        consultaRegistroCuenta();                        
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
                  	System.out.println("Doble Clic");
                        JTable target = (JTable)evnt.getSource();
                        int row = target.getSelectedRow(); // select a row
                        int column = target.getSelectedColumn(); // select a column
                        consultaRegistroPar();                        
                    }
    		}
    	}
        );

    }

    public DefaultTableModel creaModelo(){
        //Cabecera Cuentas
        tableHeaders.clear();
        tableHeaders.add("Objeto");
        tableHeaders.add("Cuenta"); 
        tableHeaders.add("Nombre");
        tableHeaders.add("D/C");
        tableHeaders.add("Fórmula");

        //Cabecera Parámetros
        tableHeadersPar.clear();
        tableHeadersPar.add("Objeto");
        tableHeadersPar.add("Orden"); 
        tableHeadersPar.add("Nombre del parámetro"); 
        
        //Modelo de la tabla de cuentas
        DefaultTableCellRenderer AlinearDerecha = new DefaultTableCellRenderer();
        AlinearDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        DefaultTableCellRenderer AlinearCentro = new DefaultTableCellRenderer();
        AlinearCentro.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer AlinearIzquierda = new DefaultTableCellRenderer();
        AlinearIzquierda.setHorizontalAlignment(SwingConstants.LEFT);

        tableData.clear();
        DefaultTableModel modelo = new DefaultTableModel(tableData,tableHeaders)
        {@Override
                public boolean isCellEditable (int fila, int columna) {
                if (columna >= 0)
                    return false;
                
                    return true;
                }
        };     

        tableDataPar.clear();
        DefaultTableModel modeloPar = new DefaultTableModel(tableDataPar,tableHeadersPar)
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
        listaCuentas.getColumnModel().getColumn(0).setPreferredWidth(0);
        listaCuentas.getColumnModel().getColumn(0).setMinWidth(0);
        listaCuentas.getColumnModel().getColumn(0).setMaxWidth(0);
        listaCuentas.getColumnModel().getColumn(1).setPreferredWidth(30);
        listaCuentas.getColumnModel().getColumn(2).setPreferredWidth(200);
        listaCuentas.getColumnModel().getColumn(3).setPreferredWidth(30);
        listaCuentas.getColumnModel().getColumn(4).setPreferredWidth(250);
        
        //Especifico ancho de columnas de JTable de parámetros
        //Los campor de Id lo pongo para que no se vean
        listaPar.setModel(modeloPar);
        listaPar.getColumnModel().getColumn(0).setPreferredWidth(0);
        listaPar.getColumnModel().getColumn(0).setMinWidth(0);
        listaPar.getColumnModel().getColumn(0).setMaxWidth(0);
        listaPar.getColumnModel().getColumn(1).setPreferredWidth(50);
        listaPar.getColumnModel().getColumn(2).setPreferredWidth(200);
        
        //Alineo las columnas necesarias
        listaPar.getColumnModel().getColumn(2).setCellRenderer(AlinearIzquierda);
        listaPar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        return modelo;
        
        
    }
    
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
    
    private void muestraListaCuentas() {
        DefaultTableModel modelo = (DefaultTableModel) listaCuentas.getModel();
        tableData.clear();
        Iterator i= registroSel.getPlantillaDets().iterator();
        while (i.hasNext()){
            PlantillaDet p= (PlantillaDet) i.next();
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(p);  
            oneRow.add(p.getCuenta().getCuentaNumero());
            oneRow.add(p.getCuenta().getCuentaNombre());
            oneRow.add(p.getDc());
            oneRow.add(p.getFormula());
            modelo.addRow(oneRow);
        }
        listaCuentas.setModel(modelo);
    }
    
    private void muestraListaPar() {
        DefaultTableModel modelo = (DefaultTableModel) listaPar.getModel();
        tableDataPar.clear();
        Iterator i= registroSel.getPlantillaParams().iterator();
        while (i.hasNext()){
            PlantillaParam p= (PlantillaParam) i.next();
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(p);  
            oneRow.add(p.getOrden());
            oneRow.add(p.getNombre());
            modelo.addRow(oneRow);
        }
        listaPar.setModel(modelo);
    }
    
    public void agregaCuenta(PlantillaDet p) {
        
        Vector<Object> oneRow = new Vector<Object>();
        oneRow.add(p.getCuenta().getId());
        oneRow.add(p.getCuenta().getCuentaNumero());
        oneRow.add(p.getCuenta().getCuentaNombre());
        oneRow.add(p.getDc());
        oneRow.add(p.getFormula());
        tableData.add(oneRow);
        
        registroSel.getPlantillaDets().add(p);
        
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
        jTNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jFTNumero = new javax.swing.JFormattedTextField();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelCuentas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaCuentas = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        buEditarCuenta = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        buNuevaCuenta = new javax.swing.JButton();
        jPanelPar = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaPar = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        buAceptar = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

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
        setTitle("Plantilla contable");
        setModal(true);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Nombre:");

        jTNombre.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Id:");

        jTId.setEditable(false);
        jTId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Número:");

        jFTNumero.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#####"))));
        jFTNumero.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanelCabLayout = new javax.swing.GroupLayout(jPanelCab);
        jPanelCab.setLayout(jPanelCabLayout);
        jPanelCabLayout.setHorizontalGroup(
            jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTNombre)
                    .addGroup(jPanelCabLayout.createSequentialGroup()
                        .addComponent(jTId, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFTNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelCabLayout.setVerticalGroup(
            jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCabLayout.createSequentialGroup()
                .addGroup(jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jFTNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        listaCuentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id", "Cuenta", "Nombre", "D/C", "Fórmula"
            }
        ));
        listaCuentas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(listaCuentas);

        buEditarCuenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        buEditarCuenta.setText("Editar cuenta");
        buEditarCuenta.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buEditarCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buEditarCuentaActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setText("Eliminar cuenta");
        jButton2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buEditarCuenta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                .addGap(0, 20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(buEditarCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {buEditarCuenta, jButton2});

        buNuevaCuenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        buNuevaCuenta.setText("Nueva cuenta");
        buNuevaCuenta.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buNuevaCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buNuevaCuentaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCuentasLayout = new javax.swing.GroupLayout(jPanelCuentas);
        jPanelCuentas.setLayout(jPanelCuentasLayout);
        jPanelCuentasLayout.setHorizontalGroup(
            jPanelCuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCuentasLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buNuevaCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        jPanelCuentasLayout.setVerticalGroup(
            jPanelCuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCuentasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                    .addGroup(jPanelCuentasLayout.createSequentialGroup()
                        .addComponent(buNuevaCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jTabbedPane.addTab("Cuentas", jPanelCuentas);

        listaPar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Orden", "Nombre"
            }
        ));
        jScrollPane2.setViewportView(listaPar);

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton3.setText("Nuevo");
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

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setText("Editar");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(178, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelParLayout = new javax.swing.GroupLayout(jPanelPar);
        jPanelPar.setLayout(jPanelParLayout);
        jPanelParLayout.setHorizontalGroup(
            jPanelParLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelParLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 731, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanelParLayout.setVerticalGroup(
            jPanelParLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelParLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelParLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelParLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(67, 67, 67))
                    .addGroup(jPanelParLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        jTabbedPane.addTab("Parámetros", jPanelPar);

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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buAceptar, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelCab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanelCab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
        int li_orden;
        li_orden=registroSel.getPlantillaParams().size() + 1;
        
        ParametroDialog dialog = new ParametroDialog(this, true);
        dialog.setLocationRelativeTo(null);
        dialog.setOrden(li_orden);
        dialog.nuevoRegistro();
        dialog.setVisible(true);
        
        
        PlantillaParam p= dialog.getRegistroSel();
        if (p.getNombre() != null){

            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(p);
            oneRow.add(p.getOrden());
            oneRow.add(p.getNombre());
            
            DefaultTableModel modelo = (DefaultTableModel) listaPar.getModel();
            modelo.addRow(oneRow);
            listaPar.setModel(modelo);
        
            p.setPlantilla(registroSel);
            registroSel.getPlantillaParams().add(p);
            
        }        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void buEditarCuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buEditarCuentaActionPerformed
        // TODO add your handling code here:
        Object[] opciones = {"Sí","No"};  //Opciones de la ventana de confirmación
        int li_id=0,li_id_cuenta=0;
        int li_fila_sel=listaCuentas.getSelectedRow();  //Fila seleccionada
        if (li_fila_sel == -1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro","",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        PlantillaDet p= (PlantillaDet) listaCuentas.getValueAt(li_fila_sel,0);
        
        BuscaCuentaPlantillaDialog dialog = new BuscaCuentaPlantillaDialog(this, true);
        dialog.setLocationRelativeTo(null);
        dialog.setCuentaSel(p);
        dialog.setPlantillaSel(registroSel);
        dialog.editaRegistro();
        dialog.setVisible(true);
        
        p= dialog.getCuentaSel();
        
        if (registroSel.getPlantillaDets().add(p));
        {
                //Modifico registro en datatable
                DefaultTableModel modelo = (DefaultTableModel) listaCuentas.getModel();
                //Modifico Jtable
                DecimalFormat df = new DecimalFormat( "#,###.00");   
                modelo.setValueAt(p,li_fila_sel,0);
                modelo.setValueAt(p.getCuenta().getCuentaNumero(),li_fila_sel,1);
                modelo.setValueAt(p.getCuenta().getCuentaNombre(),li_fila_sel,2);
                modelo.setValueAt(p.getDc(),li_fila_sel,3);
                modelo.setValueAt(p.getFormula(),li_fila_sel,4);                
                
                listaCuentas.setModel(modelo);
                listaCuentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
    }//GEN-LAST:event_buEditarCuentaActionPerformed

    private void buAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buAceptarActionPerformed
        // TODO add your handling code here:
         if (grabaRegistro())
           dispose();
    }//GEN-LAST:event_buAceptarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
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
                
            PlantillaDet p= (PlantillaDet) listaCuentas.getValueAt(li_fila_sel,0);
            
            if (registroSel.getPlantillaDets().remove(p)){
                DefaultTableModel modelo = (DefaultTableModel) listaCuentas.getModel();
                modelo.removeRow(li_fila_sel);
                listaCuentas.setModel(modelo);
                
            }
                
            }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void buNuevaCuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buNuevaCuentaActionPerformed
        // TODO add your handling code here:
        BuscaCuentaPlantillaDialog dialog = new BuscaCuentaPlantillaDialog(this, true);
        dialog.setLocationRelativeTo(null);
        dialog.setPlantillaSel(registroSel);
        dialog.nuevoRegistro();
        dialog.setVisible(true);
        
        
        PlantillaDet p= dialog.getCuentaSel();
        if (p.getCuenta() != null){

            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(p);
            oneRow.add(p.getCuenta().getCuentaNumero());
            oneRow.add(p.getCuenta().getCuentaNombre());
            oneRow.add(p.getDc());
            oneRow.add(p.getFormula());
            
            DefaultTableModel modelo = (DefaultTableModel) listaCuentas.getModel();
            modelo.addRow(oneRow);
            listaCuentas.setModel(modelo);
        
            p.setPlantilla(registroSel);
            registroSel.getPlantillaDets().add(p);
            
        }
    }//GEN-LAST:event_buNuevaCuentaActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        Object[] opciones = {"Sí","No"};  //Opciones de la ventana de confirmación
        int li_fila_sel=listaPar.getSelectedRow();  //Fila seleccionada
        if (li_fila_sel == -1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro","",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int li_respuesta=JOptionPane.showOptionDialog(new JFrame(),"Está seguro de eliminar?","Eliminar rubro",JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE , null,opciones,opciones[0]);
            if (li_respuesta == JOptionPane.YES_OPTION)
            {
                
            PlantillaParam p= (PlantillaParam) listaPar.getValueAt(li_fila_sel,0);
            
            if (registroSel.getPlantillaParams().remove(p)){
                DefaultTableModel modelo = (DefaultTableModel) listaPar.getModel();
                modelo.removeRow(li_fila_sel);
                listaPar.setModel(modelo);
                
            }
                
            }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Object[] opciones = {"Sí","No"};  //Opciones de la ventana de confirmación
        int li_id=0,li_id_cuenta=0;
        int li_fila_sel=listaPar.getSelectedRow();  //Fila seleccionada
        if (li_fila_sel == -1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro","",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        PlantillaParam p= (PlantillaParam) listaPar.getValueAt(li_fila_sel,0);
        
        ParametroDialog dialog = new ParametroDialog(this, true);
        dialog.setLocationRelativeTo(null);
        dialog.setRegistroSel(p);
        dialog.editaRegistro();
        dialog.setVisible(true);
        
        p= dialog.getRegistroSel();
        
        if (registroSel.getPlantillaParams().add(p));
        {
                //Modifico registro en datatable
                DefaultTableModel modelo = (DefaultTableModel) listaPar.getModel();
                //Modifico Jtable
                modelo.setValueAt(p,li_fila_sel,0);
                modelo.setValueAt(p.getOrden(),li_fila_sel,1);
                modelo.setValueAt(p.getNombre(),li_fila_sel,2);
                
                listaPar.setModel(modelo);
                listaPar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    public void nuevoRegistro(){
        setTitle("Nueva plantilla");
        wb_nuevo=true;
        
        int li_ult_num_plantilla=obtenerUltimoNumPlantilla(PrincipalFrame.ejercicioSel);
        li_ult_num_plantilla++;
        jFTNumero.setValue(li_ult_num_plantilla);

        jTNombre.requestFocus();
        
        creaModelo();
        /**
        tableData.clear();
        listaCuentas.setModel(new DefaultTableModel(tableData, tableHeaders));
        
        listaCuentas.getColumnModel().getColumn(0).setPreferredWidth(0);
        listaCuentas.getColumnModel().getColumn(0).setMinWidth(0);
        listaCuentas.getColumnModel().getColumn(0).setMaxWidth(0);
        listaCuentas.getColumnModel().getColumn(1).setPreferredWidth(30);
        listaCuentas.getColumnModel().getColumn(2).setPreferredWidth(200);
        listaCuentas.getColumnModel().getColumn(3).setPreferredWidth(30);
        listaCuentas.getColumnModel().getColumn(4).setPreferredWidth(250);
        ***/

    }
    
    public void editaRegistro(){
        setTitle("Editar plantilla");
        wb_nuevo=false;
        jTId.setText(Integer.toString(registroSel.getId()));
        jTNombre.setText(registroSel.getNombre());
        jFTNumero.setValue(registroSel.getNumero());
        jTNombre.requestFocus();
        muestraListaCuentas();
        muestraListaPar();
        
    }
    
    public void consultaRegistroCuenta(){
        Object[] opciones = {"Sí","No"};  //Opciones de la ventana de confirmación
        int li_id=0,li_id_cuenta=0;
        int li_fila_sel=listaCuentas.getSelectedRow();  //Fila seleccionada
        if (li_fila_sel == -1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro","",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        PlantillaDet p= (PlantillaDet) listaCuentas.getValueAt(li_fila_sel,0);
        
        BuscaCuentaPlantillaDialog dialog = new BuscaCuentaPlantillaDialog(this, true);
        dialog.setLocationRelativeTo(null);
        dialog.setCuentaSel(p);
        dialog.setPlantillaSel(registroSel);
        dialog.editaRegistro();
        dialog.setVisible(true);
        
        p= dialog.getCuentaSel();
        
        if (registroSel.getPlantillaDets().add(p));
        {
            
                //Modifico registro en datatable
                DefaultTableModel modelo = (DefaultTableModel) listaCuentas.getModel();
                //Modifico Jtable
                DecimalFormat df = new DecimalFormat( "#,###.00");   
                modelo.setValueAt(p,li_fila_sel,0);
                modelo.setValueAt(p.getCuenta().getCuentaNumero(),li_fila_sel,1);
                modelo.setValueAt(p.getCuenta().getCuentaNombre(),li_fila_sel,2);
                modelo.setValueAt(p.getDc(),li_fila_sel,3);
                modelo.setValueAt(p.getFormula(),li_fila_sel,4);                
                
                listaCuentas.setModel(modelo);
                listaCuentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
            }
            
    }
    
    public void consultaRegistroPar(){
        Object[] opciones = {"Sí","No"};  //Opciones de la ventana de confirmación
        int li_id=0,li_id_cuenta=0;
        int li_fila_sel=listaPar.getSelectedRow();  //Fila seleccionada
        if (li_fila_sel == -1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro","",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        PlantillaParam p= (PlantillaParam) listaPar.getValueAt(li_fila_sel,0);
        
        ParametroDialog dialog = new ParametroDialog(this, true);
        dialog.setLocationRelativeTo(null);
        dialog.setRegistroSel(p);
        dialog.editaRegistro();
        dialog.setVisible(true);
        
        p= dialog.getRegistroSel();
        
        if (registroSel.getPlantillaParams().add(p));
        {
                //Modifico registro en datatable
                DefaultTableModel modelo = (DefaultTableModel) listaPar.getModel();
                //Modifico Jtable
                modelo.setValueAt(p,li_fila_sel,0);
                modelo.setValueAt(p.getOrden(),li_fila_sel,1);
                modelo.setValueAt(p.getNombre(),li_fila_sel,2);
                
                listaPar.setModel(modelo);
                listaPar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
    }
    

    public boolean grabaRegistro()
    {
        String ls_nombre;
        
        Object ls_numero=jFTNumero.getValue();
        if (ls_numero==null ){
          JOptionPane.showMessageDialog(null, "Debe ingresar el número de plantilla","Datos incompletos",JOptionPane.WARNING_MESSAGE);
          jFTNumero.requestFocus();
          return false;
        } 
        
        int li_numero;
        li_numero=Integer.valueOf( Integer.valueOf(jFTNumero.getValue().toString()));
        if (li_numero == 0){
          JOptionPane.showMessageDialog(null, "Debe ingresar el número de plantilla","Datos incompletos",JOptionPane.WARNING_MESSAGE);
          jFTNumero.requestFocus();
          return false;
        }
                       
        ls_nombre=jTNombre.getText().trim();
        if (ls_nombre==null || ls_nombre.equals("")){
          JOptionPane.showMessageDialog(null, "Debe ingresar el nombre de la plantilla","Datos incompletos",JOptionPane.WARNING_MESSAGE);
          jTNombre.requestFocus();
          return false;
        }
        
         
        Plantilla p= this.getRegistroSel();
        p.setEjercicio(PrincipalFrame.ejercicioSel);
        p.setNumero(li_numero);
        p.setNombre(ls_nombre);
        
        //Grabo en la base
        if (actualizaBaseDatos(p)){
            JOptionPane.showMessageDialog(null, "Actualización exitosa","",JOptionPane.INFORMATION_MESSAGE);
            if (wb_nuevo)
                jTId.setText(Integer.toString(p.getId()));
            return true;    
        }
        else
            return false;
        
    }    
    
    private boolean actualizaBaseDatos(Object o){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Plantilla u= (Plantilla) o;
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

    private int obtenerUltimoNumPlantilla(Ejercicio ejercicio) {
        int li_idUltimoNum=0;
        Session session = HibernateUtil.getSessionFactory().openSession();        
        try {
            session.beginTransaction();
            Query q=session.createQuery("select max(a.numero) from Plantilla a where a.ejercicio = :ejercicioSel");
            q.setParameter("ejercicioSel",ejercicio);
            List resultList = q.list();
            if (resultList.get(0) != null){
                //Hay un ejercicio anterior
                li_idUltimoNum=((Integer) resultList.get(0)).intValue();
            }
            session.getTransaction().commit();
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        finally {
            session.close();
        }
        return li_idUltimoNum;
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
            java.util.logging.Logger.getLogger(PlantillaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PlantillaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PlantillaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlantillaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PlantillaDialog dialog = new PlantillaDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton buEditarCuenta;
    private javax.swing.JButton buNuevaCuenta;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JFormattedTextField jFTNumero;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelCab;
    private javax.swing.JPanel jPanelCuentas;
    private javax.swing.JPanel jPanelPar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTId;
    private javax.swing.JTextField jTNombre;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTable listaCuentas;
    private javax.swing.JTable listaPar;
    // End of variables declaration//GEN-END:variables
}
