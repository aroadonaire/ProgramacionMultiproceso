/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tarea2.pkg25_26;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author LuisRosillo <>
 */


public class Servidor {

    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        System.out.println("Servidor ITV del Infierno arrancando...");
        int contador = 1;
        Itv itv = new Itv();

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = servidor.accept();
                HiloServidor hilo = new HiloServidor(socketCliente, itv);
                new Thread(hilo, "Coche"+(contador++)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
