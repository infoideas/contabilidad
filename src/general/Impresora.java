/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package general;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
 
 
/********************************************************************
*	La siguiente clase llamada "Impresora", es la encargada de  	*
*	establecer la fuente con que se va a imprimir, de obtener el	*
*	trabajo de impresion, la página. En esta clase hay un método	*
*	llamado imprimir, el cual recibe una cadena y la imprime.		*
********************************************************************/
public final class Impresora
{
        Font fuente = new Font("Dialog", Font.PLAIN, 10);
	PrintJob pj;
	Graphics pagina;
 
 
	/********************************************************************
	*	A continuación el constructor de la clase. Aquí lo único que	*
	*	hago es tomar un objeto de impresion.							*
	********************************************************************/
	public Impresora()
	{
            configurar();
	}

        
	/********************************************************************
	*	A continuación el método "imprimir(String)", el encargado de 	*
	*	colocar en el objeto gráfico la cadena que se le pasa como 		*
	*	parámetro y se imprime.											*
	********************************************************************/
        
        public void configurar(){
		try
		{
                    JobAttributes ja = new JobAttributes();
                    ja.setDialog(JobAttributes.DialogType.NONE);
                    PageAttributes pa = new PageAttributes();
                    
                    pj = Toolkit.getDefaultToolkit().getPrintJob(new Frame(), "SCAT", ja,pa);
   		    pagina = pj.getGraphics();
		    pagina.setFont(fuente);
		    pagina.setColor(Color.black);
                    
		}catch(Exception e)
		{
                    System.out.println("Error al configurar impresora: " + e.getMessage());
		}
            
        }
        
        public void imprimirLinea(String lineaTexto,int linea)
	{
		//LO COLOCO EN UN try/catch PORQUE PUEDEN CANCELAR LA IMPRESION
                lineaTexto=getLeftString(lineaTexto,28);
		try
		{
                    pagina.drawString(lineaTexto,20,linea);
		}catch(Exception e)
		{
                    System.out.println("Error al imprimir línea de texto " + e.getMessage());
		}
	}
    
    public void cerrarImpresion(){
        pagina.dispose();
	pj.end();
    }
    
    public static String getLeftString(String st,int length){
       int stringlength=st.length();
       if(stringlength<=length){
           return st;
       }
       return st.substring((stringlength-length));
}
 
 
}
 