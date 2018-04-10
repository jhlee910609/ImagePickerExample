package com.example.junheelee.imagepickerexample

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    val TAG = "MainActivity"
    val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    lateinit var string: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mAuth = FirebaseAuth.getInstance()
        var refer = database.getReference("response")
        Log.e(TAG, refer.key)
        refer.setValue("hello")



        fab.setOnClickListener { startActivity(Intent(baseContext, AddPicActivity::class.java)) }
        button2.setOnClickListener {
            mAuth.signInWithEmailAndPassword("jhlee910609@naver.com", "wnsgml337!")
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = mAuth.currentUser
                            Snackbar.make(button2, "login is successful!", Snackbar.LENGTH_SHORT).show()
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(button2, "login is failed!", Snackbar.LENGTH_SHORT).show()

                        }

                        // ...
                    }
        }

        ratingBar.apply {
            setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                "========================== [ your rating is $rating ]"
                        .trim()
                        .apply {
                            Snackbar.make(ratingBar, this, Toast.LENGTH_SHORT).show()
                        }
            }
        }
        btn_func.setOnClickListener {

            val rating: Double = Math.random() * 10
            val tempRating = (rating * 10).toInt()
            val units = (tempRating / 10).toDouble()
            val tempTenths: Double = (tempRating - units * 10).let {
                val tempUnit = 10
                if (it in (0.1 * tempUnit)..(0.5 * tempUnit)) {
                    0.5
                } else if (it in (0.6 * tempUnit)..(1 * tempUnit).toDouble()) {
                    1.0
                } else {
                    0.0
                }
            }

            Snackbar.make(btn_func, "this rating is ${units + tempTenths}", Snackbar.LENGTH_SHORT).show()
            startActivity<Main2Activity>()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
