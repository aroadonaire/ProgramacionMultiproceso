/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.donaire_gabaldon_aroa_tarea2psp;

import java.util.concurrent.Semaphore;

/**
 *
 * @author aroa_
 */
public class RecursoCompartido {
    private Semaphore lineas = new Semaphore(4);
    
    public void entrarLinea() throws InterruptedException{
        lineas.acquire();
    }
    
    public void salirLinea(){
        lineas.release();
    }
    
}
