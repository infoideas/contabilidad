/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

/**
 *
 * @author Propietario
 */
public class ListaDetalle {
    private int codigo;
    private String nombre;

    public ListaDetalle() {
    }

    public ListaDetalle(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
     public String toString()
    {
     return nombre;
    }
     
    @Override  
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof ListaDetalle)) {
            return false;
        }
        ListaDetalle detalle = (ListaDetalle) o;
        if (codigo != detalle.codigo) {
            return false;
        }
        if (nombre != null ? !nombre.equals(detalle.nombre) : detalle.nombre != null) {
            return false;
        }
        return true;
    }

    @Override      
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        hash = 89 * hash + this.codigo;
        return hash;
    }
    
    
    
}
