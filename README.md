# Chat Multi-Usuario

## Descripción

En este proyecto se ha desarrollado un **chat multi-usuario** utilizando **Java** y **JavaFX**, basado en una arquitectura **cliente-servidor** con **sockets** y **concurrencia** mediante hilos.

La aplicación permite que varios usuarios se conecten al mismo tiempo y envíen mensajes que se muestran en tiempo real al resto de usuarios conectados.

---

## Descripción de las clases

### Server

Clase que implementa el **servidor del chat**.

Funciones principales:

* Abre un `ServerSocket` en el puerto 8080
* Acepta múltiples conexiones de usuarios (max: 10)
* Gestiona cada usuario en un hilo independiente
* Reenvía los mensajes a todos los usuarios conectados

---

### UserManager

Clase encargada de **gestionar un usuario conectado**.

* Lee el nombre del usuario al conectarse
* Escucha los mensajes enviados por el cliente
* Detecta el comando `salir` para desconectar al usuario
* Notifica las conexiones y desconexiones al resto del chat

---

### Client

Clase que representa al **cliente del chat**.

* Se conecta al servidor mediante `Socket`
* Envía el nombre del usuario al conectarse
* Permite enviar mensajes de texto
* Escucha mensajes del servidor en un hilo independiente

---

### ChatController

Controlador JavaFX que gestiona la **interfaz gráfica**.

* Valida el nombre de usuario
* Controla la conexión y desconexión
* Envía mensajes escritos por el usuario
* Actualiza la interfaz usando `Platform.runLater()`

---

### ChatMessage

Modelo de datos utilizado para mostrar los mensajes.

Tipos de mensaje:

* `INFO` --> mensajes del sistema
* `ME` --> mensajes del propio usuario
* `OTHER` --> mensajes de otros usuarios

---

## Funcionamiento general

1. El servidor se inicia y queda a la espera de conexiones
2. El cliente se conecta introduciendo un nombre
3. Los mensajes enviados se retransmiten a todos los usuarios
4. El comando `salir` cierra la conexión de forma segura
