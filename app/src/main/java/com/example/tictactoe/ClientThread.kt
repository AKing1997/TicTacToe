package com.example.tictactoe

import android.os.AsyncTask
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

class ClientThread(
    private val onConnect: () -> Unit,
    private val onMessageReceived: (String) -> Unit,
    private val onConnectionError: () -> Unit,
    private val serverIpAddress: String,
    private val serverPort: Int
) : Thread() {
    private var socket: Socket? = null
    private var inputStream: ObjectInputStream? = null
    private var outputStream: ObjectOutputStream? = null

    override fun run() {
        try {
            socket = Socket(serverIpAddress, serverPort)
            inputStream = ObjectInputStream(socket!!.getInputStream())
            outputStream = ObjectOutputStream(socket!!.getOutputStream())
            onConnect.invoke()
            while (!currentThread().isInterrupted) {
                try {
                    val message = inputStream!!.readObject() as String
                    onMessageReceived.invoke(message)
                } catch (e: IOException) {
                    onConnectionError.invoke()
                    break
                } catch (e: ClassNotFoundException) {
                    onConnectionError.invoke()
                    break
                }
            }
        } catch (e: IOException) {
            onConnectionError.invoke()
        }
    }

    fun sendMessage(message: String?) {
        AsyncTask.execute {
            try {
                outputStream!!.writeObject(message)
                outputStream!!.flush()
            } catch (e: IOException) {
                onConnectionError.invoke()
            }
        }
    }

    fun disconnect() {
        try {
            if (socket != null) {
                socket!!.close()
            }
            if (inputStream != null) {
                inputStream!!.close()
            }
            if (outputStream != null) {
                outputStream!!.close()
            }
        } catch (e: IOException) {
            onConnectionError.invoke()
        }
    }
}