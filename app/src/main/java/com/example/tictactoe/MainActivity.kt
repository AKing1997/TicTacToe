package com.example.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    private lateinit var ip: EditText;
    private lateinit var port: EditText;
    private lateinit var connect: Button;
    private lateinit var gamePlayOpt: ConstraintLayout;
    private lateinit var ipErMsg: TextView;
    private lateinit var portErMsg: TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val offLine: Button = findViewById(R.id.offLine)
        val online: Button = findViewById(R.id.online)

        ip = findViewById(R.id.ip)
        port = findViewById(R.id.port)
        connect = findViewById(R.id.connect)
        gamePlayOpt = findViewById(R.id.gamePlayOpt)

        ipErMsg = findViewById(R.id.ipErMsg)
        portErMsg = findViewById(R.id.portErMsg)

        val redrige = Intent(this, FragmentActivity::class.java);
        connect.setOnClickListener {
            val esIpValid = Regex("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\$");
            val esPortValid = Regex("^\\d{3,5}\$");
            if((esIpValid.matches(ip.text.toString()) || ip.text.toString() == "localhost")&& esPortValid.matches(port.text.toString())){
                ipErMsg.visibility = View.GONE
                portErMsg.visibility = View.GONE
                gamePlayOpt.visibility = View.VISIBLE;
                redrige.putExtra("IP",ip.text.toString())
                redrige.putExtra("PORT",port.text.toString())
            }else{
                ipErMsg.visibility = if(esIpValid.matches(ip.text.toString())) View.GONE else View.VISIBLE;
                portErMsg.visibility = if(esPortValid.matches(port.text.toString())) View.GONE else View.VISIBLE;
                gamePlayOpt.visibility = View.GONE;
            }
        }
        offLine.setOnClickListener {
            redrige.putExtra("FRAGMENT","offLine")
            startActivity(redrige)
        }

        online.setOnClickListener {
            redrige.putExtra("FRAGMENT","online")
            startActivity(redrige)
        }
    }
}