package com.example.tictactoe
import android.os.AsyncTask
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintStream
import java.net.Socket

class ServerConnectionTask(private val serverIP: String, private val serverPort: Int, private val request: String, private val listener: ServerConnectionListener) : AsyncTask<Void, Void, String>() {

    interface ServerConnectionListener {
        fun onServerResponse(response: String)
    }

    override fun doInBackground(vararg params: Void?): String {
        var response = ""
        var clientSocket: Socket? = null
        var input: BufferedReader? = null
        var output: PrintStream? = null

        try {
            // Conexión al servidor
            clientSocket = Socket(serverIP, serverPort)
            println("Cliente> Conexión establecida con el servidor")

            // Para leer la respuesta del servidor
            input = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
            // Para enviar la solicitud al servidor
            output = PrintStream(clientSocket.getOutputStream())

            // Envío de la solicitud al servidor
            output.println(request)
            output.flush()
            println("Cliente> Solicitud enviada al servidor")

            // Lectura de la respuesta del servidor
            response = input.readLine()
            println("Cliente> Respuesta del servidor: $response")
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            // Cierre de recursos
            try {
                input?.close()
                output?.close()
                clientSocket?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return response
    }

    override fun onPostExecute(result: String) {
        listener.onServerResponse(result)
    }
}
