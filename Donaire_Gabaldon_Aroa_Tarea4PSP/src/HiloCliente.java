import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author aroa_
 */
public class HiloCliente extends Thread{
    private SSLSocket socket;
    private Recurso recurso;
    
    public HiloCliente(SSLSocket socket, Recurso recurso){
        this.socket=socket;
        this.recurso=recurso;
    }
    
    public void run(){
        try(
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)
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
                    responder(salida, Recurso.login(""));
                }else if(ruta.equals("/inicio")){
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
                
                if (ruta.startsWith("/inicio")) {

                    String email = parametros.get("email");
                    String password = parametros.get("password");

                     try {

                        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                            log("ERROR login email inválido: " + email);
                            throw new Exception("Email inválido");
                        }

                        if (password == null || !password.matches("^[A-Za-z0-9]{6,}$")) {
                            log("ERROR login password inválida: " + password);
                            throw new Exception("Password inválida");
                        }

                        if (recurso.comprobarLogin(email, password)) {
                            log("Login correcto: " + email);
                            redireccionar(salida, "/inicio");
                            return;
                        } else {
                            log("ERROR login incorrecto: " + email);
                            responder(salida, Recurso.login("Credenciales incorrectas"));
                        }
                    } catch (Exception e) {
                        responder(salida, Recurso.login(e.getMessage()));
                    }
                }else if (ruta.startsWith("/registro")) {

                    String email = parametros.get("email");
                    String password = parametros.get("password");

                    try {

                        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                            log("ERROR registro email inválido: " + email);
                            throw new Exception("Email inválido");
                        }

                        if (password == null || !password.matches("^[A-Za-z0-9]{6,}$")) {
                            log("ERROR registro password inválida: " + password);
                            throw new Exception("Password inválida");
                        }

                        String mensajeResultado= recurso.registrarUsuario(email, password);
                        
                        if(mensajeResultado.contains("correcto")){
                            log("Registro correcto: " + email);
                        }
                        responder(salida, Recurso.login(mensajeResultado));

                    } catch (Exception e) {
                        responder(salida, Recurso.login(e.getMessage()));
                    }
                }else if(ruta.startsWith("/reservar")){
                    String matricula= parametros.get("matricula");
                    
                    boolean reservada= Recurso.reservar(matricula);
                    
                    String mensaje= reservada ? "Cita reservada correctamente":"La matricula ya tenia cita";
                    
                    String panel = recurso.generarPanel();
                    responder(salida, mensaje+Recurso.htmlIndex(panel));
                }else if(ruta.startsWith("/pasar")){
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
        salida.println("Content-Type: text/html; charset=UTF-8");
        salida.println();
        salida.println(contenido);
    }
    
    private void redireccionar(PrintWriter salida, String nuevaRuta) {
        salida.println("HTTP/1.1 302 Found");
        salida.println("Location: " + nuevaRuta);
        salida.println();
        salida.flush();
    }
    
    private Map<String, String> procesarDatos(String datos){
        Map<String, String> mapa = new HashMap<>();
        if (datos == null || datos.isEmpty()) return mapa;

        String[] pares = datos.split("&");
        for (String par : pares) {
            String[] claveValor = par.split("=");
            if (claveValor.length == 2) {
                String clave = URLDecoder.decode(claveValor[0], StandardCharsets.UTF_8);
                String valor = URLDecoder.decode(claveValor[1], StandardCharsets.UTF_8);

                mapa.put(clave, valor);
            }
        }
        return mapa;
    }
    
    private void log(String mensaje) {
        try (FileWriter fw = new FileWriter("log.txt", true)) {
            fw.write(LocalDateTime.now() + " - " + mensaje + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
