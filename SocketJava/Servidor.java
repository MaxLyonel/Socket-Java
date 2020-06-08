
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.util.logging.Level;


// Creación de la clase Servidor
public class Servidor {

    public static void main(String[] args) {

        // De nueva cuenta código solo para mostrar en una interfaz 
        // como ser una ventana, donde se podrá visualizar mejor las respuestas
        MarcoServidor mimarco = new MarcoServidor();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    
}

// Clases para la creación de la interfaz, para visualizar el resultado

class MarcoServidor extends JFrame implements Runnable {
    
    public static String obtenerFechaYHoraActual() {
        String formato = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern(formato);
        LocalDateTime ahora = LocalDateTime.now();
        return formateador.format(ahora);
    }

    public MarcoServidor() {

        setBounds(1200, 300, 280, 350);
        JPanel milamina = new JPanel();
        // final JLabel texto = new JLabel("SERVIDOR");
        milamina.setLayout(new BorderLayout());
        
        // milamina.add(texto);
        areatexto = new JTextArea();
        // areatexto.add(texto);
        milamina.add(areatexto, BorderLayout.CENTER);
        add(milamina);
        setVisible(true);
        Thread mihilo = new Thread(this); // implementación de un hilo
        mihilo.start(); // haciendo correr el hilo 
    }

    private JTextArea areatexto;

    // método que estará a la escucha
    @Override
    public void run() {
        // final Logger logger = Logger.getLogger(MarcoServidor.class.getName());

        // logger.setLevel(Level.WARNING);
        // logger.warning("Comenzando el main");
        try {
            // Poniendo a la escucha el servidor abriendo el puerto 9999
            ServerSocket servidor = new ServerSocket(9999);

            String mensaje;

            PaqueteEnvio paquete_recibido;

            while(true){
            // que acepte cualquier conexión del exterior
            Socket misocket = servidor.accept();
            // System.out.println("-------Datos del Cliente---------");
            // System.out.println("Dirección IP del cliente: "+misocket.getInetAddress());
            // System.out.println("Puerto del cliente: "+misocket.getPort());
            // System.out.println("-------Datos del Servidor--------");
            // System.out.println("Puerto Local del Servidor: "+misocket.getLocalPort());

            // creamos el flujo de entrada para el objeto que recibirá un Object
            ObjectInputStream paquete_datos = new ObjectInputStream(misocket.getInputStream());
            // Convertimos el flujo a Object
            paquete_recibido = (PaqueteEnvio) paquete_datos.readObject();
            // mediante el método get obtenemos el mensaje del Objeto
            mensaje = paquete_recibido.getMensaje();
            String mensaje_cliente= mensaje;
            switch(mensaje){
                case "Opcion 1":
                    mensaje = "Papel";
                break;

                case "Opcion 2":
                    mensaje = "Piedra";
                break;

                case "Opcion 3":
                    mensaje = "Tijer";
                break;

                default:
                    mensaje = "Aún sin peticiones";
                break;
                     
            }

            mensaje_cliente = mensaje_cliente + "-->" + mensaje;

            // misocket.setSoTimeout(30000);
            // System.out.println("---Datos que van a ser enviados al Cliente---- ");
            // System.out.println("Datos: -> "+ mensaje);

            // System.out.println("Tiempo de espera :"+misocket.getSoTimeout()+ " en  milisegundos");
            
            // lo mostramos en la ventana de la interfaz
            areatexto.append("\nMensaje del Cliente:\n   "+mensaje_cliente);

            // creamos un socket para el envío
            Socket enviaDestinatario = new Socket("127.0.0.1", 9998);
            // convertimos el mensaje a mayusculas
            // mensaje = mensaje.toUpperCase();
            // obtenemos la fecha y hora actual con un método obtenerFechaYHoraActual
            // String fechaYHora = obtenerFechaYHoraActual();
            // Concadenamos a la cadena
            // mensaje = mensaje+" "+fechaYHora;
            // seteamos o cambiamos el valor del mensaje en el objeto
            paquete_recibido.setMensaje(mensaje);
            // Creamos el flujo para el envío del paquete
            ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
            // introducimos la información en el Objeto
            paqueteReenvio.writeObject(paquete_recibido);
            paqueteReenvio.close();
            // cerramos los socktes
            enviaDestinatario.close();
            misocket.close();
            
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // logger.severe("Problemas dentro del main");
        }
    }
}
