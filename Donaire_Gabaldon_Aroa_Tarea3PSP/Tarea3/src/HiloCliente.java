
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author aroa_
 */
public class HiloCliente extends Thread{
    private Socket socket;
    private Recurso recurso;
    
    public HiloCliente(Socket socket, Recurso recurso){
        this.socket=socket;
        this.recurso=recurso;
    }
    
    public void run(){
        try(
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida= new PrintWriter(socket.getOutputStream(), true)
        ){
            String primeraLinea= entrada.readLine();
            
            if(primeraLinea==null || primeraLinea.isEmpty())return;
            
            String[] partes = primeraLinea.split(" ");
            if (partes.length < 2) return;

            String metodo = partes[0];
            String ruta = partes[1];
            
            System.out.println("Peticion recibida: " + metodo + " " + ruta);
            
            if(metodo.equals("GET")){
                if(ruta.equals("/")){
                    String panel = recurso.generarPanel();
                    responder(salida, Recurso.htmlIndex(panel));
                }else if(ruta.startsWith("/reservar")){
                    responder(salida, Recurso.htmlReservar);
                }else if(ruta.startsWith("/pasar")){
                    responder(salida, Recurso.htmlPasarITV("", ""));
                }else{
                    responder(salida, Recurso.html_notFound);
                }
            }
            else if(metodo.equals("POST")){
                int contentLength=0;
                String linea;
                
                while((linea = entrada.readLine()) != null && !linea.isEmpty()){
                    if(linea.toLowerCase().startsWith("content-length:")){
                        contentLength= Integer.parseInt(linea.split(": ")[1].trim());
                    }
                }
                
                StringBuilder cuerpoSb = new StringBuilder();
                for (int i = 0; i < contentLength; i++) {
                    cuerpoSb.append((char) entrada.read());
                }
                String datos = cuerpoSb.toString();
                Map<String, String> parametros= procesarDatos(datos);
                
                if(ruta.startsWith("/reservar")){
                    String matricula= parametros.get("matricula");
                    
                    boolean reservada= Recurso.reservar(matricula);
                    
                    String mensaje= reservada ? "Cita reservada correctamente":"La matricula ya tenia cita";
                    
                    String panel = recurso.generarPanel();
                    responder(salida, mensaje+Recurso.htmlIndex(panel));
                } else if(ruta.startsWith("/pasar")){
                    String matricula= parametros.get("matricula");
                    
                    String resultado= recurso.realizarInspeccion(matricula);
                    
                    responder(salida, Recurso.htmlPasarITV(matricula, resultado));
                }else{
                    responder(salida, Recurso.html_notFound);
                }
            }
            
        }catch (Exception e) {
            System.err.println("Error procesando cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }       
    }
    
    private void responder(PrintWriter salida, String contenido){
        salida.println("HTTP/1.1 200 OK");
        salida.println("Content-Type: text/html");
        salida.println();
        salida.println(contenido);
    }
    
    private Map<String, String> procesarDatos(String datos){
        Map<String, String> mapa = new HashMap<>();
        if (datos == null || datos.isEmpty()) return mapa;

        String[] pares = datos.split("&");
        for (String par : pares) {
            String[] claveValor = par.split("=");
            if (claveValor.length == 2) {
                String valor = claveValor[1].replace("+", " ");
                mapa.put(claveValor[0], valor);
            } else if (claveValor.length == 1) {
                mapa.put(claveValor[0], ""); 
            }
        }
        return mapa;
    }
    
}
