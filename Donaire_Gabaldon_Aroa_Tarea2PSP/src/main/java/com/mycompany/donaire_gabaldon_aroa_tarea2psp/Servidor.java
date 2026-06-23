/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.donaire_gabaldon_aroa_tarea2psp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author aroa_
 */
public class Servidor {
    private static int contadorCoches =0;

    public static void main(String[] args) throws IOException {
        RecursoCompartido rc = new RecursoCompartido();
        
        ServerSocket ss = new ServerSocket(12349);
        System.out.println("Servidor ITV del infierno arrancando...");
        
        while(true){
            Socket clientSocket = ss.accept();
            contadorCoches++;
            String cocheNombre = "Coche "+ contadorCoches;
            System.out.println(cocheNombre+ " entra en la ITV");
            HiloServidor hilo = new HiloServidor(clientSocket, rc, cocheNombre);
            new Thread(hilo).start();
        }
    }
}
