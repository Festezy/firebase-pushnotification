package com.ariqandrean.firebasepushnotificationapp

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ariqandrean.firebasepushnotificationapp.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful){
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result

//            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, "Token: $token" )
            binding.token.setText("$token")
            Toast.makeText(this, "Token: $token", Toast.LENGTH_LONG).show()
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

        FirebaseMessaging.getInstance().subscribeToTopic("all").addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.d(TAG, "Successfully send to all")
            } else {
                //sub service failed
            }
        }
    }
}