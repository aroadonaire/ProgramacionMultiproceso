/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarea2.pkg25_26;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author LuisRosillo <>
 */



public class HiloServidor implements Runnable {

    private Socket socket;
    private Itv itv;

    private final String[] pruebas = {
        "Luces", "Frenos", "Emisiones", "Dirección", "Suspensión"
    };

    private final String[] frasesProhibidas = {
        "ok jefe", "lo que tú digas", "a mandar", "como usted mande",
        "vamos al lío", "marchando", "manda usted", "perfecto máquina",
        "de lujo"
    };

    public HiloServidor(Socket socket, Itv itv) {
        this.socket = socket;
        this.itv = itv;
    }

    @Override
    public void run() {
        try (Socket s = this.socket;
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);){

            itv.intentarEntrar();
            System.out.println(Thread.currentThread().getName() + " entra en la ITV.");

            

            int probabilidad = 60;
            Map<String, String> resultados = new LinkedHashMap<>();

            pw.println("Buenas tardes, le dejo aquí el walkitalkie para darle órdenes");
            //entrada.readLine();

            Random rand = new Random();

            for (String prueba : pruebas) {

                // Espera aleatoria entre 1 y 5 segundos
                int tiempoEspera = 1000 + rand.nextInt(4000);
                Thread.sleep(tiempoEspera);

                pw.println("Realice la prueba: " + prueba);
                String respuesta = br.readLine();

                for (String frase : frasesProhibidas) {
                    if (frase.equalsIgnoreCase(respuesta)) {
                        probabilidad -= 10;
                    }
                }

                boolean ok = nuevaProbabilidad(probabilidad);
                resultados.put(prueba, (ok ? "Si" : "No") + " (\"" + respuesta + "\" - prob " + probabilidad + "%)");
            }

            
            
            pw.println("Le retiro el walki, esperé en la puerta. Gracias.");
            
            // Mostrar resultado sincronizado
            if(itv.mostrarResultadoCoche(resultados)){
                pw.println("Tome su pegatina");
            }else{
                pw.println("Debe volver de nuevo");
            }

            
            itv.salir();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean nuevaProbabilidad(int prob) {
        return Math.random() * 100 < prob;
    }
}
