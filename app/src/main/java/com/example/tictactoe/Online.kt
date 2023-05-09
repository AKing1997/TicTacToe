package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar

class Online(ip: String, port: String) : Fragment() {
    private val ip = ip
    private val port = port
    private lateinit var resetBtn: Button
    private lateinit var homeBtn: Button

    companion object {
        private var tree = Array(9) { "-1" }
        private lateinit var toast: Unit
        fun conectedMsg() {
            toast
        }

        private lateinit var ganador: ConstraintLayout
        private lateinit var msgGanador: TextView
        private lateinit var ganadorEs: TextView

        /**
         * Puerto
         */
        private var SERVERPORT: Int = 0

        /**
         * HOST
         */
        private lateinit var ADDRESS: String
        private var player: Boolean = true;
        fun esGanador(p: String): Boolean {
            return (tree[0] == p && tree[1] == p && tree[2] == p ||
                    tree[3] == p && tree[4] == p && tree[5] == p ||
                    tree[6] == p && tree[7] == p && tree[8] == p ||
                    tree[0] == p && tree[4] == p && tree[8] == p ||
                    tree[6] == p && tree[4] == p && tree[2] == p ||
                    tree[0] == p && tree[3] == p && tree[6] == p ||
                    tree[1] == p && tree[4] == p && tree[7] == p ||
                    tree[2] == p && tree[5] == p && tree[8] == p)
        }
    }


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
    private lateinit var materialToolbarOn: MaterialToolbar;
    private lateinit var clientThread: ClientThread

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_online, container, false);
        materialToolbarOn = view.findViewById(R.id.materialToolbarOn)
        materialToolbarOn.setTitle("Online")

        ganador = view.findViewById(R.id.sccoreConteinerOn)
        msgGanador = view.findViewById(R.id.msgOn)
        ganadorEs = view.findViewById(R.id.ganadorOn)
        resetBtn = view.findViewById(R.id.resetBtnOn)
        homeBtn = view.findViewById(R.id.homeBtnOn)

        x = view.findViewById(R.id.x)
        o = view.findViewById(R.id.o)
        SERVERPORT = Integer.parseInt(port)
        ADDRESS = ip

        resetBtn.setOnClickListener {
            resetGame()
        }
        homeBtn.setOnClickListener {
            val redrige: Intent = Intent(view.context, MainActivity::class.java)
            startActivity(redrige)
        }

        for (i in 1..9) {
            val id = resources.getIdentifier("ob$i", "id", requireContext().packageName)
            val imageButton = view.findViewById<ImageButton>(id)
            btn.add(imageButton)
        }

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
        clientThread = ClientThread(
            onConnect = {
                // La conexión con el servidor se ha establecido correctamente
            },
            onMessageReceived = { response ->
                player = true
                Log.i("ASFAFSGAS", response)
                tree = response.split(",").toTypedArray()
                setMachiPlayer()
                gano()
            },
            onConnectionError = {
                // Ha ocurrido un error de conexión con el servidor
            },
            serverIpAddress = ADDRESS,
            serverPort = SERVERPORT + 2
        )
        clientThread.start()
        return view;
    }

    fun gano(){
        if (esGanador(if (selected) "1" else "0")) {
            visibleGanador(if (selected) "1" else "0")
        }
    }

    fun disableRBtn() {
        x.isClickable = false
        o.isClickable = false
    }

    fun setOnlistner() {
        for ((index, button) in btn.withIndex()) {
            button.setOnClickListener {
                if (player && tree[index] == "-1") {
                    player = false
                    cambiarImagen(button, if (selected) O else X, blue)
                    tree[index] = if (!selected) "1" else "0"

                    treeToString()?.let { it1 -> clientThread.sendMessage(it1) };
                    if (esGanador(if (!selected) "1" else "0")) {
                        visibleGanador(if (!selected) "1" else "0")
                    }
                }
            }
        }
    }

    private fun treeToString(): String? {
        var temp = ""
        for (i in tree.indices) {
            temp += if (i < tree.size - 1) tree[i] + "," else tree[i]
        }
        return temp + ";" + if (selected) "1" else "0"
    }

    fun cambiarImagen(img: ImageButton, image: Int, backG: Int) {
        img.setBackgroundResource(backG)
        img.setImageResource(image)
    }

    fun resetGame() {
        tree.fill("-1")
        for (b in btn) {
            cambiarImagen(b, transp, R.drawable.border)
        }
        ganador.visibility = View.GONE
    }

    fun setMachiPlayer() {
        val how = if (selected) "1" else "0"
        for ((index, button) in btn.withIndex()) {
            if (tree[index] == how) {
                cambiarImagen(button, if (!selected) O else X, red)
            }
        }
    }

    fun visibleGanador(how: String) {
        if (!(selected && how == "0" || !selected && how == "1")) {
            ganador.setBackgroundColor(getResources().getColor(R.color.red))
        } else {
            ganador.setBackgroundColor(getResources().getColor(R.color.green))
        }
        ganadorEs.text = if (how == "0") "O" else "X"
        msgGanador.text =
            if (selected && how == "0") "Felicidades ;-)" else "Para la proxima partida!"
        ganador.visibility = View.VISIBLE
    }
}