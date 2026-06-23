/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ejercicio1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author aroa_
 */
public class Padre {
    public static void main(String[]args) throws IOException{
        try{
            //Proceso para leer el mensaje de la madre
            BufferedReader entrada= new BufferedReader(new InputStreamReader(System.in));

            String linea=entrada.readLine();
            System.out.println("Madre: "+ linea);
            
            //Acción del padre para hacer como que despierta al hijo
            System.out.println("(Despertando a Mario)");
            
            
            
            //Proceso para el hijo para mandarle el proceso y recibir la respuesta
            ProcessBuilder pb= new ProcessBuilder("java", "Hijo.java",linea);
            Process hijo = pb.start();

            BufferedReader br= new BufferedReader(new InputStreamReader(hijo.getInputStream()));
            linea=br.readLine();
            System.out.println("Dice Mario que "+ linea);
            
        }catch(IOException e){
                e.printStackTrace();
        }
    }
}
