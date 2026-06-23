/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ejercicio2;

/**
 *
 * @author aroa_
 */
public class Gatos implements Runnable {
 
    private Cesta cesta;
    private int pecesComidos=0;
    
    public Gatos(Cesta cesta){
        this.cesta= cesta;
    }
    
    @Override
    public void run() {
        
        //Obtenemos el nombre de cada gato para saber que hace cada uno
        String gato = Thread.currentThread().getName();
        
        //Cada gato consigue peces cada vez que pueda
        while(true){
            try{
                boolean consigue= cesta.cogerPeces(gato);

                if(!consigue){
                    System.out.println(gato+ " Ya no quedan peces");
                    return;
                }
                pecesComidos++;
            }catch(InterruptedException e){}
            
            //Cada gato tarda en comerse el pez 25s
            try{
                Thread.sleep(25000); 
            }catch(InterruptedException e){
                
            }
            System.out.println(gato+ " Miau! "+gato+" Peces comidos: "+ pecesComidos);
        }
        
    }
    
}
