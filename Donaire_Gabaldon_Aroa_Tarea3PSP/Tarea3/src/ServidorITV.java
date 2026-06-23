
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author aroa_
 */
public class ServidorITV {
    public static void main(String[] args) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(12349)){
            System.out.println("Servidor ITV iniciado");
            Recurso recurso = new Recurso();
            
            while(true){
                Socket socket= serverSocket.accept();
                new Thread(new HiloCliente(socket, recurso)).start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
