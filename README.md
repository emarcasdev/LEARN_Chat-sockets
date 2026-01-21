# Chat Multi-Usuario

## Descripción

En esta práctica se ha desarrollado un **chat multi-usuario** con **Java** y **JavaFX**, utilizando **Sockets** y **Threads**.

El chat permite que varios usuarios conectados simultaneamente, puedan envíar mensajes y que estos se muestren en tiempo real al resto de usuarios conectados.

---

## Funcionamiento general

1. El servidor se inicia y queda a la espera de conexiones
2. El cliente se conecta introduciendo su nombre
3. Los mensajes enviados se envían y se muestran a todos los usuarios
4. El comando `salir` cierra la conexión de forma segura
