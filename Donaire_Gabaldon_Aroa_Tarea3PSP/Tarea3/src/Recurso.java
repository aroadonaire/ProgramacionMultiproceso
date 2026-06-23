/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author aroa_
 * He aprovechado el recurso que nos daban y he modificado algunos detalles, los demás html los he buscado en chatgpt
 */

public class Recurso {
    private static final ConcurrentHashMap<String, Integer> citas = new ConcurrentHashMap<>();
    private final int MAX_LINEAS = 4;
    
    private static final String[] PRUEBAS = {"Luces", "Frenos", "Emisiones", "Dirección", "Suspensión"};
    private static final String[] FRASES_OK = {"vale", "recibido", "entendido", "procedo", "hecho", "si", "correcto", "ok", "de acuerdo"};
    private static final String[] FRASES_CUNAO = {"ok jefe", "lo que tú digas", "a mandar", "perfecto máquina", "de lujo"};
    
    public synchronized static boolean reservar(String matricula){
        if (matricula == null || matricula.isEmpty()){
            return false;
        }
        
        if(!citas.containsKey(matricula)){
            citas.put(matricula, 0);
            return true;
        }
        return false;
    }
    
    public synchronized int entrarALinea(String matricula)throws InterruptedException{
        while(contarOcupadas()>=MAX_LINEAS){
            wait();
        }
        
        for(int i=1;i<=MAX_LINEAS;i++){
            if(!lineaOcupada(i)){
                citas.put(matricula, i);
                return i;
            }
        }
        return -1;
    }
    
    public synchronized void salirDeLinea(String matricula){
        citas.remove(matricula);
        notifyAll();
    }
    
    private int contarOcupadas(){
        int ocupadas=0;
        for(Integer val: citas.values()){
            if(val>0){
                ocupadas++;
            }
        }
        return ocupadas;
    }
    
    private boolean lineaOcupada(int n){
        return citas.containsValue(n);
    }
    
    public String realizarInspeccion(String matricula) throws InterruptedException{
        if(!citas.containsKey(matricula)){
            return "No tienes reserva.";
        }
        
        int numLinea= entrarALinea(matricula);
        StringBuilder sb= new StringBuilder();
        sb.append("<h3 style='color:white;'>").append(matricula).append(" en línea ").append(numLinea).append("</h3><br>");
        
        boolean aprobadoTotal = true;
        double prob = 0.6; 

        for (String prueba : PRUEBAS) {
            Thread.sleep((int) (Math.random() * 9000 + 1000));

            String respuesta;
            boolean esCunao = Math.random() > 0.7;
            if (esCunao) {
                respuesta = FRASES_CUNAO[(int) (Math.random() * FRASES_CUNAO.length)];
                prob = Math.max(0, prob - 0.1); 
            } else {
                respuesta = FRASES_OK[(int) (Math.random() * FRASES_OK.length)];
            }

            boolean pasa = Math.random() <= prob;
            if (!pasa) aprobadoTotal = false;

            String resultadoTexto = pasa ? "OK" : "NO OK";
            sb.append(prueba).append(": ").append(resultadoTexto)
              .append(" (\"").append(respuesta).append("\" - prob ")
              .append((int)(prob * 100)).append("%)<br>");
        }

        salirDeLinea(matricula);

        if (aprobadoTotal) {
            sb.append("<br><h2 style='color:#00ff00;'>ITV SUPERADA</h2>");
        } else {
            sb.append("<br><h2 style='color:#ff3333;'>ITV NO SUPERADA</h2>");
        }

        return sb.toString();
    }
    
    public String generarPanel() {
        StringBuilder sb = new StringBuilder();

        // --- Citas (texto simple arriba del panel)
        sb.append("<div style='color:white; font-size:18px;'>");
        sb.append("<strong>CITAS</strong><br>");
        sb.append("-----------------------------<br>");

        for (Map.Entry<String, Integer> entry : citas.entrySet()) {
            if (entry.getValue() == 0) {   // solo citas pendientes
                sb.append(entry.getKey())
                  .append("<br>");
            }
        }
        sb.append("<br><strong>LINEAS DE INSPECCIÃ“N</strong><br>")
            .append("-----------------------------<br><br>")
            .append("</div>");

        // --- Panel principal tipo LED
        for (int i = 1; i <= MAX_LINEAS; i++) {
            String matricula = "LIBRE";
            boolean libre = true;
            boolean encontrada = false; 

            for (Map.Entry<String, Integer> entry : citas.entrySet()) {

                if (!encontrada && entry.getValue() == i) {
                    matricula = entry.getKey();
                    libre = false;
                    encontrada = true; 
                }
            }

            String color = libre ? "verde" : "rojo";

            sb.append("""
                <div class="linea">
                    <div class="%s">%s</div>
                    <div class="%s">%d</div>
                </div>
            """.formatted(color, matricula, color, i));
        }

        return sb.toString();
    }
    
    
    
