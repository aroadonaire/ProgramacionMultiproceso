/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarea2.pkg25_26;

import java.util.Map;

/**
 *
 * @author LuisRosillo <>
 */
public class Itv {
        
    private final int MAX_LINEAS = 4;
    private int cochesDentro = 0;
    private int contadorCoches = 1;

    // Intentar entrar a la ITV
    public synchronized int intentarEntrar() throws InterruptedException {
        while (cochesDentro >= MAX_LINEAS) {
            System.out.println(Thread.currentThread().getName() + " esperando para entrar.");
            wait();
        }
        cochesDentro++;
        return contadorCoches++;
    }

    public synchronized void salir() {
        cochesDentro--;
        notifyAll();
    }

    // Mostrar resultado de forma sincronizada
    public synchronized boolean mostrarResultadoCoche(Map<String, String> resultados) {
        boolean aprobada = true;
        for (String res : resultados.values()) {
            if (!res.startsWith("Si")) {
                aprobada = false;
                break;
            }
        }

        System.out.println("\n--- Resultado " + Thread.currentThread().getName() + " ---");
        System.out.println(Thread.currentThread().getName() + " ITV " + (aprobada ? "SUPERADA" : "NO SUPERADA") + ".");
        for (Map.Entry<String, String> entry : resultados.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("--------------------------------------\n");
        return aprobada;
    }
}
