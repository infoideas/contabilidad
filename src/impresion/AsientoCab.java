/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package impresion;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author rafaelg
 */
public class AsientoCab {
    private String empresa;
    private int asiento;
    private String descripcion;
    private String moneda;
    private Date fecha;
    private String usuario;
    private ArrayList<AsientoDetalle> listaCuentas;

    public AsientoCab() {
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public int getAsiento() {
        return asiento;
    }

    public void setAsiento(int asiento) {
        this.asiento = asiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public ArrayList<AsientoDetalle> getListaCuentas() {
        return listaCuentas;
    }

    public void setListaCuentas(ArrayList<AsientoDetalle> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }
    
    
    
    
}
