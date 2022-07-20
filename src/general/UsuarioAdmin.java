/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package general;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rafael
 */
@XmlRootElement (name = "usuarioAdmin")
public class UsuarioAdmin {
     private int id;
     private String nombreUsuario;
     private String nombreCompletoUsuario;     
     private String estado;
     private String observaciones;
     private int canti_empresas;

    public UsuarioAdmin() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreCompletoUsuario() {
        return nombreCompletoUsuario;
    }

    public void setNombreCompletoUsuario(String nombreCompletoUsuario) {
        this.nombreCompletoUsuario = nombreCompletoUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getCanti_empresas() {
        return canti_empresas;
    }

    public void setCanti_empresas(int canti_empresas) {
        this.canti_empresas = canti_empresas;
    }

   

}
