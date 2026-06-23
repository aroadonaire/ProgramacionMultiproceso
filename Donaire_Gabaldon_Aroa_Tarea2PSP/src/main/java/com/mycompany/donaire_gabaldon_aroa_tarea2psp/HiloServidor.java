/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.donaire_gabaldon_aroa_tarea2psp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aroa_
 */
public class HiloServidor implements Runnable {
    private final Socket s;
    private RecursoCompartido rc;
    private double probabilidad = 0.6;
    private String cocheNombre;
    
    private static final String[] PRUEBAS ={
        "Luces", "Frenos", "Emisiones", "Dirección", "Suspensión"
    };
    
    private static final String[] FRASES_CUNAO={
        "ok jefe", "lo que tú digas", "a mandar", "como usted mande", "vamos al lío",
        "marchando", "manda usted", "perfecto máquina", "de lujo"
    };
    
    public HiloServidor(Socket socket, RecursoCompartido rc, String cocheNombre){
        this.s= socket;
        this.rc= rc;
        this.cocheNombre=cocheNombre;
    }
    
    @Override
    public void run(){
        try{
            BufferedReader br= new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
            
            System.out.println(cocheNombre+" esperando línea ITV...");
            rc.entrarLinea();
            System.out.println(cocheNombre+ " entra en la ITV");
            
            pw.println("Buenas tardes, le dejo aquí el walkie para darle órdenes");
            
            boolean aprobado = true;
            StringBuilder resultado = new StringBuilder();
            int pruebaNum=1;
            
            for(String prueba : PRUEBAS){
                pw.println("Realice la prueba: "+ prueba);
                String respuesta = br.readLine();
                
                boolean esCunao= esCunao(respuesta);
                if(esCunao(respuesta)){
                    probabilidad =Math.max(0, probabilidad -0.1);
                }
                boolean paso= Math.random()<=probabilidad;
                resultado.append(prueba).append(": ").append(paso ? "si":"no")
                        .append(" (\"").append(respuesta).append("\" prob ")
                        .append((int)(probabilidad*100)).append("%)\n");
                
                if(!paso){
                    aprobado= false;
                    
                }
                
                Thread.sleep((int)(Math.random()*4000+1000));
                pruebaNum++;
                
            }
            
            pw.println("Le retiro el walkie, espere en la puerta. Gracias.");
            Thread.sleep(1000);
            
            if(aprobado){
                pw.println("Tome su pegatina");
                System.out.println(cocheNombre+ " ITV superada.");
            }else{
                pw.println("Debe volver de nuevo");
                System.out.println(cocheNombre+ " ITV no superada.");
            }
            System.out.println("Resultado de "+ cocheNombre+" es: "+ resultado.toString());
            
            br.close();
            pw.close();
            s.close();
            
        }catch (SocketException e){
            System.err.println("Error de socket: "+ e.getMessage());
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }catch(IOException ex){
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            rc.salirLinea();
        }
    }
    private boolean esCunao(String frase){
        return Arrays.asList(FRASES_CUNAO).contains(frase);
    }
    
}
