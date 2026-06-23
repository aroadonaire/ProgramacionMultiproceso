/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.donaire_gabaldon_aroa_tarea2psp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author aroa_
 */
public class Cliente implements Runnable{
    private boolean aprobado;
    
    public boolean isAprobado(){
        return aprobado;
    }
    
    private static final String[] FRASES_OK ={
        "vale", "recibido", "entendido", "procedo", "hecho", "si", "correcto", "ok", "de acuerdo"
    };
    
    private static final String[] FRASES_CUNAO= {
        "ok jefe", "lo que tú digas", "a mandar", "como usted mande", "vamos al lío",
        "marchando", "manda usted", "perfecto máquina", "de lujo"
    };
    
    @Override
    public void run(){
        try{
            Socket s= new Socket("localhost", 12349);
            System.out.println("Cliente conectado al servidor: "+ Thread.currentThread().getName());
            
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
            
            String mensaje;
            
            while((mensaje = br.readLine())!= null){
                System.out.println("Mensaje recibido del servidor: " + mensaje);
                if(mensaje.contains("Realice la prueba")){
                    String r = respuestaAleatoria();
                    System.out.println("Cliente responde: " + r);
                    pw.println(r);
                }else if(mensaje.contains("Tome su pegatina")){
                    aprobado=true;
                    break;
                }else if(mensaje.contains("Debe volver")){
                    aprobado=false;
                    break;
                }
            }
            br.close();
            pw.close();
            s.close();
            
        }catch(Exception e){
            e.printStackTrace();

        }
    }
    
    private String respuestaAleatoria(){
        if(Math.random()<0.7){
            return FRASES_OK[(int)(Math.random()*FRASES_OK.length)];
        }else{
            return FRASES_CUNAO[(int)(Math.random()*FRASES_CUNAO.length)];
        }
    }
    
}
