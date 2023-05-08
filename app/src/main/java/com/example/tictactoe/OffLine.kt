package com.example.tictactoe

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import java.io.IOException
import java.io.PrintStream
import java.lang.Integer.parseInt
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import java.nio.charset.Charset


class OffLine(ip: String, port: String) : Fragment() {
    private val ip = ip
    private val port = port
    private lateinit var resetBtn: Button
    private lateinit var homeBtn: Button
    companion object {
        private lateinit var toast: Unit
        fun conectedMsg() {
            toast
        }

        private lateinit var ganador: ConstraintLayout
        /**
         * Puerto
         */
        private var SERVERPORT: Int = 0

        /**
         * HOST
         */
        private lateinit var ADDRESS: String
        private var player: Boolean = true;

        fun hayGanador(tablero: Array<String>): Boolean {
            // Verificar filas
            for (fila in 0 until 3) {
                val indiceInicial = fila * 3
                if (tablero[indiceInicial] != "-1" && tablero[indiceInicial] == tablero[indiceInicial + 1] && tablero[indiceInicial] == tablero[indiceInicial + 2]) {
                    return true
                }
            }
            // Verificar columnas
            for (columna in 0 until 3) {
                if (tablero[columna] != "-1" && tablero[columna] == tablero[columna + 3] && tablero[columna] == tablero[columna + 6]) {
                    return true
                }
            }
            // Verificar diagonales
            if (tablero[0] != "-1" && tablero[0] == tablero[4] && tablero[0] == tablero[8]) {
                return true
            }
            if (tablero[2] != "-1" && tablero[2] == tablero[4] && tablero[2] == tablero[6]) {
                return true
            }
            // No hay ganador
            return false
        }



    }

    private val tree = Array(9) { "-1" }

    private val btn: ArrayList<ImageButton> = ArrayList()
    private lateinit var x: RadioButton;
    private lateinit var o: RadioButton;

    private val red: Int = R.drawable.button_background_red
    private val blue: Int = R.drawable.button_background
    private val X: Int = R.drawable.x_24
    private val O: Int = R.drawable.o_24
    private val transp: Int = R.drawable.transp

    private var selected: Boolean = false;
    private var selectedBool: Boolean = false;
    private lateinit var materialToolbarOff: MaterialToolbar;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_off_line, container, false);
        materialToolbarOff = view.findViewById(R.id.materialToolbarOff)
        ganador = view.findViewById(R.id.sccoreConteiner)
        resetBtn = view.findViewById(R.id.resetBtn)
        homeBtn = view.findViewById(R.id.homeBtn)
        toast = Toast.makeText(view.context, "Connected", Toast.LENGTH_SHORT).show()
        resetBtn.setOnClickListener {
            resetGame()
        }
        homeBtn.setOnClickListener {
            val redrige: Intent = Intent(view.context, MainActivity::class.java)
            startActivity(redrige)
        }
        materialToolbarOff.title = "OffLine"
        SERVERPORT = parseInt(port)
        ADDRESS = ip
        val playGBtn: RadioGroup = view.findViewById(R.id.playGBtn)
        for (i in 1..9) {
            val id = resources.getIdentifier("b$i", "id", requireContext().packageName)
            val imageButton = view.findViewById<ImageButton>(id)
            btn.add(imageButton)
        }
        x = view.findViewById(R.id.x)
        o = view.findViewById(R.id.o)



        /*playGBtn.setOnCheckedChangeListener { radioGroup, i ->
            val value =
                ((view.findViewById(radioGroup.getCheckedRadioButtonId()) as RadioButton).text).toString();
            Toast.makeText(view.context, value, Toast.LENGTH_SHORT).show()
        }*/
        if (!selectedBool) {
            x.setOnClickListener {
                selected = false;
                selectedBool = true
                setOnlistner()
                disableRBtn()
            }
            o.setOnClickListener {
                selected = true;
                selectedBool = true
                setOnlistner()
                disableRBtn()
            }
        }
        return view;
    }

    fun disableRBtn() {
        x.isClickable = false
        o.isClickable = false
    }

    fun setOnlistner() {
        for ((index, button) in btn.withIndex()) {
            button.setOnClickListener {
                if (player) {
                    player = false
                    cambiarImagen(button, if (selected) O else X, blue)
                    tree[index] = "1"
                    val myATaskYW = MyATaskCliente()
                    myATaskYW.execute(treeToString())
                }
            }
        }
    }

    private fun treeToString(): String? {
        var temp = ""
        for (i in tree.indices) {
            temp += if (i < tree.size - 1) tree[i] + "," else tree[i]
        }
        return temp
    }

    fun cambiarImagen(img: ImageButton, image: Int, backG: Int) {
            img.setBackgroundResource(backG)
            img.setImageResource(image)
    }

    fun resetGame(){
        tree.fill("-1")
        for (b in btn){
            cambiarImagen(b, transp, R.drawable.border)
        }
    }
    fun setPosicion(x: Int,selectedOp: String) {
        if (tree[x] != "-1") return;
        tree[x] = selectedOp
    }

    internal class MyATaskCliente :
        AsyncTask<String?, Void?, String?>() {
        /**
         * Ventana que bloqueara la pantalla del movil hasta recibir respuesta del servidor
         */
        var progressDialog: ProgressDialog? = null

        /**
         * muestra una ventana emergente
         */
        override fun onPreExecute() {
            super.onPreExecute()
            conectedMsg()
        }

        /**
         * Se conecta al servidor y trata resultado
         */
        override fun doInBackground(vararg values: String?): String? {
            return try {
                //Se conecta al servidor
                val serverAddr = InetAddress.getByName(ADDRESS)
                Log.i("I/TCP Client", "Connecting...")
                val socket = Socket(serverAddr, SERVERPORT)
                Log.i("I/TCP Client", "Connected to server")

                //envia peticion de cliente
                Log.i("I/TCP Client", "Send data to server")
                val output = PrintStream(socket.getOutputStream())
                val request = values[0]
                output.println(request)

                //recibe respuesta del servidor y formatea a String
                Log.i("I/TCP Client", "Received data to server")
                val stream = socket.getInputStream()
                val lenBytes = ByteArray(256)
                stream.read(lenBytes, 0, 256)
                val received = String(lenBytes, Charset.forName("UTF-8")).trim { it <= ' ' }
                Log.i("I/TCP Client", "Received $received")
                Log.i("I/TCP Client", "")
                //cierra conexion
                socket.close()
                received
            } catch (ex: UnknownHostException) {
                Log.e("E/TCP Client", "" + ex.message)
                ex.message
            } catch (ex: IOException) {
                Log.e("E/TCP Client", "" + ex.message)
                ex.message
            }
        }

        /**
         * Oculta ventana emergente y muestra resultado en pantalla
         */
        override fun onPostExecute(value: String?) {
            player = true
            val array = value?.split(",")?.toTypedArray()
            if(array?.let { hayGanador(it) }!!){
                ganador.visibility = View.VISIBLE
            }
        }
    }
}