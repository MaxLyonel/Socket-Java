
import javax.swing.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;

import java.awt.event.*; // librerias necesarias para manejar interfaces, ventanas, swing
import java.io.IOException; //manejo de flujo de datos
import java.io.ObjectInputStream; //manejo de flujos con objetos
import java.io.ObjectOutputStream; //salida
import java.io.Serializable; //para conversion de byte los objetos que queramos enviar
import java.net.*; //libreria necesaria para socket en TCP


// Creación de la clase cliente
public class Cliente {

    public static void main(final String[] args) {

        // Solo son herramientas necesarias para mostrar con una interfaz 
        // mucho mas amigables, como se verá en el resultado más adelante

        // Creamo un marco, o ventana de interfaz
        final MarcoCliente mimarco = new MarcoCliente();
        // funcionalidades de la ventana
        // método o función para cerrar la ventana
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}

// creación de una clase marcocliente
// de nueva cuenta esto para mostrar 
// con una interfaz más amigable

class MarcoCliente extends JFrame {

    // Constructor de la clase
    public MarcoCliente() {

        // medidas necesarias, medidas de la ventana del resultado
        setBounds(600, 300, 280, 350);
        // creando objeto de la clase laminaMarcoCliente
        final LaminaMarcoCliente milamina = new LaminaMarcoCliente();
        // lo addicionamos 
        add(milamina);
        // lo mostramos o hacemos visible 
        setVisible(true);
    }

}

// de nueva cuenta creamos una clase para laminaMarcoCliente
// esto para la interfaz del resultado
class LaminaMarcoCliente extends JPanel implements Runnable {

    private JComboBox micombo;
    public LaminaMarcoCliente() {


        
        final JLabel texto = new JLabel("CLIENTE");
        add(texto);
        campoRes = new JTextArea(12, 20);
        add(campoRes);
        campo1 = new JTextField(20);
        // add(campo1);
        miboton = new JButton("Enviar");

        JPanel lamina_norte = new JPanel();
        micombo = new JComboBox();
        micombo.addItem("Opcion 1");
        micombo.addItem("Opcion 2");
        micombo.addItem("Opcion 3");
        lamina_norte.add(micombo);
        add(lamina_norte);
        final EnviaTexto mievento = new EnviaTexto();
        miboton.addActionListener(mievento);

        add(miboton);
        Thread mihilo = new Thread(this);
        mihilo.start();
    }


    // Creamos una clase interna dentro de la clase CLiente 
    // la clase EnviaTexto será la encargada de implementar 
    // las funcionalidades para el envío de paquetes con TCP
    // al servidor
    private class EnviaTexto implements ActionListener { 



        // Sobre escribimnos el método actionPerformed al implementar la clase ActionListener
        @Override
        public void actionPerformed(final java.awt.event.ActionEvent e) { 

            try {
                // creamos el socket
                final Socket misocket = new Socket("127.0.0.1", 9999);
                // Creamos un objeto de nuestra clase
                PaqueteEnvio datos = new PaqueteEnvio();
                // cambiamos los atributos con los datos que se escriba en
                // nuestro campo de texto de la ventana
                // datos.setMensaje(campo1.getText());

                datos.setMensaje((String) micombo.getSelectedItem());
                // creamos el flujo de envio para objetos
                ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOutputStream());
                // escribimos los datos en el objeto de salida de flujo.
                paquete_datos.writeObject(datos);
                // cerramos el socket
                misocket.close();

            } catch (final UnknownHostException e1) {
                e1.printStackTrace();
            } catch (final IOException e1) {
                System.out.println(e1.getMessage());
            }

        }

    }

    // DECLARAMOS VARIABLES para la interfaz
    private final JTextField campo1;
    private final JTextArea campoRes;
    private final JButton miboton;

    // Implementación de un hilo o proceso
    @Override
    public void run() {
        try {
            // Creamos un socket servidor, que reciba peticiones en el puerto 9998
            ServerSocket servidor_cliente = new ServerSocket(9998);
            Socket cliente;
            PaqueteEnvio paqueteRecibido;
            // un while para que el cliente se ponga a la escucha de peticiones o respuestas del servidor
            while(true){

                // Aceptamos los flujos o peticiones entrantes
                cliente = servidor_cliente.accept();
                // Creamos el flujo de tipo objeto para recibir o recepcionar los datos procedente del servidor en respuesta
                // a la petición echa
                ObjectInputStream flujo_entrada = new ObjectInputStream(cliente.getInputStream());
                // convertimos el flujo de datos en un tipo de dato Object
                paqueteRecibido = (PaqueteEnvio) flujo_entrada.readObject();
                // Escribimos los datos en el campoRes que es un campo de tipo textArea para visualizar
                // la respuesta del serrvidor
                campoRes.append("\nRespuesta del servidor:\n  "+paqueteRecibido.getMensaje());

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}

// Creamos la clase que contendra los datos necesarios para
// enviar al Servidor.

class PaqueteEnvio implements Serializable {
    private String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    

   

}