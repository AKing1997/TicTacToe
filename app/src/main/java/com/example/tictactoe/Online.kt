package com.example.tictactoe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar

class Online(ip: String, port: String) : Fragment() {
    companion object {
        lateinit var context: Context
    }
    private lateinit var ob3: ImageButton
    private lateinit var ob4: ImageButton
    private lateinit var ob2: ImageButton
    private lateinit var ob1: ImageButton
    private lateinit var ob5: ImageButton
    private lateinit var ob6: ImageButton
    private lateinit var ob7: ImageButton
    private lateinit var ob8: ImageButton
    private lateinit var ob9 : ImageButton;
    private lateinit var x : RadioButton;
    private lateinit var o : RadioButton;
    private lateinit var selected: String;
    private var selectedBool: Boolean = false;
    private lateinit var clientThread: ClientThread
     private lateinit var materialToolbarOn: MaterialToolbar;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_online, container, false);
        materialToolbarOn = view.findViewById(R.id.materialToolbarOn)
        materialToolbarOn.setTitle("Online")
        ob3 = view.findViewById(R.id.ob3)
        ob4 = view.findViewById(R.id.ob4)
        ob2 = view.findViewById(R.id.ob2)
        ob1 = view.findViewById(R.id.ob1)
        ob5 = view.findViewById(R.id.ob5)
        ob6 = view.findViewById(R.id.ob6)
        ob7 = view.findViewById(R.id.ob7)
        ob8 = view.findViewById(R.id.ob8)
        ob9 = view.findViewById(R.id.ob9)
        x = view.findViewById(R.id.x)
        o = view.findViewById(R.id.o)

        clientThread = ClientThread(
            onConnect = {
                // La conexión con el servidor se ha establecido correctamente
            },
            onMessageReceived = { message ->
                // Se ha recibido un mensaje del servidor
            },
            onConnectionError = {
                // Ha ocurrido un error de conexión con el servidor
            },
            serverIpAddress = "10.0.2.2",
            serverPort = 5002
        )

        clientThread.start()
        return view;
    }
}