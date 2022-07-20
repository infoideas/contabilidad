package entidades;
// Generated 13 nov. 2021 10:48:18 by Hibernate Tools 4.3.1


import general.BeanBase;
import java.util.HashSet;
import java.util.Set;

/**
 * Ejercicio generated by hbm2java
 */
public class Ejercicio  implements java.io.Serializable {


     private Integer id;
     private Empresa empresa;
     private short mesInicio;
     private short anioInicio;
     private short mesFin;
     private short anioFin;
     private Set<Cuenta> cuentas = new HashSet<Cuenta>(0);
     private Set<CentroCosto> centroCostos = new HashSet<CentroCosto>(0);
     private Set<Periodo> periodos = new HashSet<Periodo>(0);
     private Set<Plantilla> plantillas = new HashSet<Plantilla>(0);
     private Set<Asiento> asientos = new HashSet<Asiento>(0);

    public Ejercicio() {
    }

	
    public Ejercicio(Empresa empresa, short mesInicio, short anioInicio, short mesFin, short anioFin) {
        this.empresa = empresa;
        this.mesInicio = mesInicio;
        this.anioInicio = anioInicio;
        this.mesFin = mesFin;
        this.anioFin = anioFin;
    }
    public Ejercicio(Empresa empresa, short mesInicio, short anioInicio, short mesFin, short anioFin, Set<Cuenta> cuentas, Set<CentroCosto> centroCostos, Set<Periodo> periodos, Set<Plantilla> plantillas, Set<Asiento> asientos) {
       this.empresa = empresa;
       this.mesInicio = mesInicio;
       this.anioInicio = anioInicio;
       this.mesFin = mesFin;
       this.anioFin = anioFin;
       this.cuentas = cuentas;
       this.centroCostos = centroCostos;
       this.periodos = periodos;
       this.plantillas = plantillas;
       this.asientos = asientos;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Empresa getEmpresa() {
        return this.empresa;
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    public short getMesInicio() {
        return this.mesInicio;
    }
    
    public void setMesInicio(short mesInicio) {
        this.mesInicio = mesInicio;
    }
    public short getAnioInicio() {
        return this.anioInicio;
    }
    
    public void setAnioInicio(short anioInicio) {
        this.anioInicio = anioInicio;
    }
    public short getMesFin() {
        return this.mesFin;
    }
    
    public void setMesFin(short mesFin) {
        this.mesFin = mesFin;
    }
    public short getAnioFin() {
        return this.anioFin;
    }
    
    public void setAnioFin(short anioFin) {
        this.anioFin = anioFin;
    }
    public Set<Cuenta> getCuentas() {
        return this.cuentas;
    }
    
    public void setCuentas(Set<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }
    public Set<CentroCosto> getCentroCostos() {
        return this.centroCostos;
    }
    
    public void setCentroCostos(Set<CentroCosto> centroCostos) {
        this.centroCostos = centroCostos;
    }
    public Set<Periodo> getPeriodos() {
        return this.periodos;
    }
    
    public void setPeriodos(Set<Periodo> periodos) {
        this.periodos = periodos;
    }
    public Set<Plantilla> getPlantillas() {
        return this.plantillas;
    }
    
    public void setPlantillas(Set<Plantilla> plantillas) {
        this.plantillas = plantillas;
    }
    public Set<Asiento> getAsientos() {
        return this.asientos;
    }
    
    public void setAsientos(Set<Asiento> asientos) {
        this.asientos = asientos;
    }

     @Override
    public String toString() {
        BeanBase b= new BeanBase();
        return b.mesLetras(mesInicio,"L").concat("/").concat(String.valueOf(anioInicio)).concat(" - ").concat(b.mesLetras(mesFin,"L").concat("/").concat(String.valueOf(anioFin)));
        
    }

   
}

