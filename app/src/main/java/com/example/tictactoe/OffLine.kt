package com.example.tictactoe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import com.google.android.material.appbar.MaterialToolbar

class OffLine : Fragment() {
    private lateinit var b3: ImageButton
    private lateinit var b4: ImageButton
    private lateinit var b2: ImageButton
    private lateinit var b1: ImageButton
    private lateinit var b5: ImageButton
    private lateinit var b6: ImageButton
    private lateinit var b7: ImageButton
    private lateinit var b8: ImageButton
    private lateinit var b9 : ImageButton;
    private lateinit var x : RadioButton;
    private lateinit var o : RadioButton;
    private lateinit var selected: String;
    private var selectedBool: Boolean = false;
    private lateinit var materialToolbarOff: MaterialToolbar;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_off_line, container, false);
        materialToolbarOff = view.findViewById(R.id.materialToolbarOff)
        materialToolbarOff.setTitle("OffLine")
        b3 = view.findViewById(R.id.b3)
        b4 = view.findViewById(R.id.b4)
        b2 = view.findViewById(R.id.b2)
        b1 = view.findViewById(R.id.b1)
        b5 = view.findViewById(R.id.b5)
        b6 = view.findViewById(R.id.b6)
        b7 = view.findViewById(R.id.b7)
        b8 = view.findViewById(R.id.b8)
        b9 = view.findViewById(R.id.b9)
        x = view.findViewById(R.id.x)
        o = view.findViewById(R.id.o)
        return view;
    }
}