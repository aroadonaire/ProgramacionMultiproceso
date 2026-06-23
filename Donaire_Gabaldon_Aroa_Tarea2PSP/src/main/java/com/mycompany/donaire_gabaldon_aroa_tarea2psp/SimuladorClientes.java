/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.donaire_gabaldon_aroa_tarea2psp;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author aroa_
 */
public class SimuladorClientes {
    public static void main(String[] args) throws InterruptedException{
        Scanner scanner = new Scanner(System.in);
        List<Thread> lista = new ArrayList<>();
        List<Cliente> clientes = new ArrayList<>();
        int opcion=0;
        
        while(opcion!=2){
            System.out.println("\nMenu:");
            System.out.println("1. Simular peticiones clientes");
            System.out.println("2. Terminar programa");
            System.out.println("Selecciona una opcion: ");
            
            opcion = scanner.nextInt();
            if(opcion ==1){
                System.out.print("¿Cuántos clientes desea simular?: ");
                int numCliente = scanner.nextInt();
                clientes.clear();
                lista.clear();
                
                for(int i=0; i<numCliente;i++){
                    Cliente c= new Cliente();
                    Thread t= new Thread(c);
                    clientes.add(c);
                    lista.add(t);
                    t.start();
                }
                
                for(Thread hilo : lista){
                hilo.join();
                }
                
                int aprobados=0;
                int suspensos=0;
                for(Cliente c: clientes){
                    if(c.isAprobado()) aprobados++;
                    else suspensos++;
                }
                System.out.println("ITV superadas: "+ aprobados);
                System.out.println("ITV no superadas: "+suspensos);
            
            }else if(opcion ==2){
                System.out.println("Programa terminado");
            }else{
                System.out.println("Opción no válida");
            }
        }
    }
    
}
