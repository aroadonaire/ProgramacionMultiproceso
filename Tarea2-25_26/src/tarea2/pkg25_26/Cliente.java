/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarea2.pkg25_26;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

/**
 *
 * @author LuisRosillo <>
 */



public class Cliente implements Runnable {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;
    private static final Object contador = new Object();
    public static int itvSuperadas = 0;
    public static int itvNoSuperadas = 0;

    private static final String[] frasesPermitidas = {
        "vale", "recibido", "entendido", "procedo", "hecho",
        "si", "correcto", "ok", "de acuerdo"
    };

    private static final String[] frasesProhibidas = {
        "ok jefe", "lo que tú digas", "a mandar", "como usted mande",
        "vamos al lío", "marchando", "manda usted", "perfecto máquina",
        "de lujo"
    };

    private static final Random random = new Random();

    private static String fraseAleatoria() {
        if (Math.random() < 0.7) {
            return frasesPermitidas[random.nextInt(frasesPermitidas.length)];
        } else {
            return frasesProhibidas[random.nextInt(frasesProhibidas.length)];
        }
    }
    

    @Override
    public void run() {
        try (
            Socket s = new Socket(HOST, PUERTO);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);){

            String linea;
            String respuesta;
            br.readLine();
            while (!(linea = br.readLine()).equals("Le retiro el walki, esperé en la puerta. Gracias.")) {
                respuesta = fraseAleatoria();
                pw.println(respuesta);
                //System.out.println(Thread.currentThread().getName() + "-> Inspector: " + linea + "Cliente: " + respuesta);
            }
            linea = br.readLine();
            synchronized(contador){
                if(linea.equals("Tome su pegatina")){
                    itvSuperadas++;
                }else{
                    itvNoSuperadas++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
