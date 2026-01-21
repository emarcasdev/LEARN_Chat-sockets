package org.example.chatsockets;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.*;

public class Server {
    // Definimos el puerto del servidor y el max de usuarios
    private static int PORT = 8080;
    private static int MAX_USERS = 10;

    // Pool para limitar el nº de hilos según el max de usuarios
    private static ExecutorService pool = Executors.newFixedThreadPool(MAX_USERS);
    // Lista de salidas de todos los usuarios conectados para realizar el broadcast
    private static Set<PrintWriter> writers = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        System.out.println("Servidor corriendo en el puerto " + PORT);

        // El socket escucha las nuevas conexiones
        try(ServerSocket server = new ServerSocket(PORT)) {
            // Acepta usuarios constantemente
            while (true) {
                // Espera la conexión de usuario
                Socket socket = server.accept();
                // Atendemos al usuario con un hilo del pool
                pool.execute(new UserManager(socket));
            }
        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        } finally {
            // Cierra el pool de manera ordenada
            pool.shutdown();
        }
    }

    // Enviar el mensaje a todos los usuarios conectados
    private static void broadcast(String message) {
        for (PrintWriter writer : writers) {
            writer.println(message);
        }
    }

    // Atiende al usuario
    private static class UserManager implements Runnable {
        private Socket socket;

        UserManager(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            // Enviar mensajes a este usuario
            PrintWriter writer = null;
            // Nombre por defecto
            String name = "Usuario";
            // Indicar que se cerró al escribir "salir"
            boolean quitText = false;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))){
                // Enviar mensajes a este usuario
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
                // Obtener nombre usuario y agregarlo a la lista de usuarios
                name = reader.readLine();
                writers.add(writer);
                // Mensaje global de que se conecto
                broadcast("Info: " + name + " se ha conectado.");

                // Leer los mensajes
                String text;
                while ((text = reader.readLine()) != null) {
                    // Si se escribe salir abandona el chat
                    if (text.equalsIgnoreCase("salir")) {
                        quitText = true;
                        break;
                    }
                    // Reenviar a todos el mensaje
                    broadcast(name + ": " + text);
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            } finally {
                quitUser(socket, writer, name, quitText);
            }
        }
    }

    // Desconectar al usuario
    private static void quitUser(Socket socket, PrintWriter writer, String name, boolean quitText) {
        // Quitar al usuario de la lista
        if (writer != null) writers.remove(writer);
        // Aviso global de la desconexion
        if (quitText) broadcast(name + ": Adios");
        broadcast("Info: " + name + " se ha desconectado.");
        // Cerrar el socket
        try {
            socket.close();
        } catch (Exception e) {}
    }
}