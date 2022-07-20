/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package impresion;

import contabilidad.PrincipalFrame;
import entidades.Asiento;
import entidades.AsientoDet;
import entidades.CentroCosto;
import entidades.Cuenta;
import entidades.Ejercicio;
import entidades.Empresa;
import general.BeanBase;
import general.UsuarioAdmin;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rafaelg
 */
public class ImpresionController extends BeanBase{
    
    //Impresión de siento contable
    public void generaFormularioAsiento(Asiento asiento) throws Exception {

       //Traigo los datos de la empresa
       Empresa e= PrincipalFrame.empresaSel;
       
       //Parámetros del formulario
       Map<String,Object> params = new HashMap<String,Object>();
       //Hay que cambiar por datos reales de empresa
       params.put("empresa",e.getNombre());
       params.put("ciudad","Santiago del Estero");
       params.put("SUBREPORT_DIR",this.getConfiguracion().getProperty("dirFormularios").trim());
       
       //Formulario a usar
       String  reporteJasper="";
       String ls_formulario="";
       ls_formulario=this.getConfiguracion().getProperty("asientoContable").trim();
       reporteJasper=this.getConfiguracion().getProperty("dirFormularios").trim() + ls_formulario + ".jasper";       

       //Armo el asiento a imprimir
       AsientoCab asientoCab= new AsientoCab();
       asientoCab.setEmpresa(e.getNombre());
       asientoCab.setAsiento(asiento.getId());
       asientoCab.setDescripcion(asiento.getDescripcion());
       asientoCab.setFecha(asiento.getFecMov());
       UsuarioAdmin usuario=obtenerUsuario(asiento.getIdUsuario());
       asientoCab.setUsuario(usuario.getNombreCompletoUsuario());
       asientoCab.setMoneda(e.getMoneda().getNombre());
       
       //Agrego las cuentas del asiento
       ArrayList<AsientoDetalle> listaCuentas = new ArrayList<AsientoDetalle>();
       Iterator i= asiento.getAsientoDets().iterator();
       while (i.hasNext()){
            AsientoDet p= (AsientoDet) i.next();
            AsientoDetalle d = new AsientoDetalle();
            d.setNumero_cuenta(p.getCuenta().getCuentaNumero());
            d.setNombre_cuenta(p.getCuenta().getCuentaNombre());
            d.setCentro_costo( (p.getCentroCosto() == null ? "" : p.getCentroCosto().getNombre()));
            d.setDc(String.valueOf(p.getDc()));
            d.setValor(p.getValor().doubleValue());
            listaCuentas.add(d);
        }
       asientoCab.setListaCuentas(listaCuentas);
       
       ArrayList listaAsientos= new ArrayList();
       listaAsientos.add(asientoCab);
      
       //Genero el archivo en el visor
       Reporte reporte = new Reporte();
       reporte.generaFormulario(reporteJasper, params, listaAsientos);
       
     }
    
    
    //Mayor por cuenta
    public void generaReporteMayorCuenta(Cuenta cuenta,ArrayList listaMovimientos,double saldo_anterior,String intervalo) throws Exception {

       //Traigo los datos de la empresa
       Empresa e= PrincipalFrame.empresaSel;
        
       //Parámetros del formulario
       Map<String,Object> params = new HashMap<String,Object>();
       params.put("empresa",e.getNombre());
       params.put("ciudad","Santiago del Estero");
       params.put("moneda",e.getMoneda().getNombre());
       params.put("cuenta",cuenta.getCuentaNumero() + " - " + cuenta.getCuentaNombre());
       params.put("saldo_anterior",saldo_anterior);
       params.put("intervalo",intervalo);
       params.put("ejercicio","Ejercicio: "  + PrincipalFrame.ejercicioSel );       
       params.put("SUBREPORT_DIR",this.getConfiguracion().getProperty("dirFormularios").trim());
       
       //Formulario a usar
       String  reporteJasper="";
       String ls_formulario="";
       ls_formulario=this.getConfiguracion().getProperty("MayorCuenta").trim();
       reporteJasper=this.getConfiguracion().getProperty("dirFormularios").trim() + ls_formulario + ".jasper";       

       //Genero el archivo en el visor
       Reporte reporte = new Reporte();
       reporte.generaFormulario(reporteJasper, params, listaMovimientos);
       
     }
    
    //Mayor por cuenta por Centro de Costo
    public void generaReporteMayorCuentaCC(Cuenta cuenta,CentroCosto centroCosto,ArrayList listaMovimientos,double saldo_anterior,String intervalo) throws Exception {

       //Traigo los datos de la empresa
       Empresa e= PrincipalFrame.empresaSel;
        
       //Parámetros del formulario
       Map<String,Object> params = new HashMap<String,Object>();
       params.put("empresa",e.getNombre());
       params.put("ciudad","Santiago del Estero");
       params.put("moneda",e.getMoneda().getNombre());
       params.put("cuenta",cuenta.getCuentaNumero() + " - " + cuenta.getCuentaNombre());
       params.put("centroCosto",centroCosto.getNombre());
       params.put("saldo_anterior",saldo_anterior);
       params.put("intervalo",intervalo);
       params.put("ejercicio","Ejercicio: "  + PrincipalFrame.ejercicioSel );       
       params.put("SUBREPORT_DIR",this.getConfiguracion().getProperty("dirFormularios").trim());
       
       //Formulario a usar
       String  reporteJasper="";
       String ls_formulario="";
       ls_formulario=this.getConfiguracion().getProperty("MayorCuentaCC").trim();
       reporteJasper=this.getConfiguracion().getProperty("dirFormularios").trim() + ls_formulario + ".jasper";       

       //Genero el archivo en el visor
       Reporte reporte = new Reporte();
       reporte.generaFormulario(reporteJasper, params, listaMovimientos);
       
     }
    
    //Balance de comprobación de sumas y saldos
    public void generaBalanceComprobacion(Ejercicio ejercicioSel,ArrayList listaMovimientos,String intervalo) throws Exception {

       //Traigo los datos de la empresa
       Empresa e= PrincipalFrame.empresaSel;
        
       //Parámetros del formulario
       Map<String,Object> params = new HashMap<String,Object>();
       params.put("empresa",e.getNombre());
       params.put("ciudad","Santiago del Estero");
       params.put("moneda",e.getMoneda().getNombre());
       params.put("intervalo",intervalo);
       params.put("ejercicio","Ejercicio: "  + PrincipalFrame.ejercicioSel );       
       params.put("SUBREPORT_DIR",this.getConfiguracion().getProperty("dirFormularios").trim());
       
       //Formulario a usar
       String  reporteJasper="";
       String ls_formulario="";
       ls_formulario=this.getConfiguracion().getProperty("BalanceComprobacion").trim();
       reporteJasper=this.getConfiguracion().getProperty("dirFormularios").trim() + ls_formulario + ".jasper";       

       //Genero el archivo en el visor
       Reporte reporte = new Reporte();
       reporte.generaFormulario(reporteJasper, params, listaMovimientos);
       
     }
    
}
