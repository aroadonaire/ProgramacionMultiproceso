/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ejercicio2;

/**
 *
 * @author aroa_
 */
public class Cesta {
    
    private int numMaxPeces;
    private int peces=0;
    private boolean pescadorTermina= false;
    
    
    public Cesta(int numMaxPeces){
        this.numMaxPeces= numMaxPeces;
    }
    
    //El pescador coloca peces en la cesta
    public synchronized void ponerPeces() throws InterruptedException{
        while(peces==numMaxPeces){
            wait();//Está completo y esperamos
        }
        peces++;
        System.out.println("Pescador ha puesto un pez en la cesta de los gatos");
        
        notifyAll(); //Avisamos a los gatos
    }
    
    //Los gatos cogen peces de la cesta
    public synchronized boolean cogerPeces(String gato) throws InterruptedException{
        while(peces==0){
            if(pescadorTermina){
                return false;
            }
            wait();//Esperamos a tener peces
        }
        peces--;
        System.out.println(gato + " ha cogido un pez. Peces restantes: "+ peces);
        
        notifyAll();//Avisamos al pescador y a los demás gatos
        
        return true;
    }
    
    //Cuando el pescador ha acabado
    public synchronized void pescadorHaTerminado(){
        pescadorTermina= true;
        notifyAll();
    }
    
    //Si la cesta está vacía
    public synchronized boolean cestaVacia(){
        return peces==0;
    }
}
