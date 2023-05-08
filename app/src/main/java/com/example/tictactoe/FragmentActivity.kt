package com.example.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        val bottomNav: BottomNavigationView = findViewById(R.id.nav);
        val fragment: String = intent.getStringExtra("FRAGMENT").toString()
        val ip: String = intent.getStringExtra("IP").toString()
        val port: String = intent.getStringExtra("PORT").toString()

        if(fragment == "online"){
            loadFragment(Online(ip, port));
        }else{
            loadFragment(OffLine(ip, port));
        }
        bottomNav.setOnItemSelectedListener { item: MenuItem ->
            // condicione como Switch
            when (item.itemId) {
                R.id.offline -> {// Frgmento Offline
                    loadFragment(OffLine(ip, port))
                    true
                }
                R.id.online -> {// fragmento online
                    loadFragment(Online(ip, port))
                    true
                }
                else -> {false}
            }
        }
    }
    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment ,fragment);
        transaction.addToBackStack(null);
        transaction.commit()
    }
}