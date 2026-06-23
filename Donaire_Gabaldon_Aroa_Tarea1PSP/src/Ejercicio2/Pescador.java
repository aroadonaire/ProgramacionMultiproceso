/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ejercicio2;

import java.util.Random;

/**
 *
 * @author aroa_
 */
public class Pescador implements Runnable{

    
    private Cesta cesta;
    private int cestaPescador=0;
    private Random random= new Random();
    
    public Pescador(Cesta cesta){
        this.cesta= cesta;
    }
    
    @Override
    public void run(){
        
        //Mientras la cesta del pescador tenga menos de 10 peces o pone peces en su cesta propia o en la de los gatos
        while(cestaPescador<10){
            try{
                int tiempo = random.nextInt(5)+1;
                Thread.sleep(tiempo*1000); //Numero aleatorio entre 1-5 para pescar
            }catch(InterruptedException e){
                
            }
            boolean pecesParaGatos = random.nextBoolean();//Aleatoriamente la pondrá en su cesta o en la de los gatos
            
            if(pecesParaGatos){
                try{
                    cesta.ponerPeces();
                }catch(InterruptedException e){}
            }else{
                cestaPescador++;
                System.out.println("Pescador guarda un pez en su cesta personal.Cesta pescador: "+ cestaPescador);
            }
        }
        System.out.println("Recogiendo la caña, ya no pescaré más");
        
        cesta.pescadorHaTerminado();
            
    }
    
}
