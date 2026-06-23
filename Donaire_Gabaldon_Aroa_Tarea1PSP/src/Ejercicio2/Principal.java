/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ejercicio2;

/**
 *
 * @author aroa_
 */
public class Principal {
    
    public static void main(String[]args){
        Cesta cesta = new Cesta(2);
        
        //Creamos los hilos del pescador como de los 3 gatos
        Thread hiloPescador= new Thread(new Pescador(cesta), "Pescador");
        Thread hiloGato1= new Thread(new Gatos(cesta), "Gato 1");
        Thread hiloGato2= new Thread(new Gatos(cesta), "Gato 2");
        Thread hiloGato3= new Thread(new Gatos(cesta), "Gato 3");
        
        hiloPescador.start();
        hiloGato1.start();
        hiloGato2.start();
        hiloGato3.start();
        
        try{
            hiloPescador.join();
            hiloGato1.join();
            hiloGato2.join();
            hiloGato3.join();
        }catch(InterruptedException e){
            
        }
        System.out.println("Otro día de pesca finalizada");
    }
}
