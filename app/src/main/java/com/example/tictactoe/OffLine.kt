package com.example.tictactoe

import android.app.ProgressDialog
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
import android.widget.TextView
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


class OffLine(ip: String, port: String) : Fragment(),
    ServerConnectionTask.ServerConnectionListener {

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
    private lateinit var materialToolbarOff: MaterialToolbar;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_off_line, container, false);
        materialToolbarOff = view.findViewById(R.id.materialToolbarOff)
        ganador = view.findViewById(R.id.sccoreConteiner)
        msgGanador = view.findViewById(R.id.msgOff)
        ganadorEs = view.findViewById(R.id.ganador)
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
                if (player && tree[index] == "-1") {
                    player = false
                    cambiarImagen(button, if (selected) O else X, blue)
                    tree[index] = if (selected) "1" else "0"
                    treeToString()?.let { it1 -> connectToServer(it1) };
                }
            }
        }
    }

    private fun treeToString(): String? {
        var temp = ""
        for (i in tree.indices) {
            temp += if (i < tree.size - 1) tree[i] + "," else tree[i]
        }
        return temp+";"+if (!selected) "1" else "0"
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

    fun setPosicion(x: Int, selectedOp: String) {
        if (tree[x] != "-1") return;
        tree[x] = selectedOp
    }

    fun setMachiPlayer(){
        val how = if (!selected) "1" else "0"
        for ((index, button) in btn.withIndex()) {
                if (tree[index] == how) {
                    cambiarImagen(button, if (!selected) O else X, red)
                }
        }
    }

    private fun connectToServer(request: String) {
        val serverConnectionTask = ServerConnectionTask(ADDRESS, SERVERPORT, request, this)
        serverConnectionTask.execute()
    }

    override fun onServerResponse(response: String) {
        player = true
        Log.i("afasa", response)
        tree = response.split(",").toTypedArray()
        setMachiPlayer()
        if (esGanador("1")) {
            ganador.setBackgroundResource(
                if(selected) blue else red

            )
            ganadorEs.text = "X"
            ganador.visibility = View.VISIBLE
        }
        if(esGanador("0")){
            ganador.setBackgroundResource(
                if(selected) blue else red
            )
            ganadorEs.text = "O"
            ganador.visibility = View.VISIBLE
        }
    }
}