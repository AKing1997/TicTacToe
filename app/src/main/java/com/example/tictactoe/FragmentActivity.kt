package com.example.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        val bottomNav: BottomNavigationView = findViewById(R.id.nav);
        val fragment: String = intent.getStringExtra("FRAGMENT").toString()
        if(fragment == "online"){
            loadFragment(Online());
        }else{
            loadFragment(OffLine());
        }
        bottomNav.setOnItemSelectedListener { item: MenuItem ->
            // condicione como Switch
            when (item.itemId) {
                R.id.offline -> {// Frgmento Offline
                    loadFragment(OffLine())
                    true
                }
                R.id.online -> {// fragmento online
                    loadFragment(Online())
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