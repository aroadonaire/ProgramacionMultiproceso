import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;


/**
 *
 * @author aroa_
 */

public class ServidorITV {
    private static final Logger logger= configurarLogger();
    
    public static void main(String[] args){
        try{
            SSLServerSocket serverSocket= crearServidorSSL();
            logger.info("Servidor SSL escuchando en puerto 12349");
            
            Recurso recurso = new Recurso();
            
            while(true){
                try{
                    SSLSocket socketSsl= (SSLSocket) serverSocket.accept();
                    System.out.println("Cliente conectado");
                    
                    new Thread(new HiloCliente(socketSsl, recurso)).start();
                }catch (IOException e){
                    logger.warning("Error aceptando cliente: "+ e.getMessage());
                }
            }
        }catch(RuntimeException e){
            logger.severe("No se pudo iniciar el servidor SSL: "+ e.getMessage());
        }
    }
    
    private static SSLServerSocket crearServidorSSL(){
        try{
            KeyStore keyStore= KeyStore.getInstance("JKS");
            
            try(FileInputStream keyFile= new FileInputStream("AlmacenSSL")){
                keyStore.load(keyFile,"123456".toCharArray());
            }
            KeyManagerFactory kmf= KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, "123456".toCharArray());
            
            SSLContext sslContext= SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);
            
            SSLServerSocketFactory factory= sslContext.getServerSocketFactory();
            return(SSLServerSocket) factory.createServerSocket(12349);
        }catch (KeyStoreException e){
            throw new RuntimeException("Error con el keyStore(formato o tipo incorrecto)", e);
        }catch(NoSuchAlgorithmException e){
            throw new RuntimeException("Algoritmo SSL no soportado", e);
        }catch(CertificateException e){
            throw new RuntimeException("Error en el certificado del almacen", e);
        }catch(UnrecoverableKeyException e){
            throw new RuntimeException("No se puede acceder a la clave privada", e);
        }catch(KeyManagementException e){
            throw new RuntimeException("Error inicializando contexto SSL", e);
        }catch(IOException e){
            throw new RuntimeException("Error de lectura del KeyStore o de red", e);
        }
    }
    
    private static Logger configurarLogger(){
        Logger logger= Logger.getLogger("MiLog");
        
        try{
            FileHandler fh= new FileHandler("ServidorSSL.log", true);
            fh.setFormatter(new Formatter(){
                @Override
                public String format(LogRecord record){
                    String fechaHora= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    return String.format("(%s)[%s] %s%n",
                            fechaHora,
                            record.getLevel(),
                            record.getMessage());
                }
            });
            logger.addHandler(fh);
            logger.setUseParentHandlers(true);
            logger.setLevel(Level.ALL);
        }catch(IOException |SecurityException e){
            System.err.println("No se pudo configurar el logger");
        }
        return logger;
        
    }
}
