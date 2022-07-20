/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impresion;

/**
 *
 * @author Propietario
 */
public class BalanceDetalle {
    private String cuentaNumero;
    private String cuentaNombre;
    private double saldo_anterior;
    private double total_mov_debito;
    private double total_mov_credito;
    private double saldo_actual;

    public BalanceDetalle() {
    }

    public String getCuentaNumero() {
        return cuentaNumero;
    }

    public void setCuentaNumero(String cuentaNumero) {
        this.cuentaNumero = cuentaNumero;
    }

    public String getCuentaNombre() {
        return cuentaNombre;
    }

    public void setCuentaNombre(String cuentaNombre) {
        this.cuentaNombre = cuentaNombre;
    }

    public double getSaldo_anterior() {
        return saldo_anterior;
    }

    public void setSaldo_anterior(double saldo_anterior) {
        this.saldo_anterior = saldo_anterior;
    }

    public double getTotal_mov_debito() {
        return total_mov_debito;
    }

    public void setTotal_mov_debito(double total_mov_debito) {
        this.total_mov_debito = total_mov_debito;
    }

    public double getTotal_mov_credito() {
        return total_mov_credito;
    }

    public void setTotal_mov_credito(double total_mov_credito) {
        this.total_mov_credito = total_mov_credito;
    }

    public double getSaldo_actual() {
        return saldo_actual;
    }

    public void setSaldo_actual(double saldo_actual) {
        this.saldo_actual = saldo_actual;
    }
    
    
    
}