    public static String htmlIndex(String panelHTML) {
        return  "<html><head>"
            + "<title>ITV del Infierno</title>"
            + "<meta charset='UTF-8'>"
            + "<link rel='icon' href='data:,'>"
            + "<meta http-equiv='refresh' content='2'>"
            + "<style>"
            + "body {"
            + "  font-family: Arial, sans-serif;"
            + "  background: linear-gradient(135deg, #000000, #440000);"
            + "  color: white;"
            + "  text-align: center;"
            + "  padding-bottom: 40px;"
            + "}"
            + ".boton {"
            + "  background: #ff3333;"
            + "  color: white;"
            + "  padding: 20px 40px;"
            + "  font-size: 22px;"
            + "  margin: 20px;"
            + "  border: none;"
            + "  border-radius: 10px;"
            + "  cursor: pointer;"
            + "  transition: 0.3s;"
            + "}"
            + ".boton:hover { background:#cc0000; transform: scale(1.05); }"

            + ".panel {"
            + "  background: black;"
            + "  width: 60%;"
            + "  margin: auto;"
            + "  padding: 25px;"
            + "  border: 8px solid #333;"
            + "  color: #00ff00;"
            + "  font-family: 'Courier New', monospace;"
            + "  box-shadow: 0 0 15px #ff000055;"
            + "}"
            + ".panel-titulo {"
            + "  text-align: center;"
            + "  font-size: 30px;"
            + "  font-weight: bold;"
            + "  color: #ff3333;"
            + "  border-bottom: 4px solid #ff3333;"
            + "  padding-bottom: 12px;"
            + "  margin-bottom: 20px;"
            + "  letter-spacing: 3px;"
            + "}"
            + ".encabezado, .linea {"
            + "  display: grid;"
            + "  grid-template-columns: 70% 30%;"
            + "  font-size: 22px;"
            + "  letter-spacing: 2px;"
            + "  padding: 6px 0;"
            + "}"
            + ".linea { font-size: 24px; }"
            + ".verde { color: #00ff00; }"
            + ".rojo { color: #ff3333; }"
            + "</style>"
            + "</head><body>"

            + "<h1 style='color:#ff5555;'>ITV del Infierno</h1>"

            + "<form action='/reservar' method='GET'>"
            + "<button class='boton'>Reservar Cita</button>"
            + "</form>"

            + "<form action='/pasar' method='GET'>"
            + "<button class='boton'>Pasar ITV</button>"
            + "</form>"

            + "<h2 style='color:#ff8888;'>Panel de Llamadas</h2>"

            + "<div class='panel'>"
            + "<div class='panel-titulo'>LÃNEAS DE INSPECCIÃ“N</div>"
            

            + panelHTML

            + "</div></body></html>";
    }


    public static String htmlReservar = "<html><head><meta charset='UTF-8'><title>Reservar Cita</title>"
            + "<link rel='icon' href='data:,'>"
            + "<style>"
            + "body { background: linear-gradient(135deg,#550000,#220000); "
            + "       color:white; font-family:Arial; text-align:center; padding-top:40px; }"
            + "input { padding:10px; font-size:18px; border-radius:8px; border:none; }"
            + "button { background:#ff4444; color:white; padding:12px 25px; "
            + "        border:none; border-radius:8px; font-size:18px; cursor:pointer; }"
            + "button:hover { background:#cc0000; }"
            + "a { color:#ffaaaa; font-size:20px; }"
            + "</style></head>"
            + "<body>"
            + "<h1>Reservar Cita ITV</h1>"
            + "<form action='/reservar' method='POST'>"
            + "MatrÃ­cula: <input type='text' name='matricula' required>"
            + "<br><br>"
            + "<button type='submit'>Confirmar</button>"
            + "</form>"
            + "<br><a href='/'>Volver</a>"
            + "</body></html>";



