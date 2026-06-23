# 🚀 Portafolio Avanzado: Programación de Servicios y Procesos (PSP)

Este repositorio reúne el ecosistema de proyectos y soluciones prácticas desarrolladas a lo largo de la asignatura de **Programación de Servicios y Procesos**. Las soluciones abarcan desde la comunicación básica interproceso a nivel de Sistema Operativo hasta el diseño de arquitecturas cliente-servidor concurrentes, seguras y criptográficas en Java.

---

## 📁 Estructura del Repositorio y Resumen de Proyectos

El proyecto está estructurado de forma modular en cuatro directorios independientes, representando una evolución arquitectónica constante:

### 1. 🔄 Módulo 01: Comunicación Jerárquica entre Procesos (IPC)
* **Conceptos clave:** `ProcessBuilder`, Pipes del Sistema (`|`), Redirección de I/O Streams, Sincronización Síncrona.
* **Resumen:** Simulación de un flujo de comunicación lineal familiar (Madre ➡️ Padre ➡️ Hijo). El proceso `Madre` emite una instrucción por la salida estándar, capturada mediante un Pipe (`|`) de la terminal por el proceso `Padre`. Este último orquesta el ciclo de vida de un subproceso dinámico (`Hijo`) a través de `ProcessBuilder`, transmitiéndole el flujo de datos y renderizando la respuesta final de forma coordinada.

### 2. 🧵 Módulo 02: Arquitectura Concurrente Multihilo (Productor-Consumidor)
* **Conceptos clave:** Monitores (`synchronized`), Control de Espera Pasiva (`wait`/`notifyAll`), Región Crítica, Condiciones de Carrera.
* **Resumen:** Simulación interactiva del ecosistema de un Pescador (Productor) y una colonia de Gatos (Consumidores) compitiendo por un recurso compartido con buffer acotado (Cesta con capacidad fija = 2). Implementa exclusión mutua estricta para mitigar condiciones de carrera y un mecanismo de muestreo determinista en el hilo principal para el cierre elegante (*graceful shutdown*) del sistema al cumplirse los predicados de negocio.

### 3. 🌐 Módulo 03: Servidor Distribuido TCP Multihilo y Simulación de Probabilidad
* **Conceptos clave:** Sockets TCP (`ServerSocket`), Conexiones Concurrentes, Lógica de Negocio Probabilística.
* **Resumen:** Sistema distribuido Cliente-Servidor que emula una estación de Inspección Técnica de Vehículos ("ITV del Infierno") que gestiona 4 líneas de atención síncronas. Los hilos clientes se conectan a través de Sockets TCP y son atendidos de forma asíncrona por hilos servidores (Inspectores). Incorpora un motor de probabilidad dinámico que penaliza la tasa de éxito (del 60% inicial al -10% acumulativo) mediante el procesamiento semántico de frases prohibidas (comportamiento de "cuñao"). Incluye una clase `SimuladorClientes` para pruebas de carga masivas.

### 4. 🔒 Módulo 04: Servidor Web HTTP Concurrente y Endurecimiento de Seguridad (HTTPS)
* **Conceptos clave:** Sockets Seguros (`SSLServerSocket`), Cifrado Simétrico (AES), Hasheo de Credenciales (BCrypt), Regex, Patrón de Lectores-Escritores, Logging Activo.
* **Resumen:** Evolución del sistema de la ITV hacia un **Servidor Web HTTPS Seguro** nativo, capaz de procesar peticiones `GET` y `POST` desde navegadores comerciales. 
  * **Seguridad en Tránsito:** Implementación de TLS/SSL mediante certificados generados con `keytool`.
  * **Seguridad en Reposo:** Gestión criptográfica de un fichero de persistencia (`usuarios.txt`). Las contraseñas sufren un proceso de salado y hashing con **BCrypt**. El fichero se almacena cifrado en todo momento con **AES**, realizándose procesos síncronos de descifrado-operación-cifrado en memoria RAM en cada acceso de hilo.
  * **Robustez:** Validación estricta de datos de entrada vía Expresiones Regulares (Regex) en el Backend y auditoría automatizada ante accesos denegados en un fichero perimetral `log.txt`.

---

## 🛠️ Stack Tecnológico Utilizado

* **Lenguaje:** Java (JDK 11 / 17)
* **Criptografía y Seguridad:** AES (Advanced Encryption Standard), BCrypt, JSSE (Java Secure Socket Extension).
* **Network & Concurrency:** Java Threads, Sockets TCP, SSLServerSockets, Stream Filters.
* **Herramientas:** Apache Ant, NetBeans IDE, Keytool CLI.

---

## 🚀 Requisitos de Ejecución Globales

1. Clonar el repositorio en local:
   ```bash
   git clone [https://github.com/aroadonaire/ProgramacionMultiproceso.git](https://github.com/aroadonaire/ProgramacionMultiproceso.git)
