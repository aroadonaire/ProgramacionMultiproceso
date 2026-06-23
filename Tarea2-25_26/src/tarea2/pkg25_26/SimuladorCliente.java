package tarea2.pkg25_26;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author LuisRosillo
 */
public class SimuladorCliente {

    public static void main(String[] args) throws InterruptedException {

        int coches;
        int cochesTotal = 0;
        List<Thread> cochesITV  = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        do{
            System.out.print("¿Cuantos coches llegan? ");
            coches = scanner.nextInt();


            for (int i = cochesTotal; i < (cochesTotal + coches); i++) {
                cochesITV.add(new Thread(new Cliente(),"Coche"+(i+1)));
                cochesITV.get(i).start();
            }
            
            cochesTotal+= coches;
        }while(coches!=0);
        
        for(Thread coche : cochesITV){
            coche.join();
        }
        
        System.out.println("Itv's superadas: " + Cliente.itvSuperadas);
        System.out.println("Itv's no superadas: " + Cliente.itvNoSuperadas);

    }
}

