package org.example.chatsockets;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class Client {
    // Socket para la conexión con el servidor
    private Socket socket;
    // Reader para leer los mensajes del servidor
    private BufferedReader reader;
    // Writer para enviar mensajes al servidor
    private PrintWriter writer;

    // Comprobar que el cliente está conectado
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    // Conecta al servidor y escucha los mensajes
    public void connect(String host, int port, String name, Consumer<String> message) throws Exception {
        // Abrimos el socket al servidor
        socket = new Socket(host, port);

        // Evitar problemas con acentos
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

        // Enviar el nombre
        writer.println(name);

        // Lee los mensajes del servidor sin bloquear la interfaz
        Thread listener = new Thread(() -> {
            try {
                String text;
                // Lee los mensajes del servidor hasta que se cierre la conexión
                while ((text = reader.readLine()) != null) {
                    // Lo enviamos al controller
                    message.accept(text);
                }
            } catch (Exception e) {
                System.err.println("Error en el listener: " + e.getMessage());
            } finally {
                close();
            }
        }, "chat-listener");

        listener.setDaemon(true); // No impide que la app se cierre
        listener.start();
    }

    // Envía el mensaje al servidor
    public void send(String message) {
        if (writer != null) writer.println(message);
    }

    // Cerrar el socket y limpiar referencias
    public void close() {
        try {
            if (socket != null) socket.close();
        } catch (Exception e) {
            System.err.println("Error al cerrar el socket: " + e.getMessage());
        }
        socket = null;
        reader = null;
        writer = null;
    }
}