    public static String htmlResultado(String contenido) {
        return "<div style='width:100%; text-align:center;'>"
            + "<h1 style='font-size:28px; margin-bottom:20px; color:#ffdddd;'>Resultado de la InspecciÃ³n</h1>"
            + "<div class='box' style='background:#550000; padding:15px; width:45%; margin:auto; border-radius:10px; box-shadow:0 0 10px #00000088; font-size:20px; color:white;'>"
            + contenido
            + "</div>"
            + "<div class='taller' style='margin-top:20px; background:#ffeecc; color:#442200; padding:12px; border-radius:10px; width:40%; margin-left:auto; margin-right:auto; box-shadow:0 0 8px #ffcc66; border:2px solid #ffbb55; font-family:\"Comic Sans MS\";'>"
            + "<h3 style='color:#aa5500; margin-bottom:4px; font-size:13px;'>ðŸ”§ Taller \"EL Oportuno\" ðŸ”§</h3>"
            + "<p style='font-size:10px;'>Â¿No has pasado la ITV? Vaya, quÃ© lÃ¡stimaâ€¦</p>"
            + "<p style='font-size:10px;'><b>Estamos justo al lado</b>, pura coincidencia</p>"
            + "<p style='font-size:10px;'><i>5% descuento con el cÃ³digo: <span style='font-size:3px;'>Deja de hacer zoom, y ponte con la tarea. Vas a suspender</span></i></p>"
            + "<small style='font-size:10px;'>(Si usa el cÃ³digo, se lo arreglarÃ¡ el becario)</small>"
            + "</div>"
            + "</div>";
    }


    public static String htmlPasarITV(String matriculaPrellenada, String resultadoFragmento) {
        String valor = (matriculaPrellenada != null) ? matriculaPrellenada : "";

        return "<html><head><meta charset='UTF-8'><title>Pasar ITV</title>"
            + "<link rel='icon' href='data:,'>"
            + "<style>"
            + "body { background: linear-gradient(135deg, #000000, #440000); color:white; font-family:Arial; text-align:center; padding-top:40px; }"
            + "h1 { color:#ff4444; text-shadow:0 0 10px #ff0000; }"
            + "input { padding:15px; font-size:24px; border-radius:10px; border:3px solid #ff0000; background:#330000; color:white; width:280px; text-align:center; }"
            + "button { background:#ff3333; color:white; padding:15px 35px; border:none; border-radius:10px; font-size:22px; cursor:pointer; transition:0.3s; }"
            + "button:hover { background:#cc0000; transform:scale(1.05); }"
            + "a { color:#ffaaaa; font-size:22px; text-decoration:none; }"
            + "a:hover { text-decoration:underline; }"
            + "</style></head><body>"

            + "<h1>Entrada a la ITV del Infierno</h1>"
            + "<form action='/pasar' method='POST'>"
            + "MatrÃ­cula:<br><br>"
            + "<input type='text' name='matricula' required value='" + valor + "'>"
            + "<br><br>"
            + "<button type='submit'>Entrar a lÃ­nea</button>"
            + "</form>"

            + "<div class='resultado'>" + resultadoFragmento + "</div>"

            + "<br><a href='/'>Volver</a>"
            + "</body></html>";
    }

    public static final String html_notFound =
        "<html><head><title>Error 404</title><meta charset=UTF-8>"
        + "<link rel=icon href=data:,/>"
        + "<style>"
        + "body{font-family:Arial;background:linear-gradient(135deg,#ff9a9e,#fad0c4);"
        + "text-align:center;padding-top:60px;}"
        + "h1{color:#333;font-size:48px;margin-bottom:10px;}"
        + "p{font-size:20px;color:#555;}"
        + "a.button{display:inline-block;padding:12px 25px;background:#e74c3c;color:white;"
        + "text-decoration:none;border-radius:8px;font-size:18px;transition:0.3s;margin-top:20px;}"
        + "a.button:hover{background:#c0392b;}"
        + "</style></head>"
        + "<body>"
        + "<h1>Error 404</h1>"
        + "<p>La pÃ¡gina que buscas no existe o no se encuentra disponible.</p>"
        + "<a class='button' href='/'>Volver al inicio</a>"
        + "</body></html>";
    
}
