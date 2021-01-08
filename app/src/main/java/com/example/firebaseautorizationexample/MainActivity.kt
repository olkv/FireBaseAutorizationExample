package com.example.firebaseautorizationexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null

    var edEmail: EditText? = null
    var edPassord:EditText? = null

    var txtTitle:TextView? = null
    var txtWelcome:TextView? = null

    var btnEnter:Button? = null
    var btnRegistration:Button? = null
    var btnBegin:Button? = null
    var btnExit:Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        auth = FirebaseAuth.getInstance()
        edEmail = findViewById(R.id.edEmail)
        edPassord = findViewById(R.id.edPassword)

        txtTitle = findViewById(R.id.txtTitleLogin)
        txtWelcome = findViewById(R.id.txtWelcome)

        btnEnter = findViewById(R.id.btnEnter)
        btnRegistration = findViewById(R.id.btnRegistration)
        btnBegin = findViewById(R.id.btnBegin)
        btnExit = findViewById(R.id.btnExit)
    }

    override fun onStart() {
        super.onStart()
        val user = auth!!.currentUser

        if(user==null) {
            Toast.makeText(this,"Пользователь не зарегистрирован",Toast.LENGTH_SHORT).show()
        }
        else
        {
            showBeginAction()
        }

    }

    fun onClickRegistration(view: View) {
        val mEmail:String = edEmail!!.text.toString()
        val mPassword = edPassord!!.text.toString()

        auth!!.createUserWithEmailAndPassword(mEmail, mPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)  {
                    sendEmail()
                    showBeginAction()

                    Toast.makeText(this, "Регистрация выполнена.",
                            Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Ошибка при регистрации.",
                            Toast.LENGTH_SHORT).show()
                }
            }

    }

    fun onClickEnter(view: View) {
        val mEmail:String = edEmail!!.text.toString()
        val mPassword = edPassord!!.text.toString()
        auth!!.signInWithEmailAndPassword(mEmail,mPassword)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        showBeginAction()

                        Toast.makeText(this, "Авторизация выполнена.",
                                Toast.LENGTH_SHORT).show()

                    }
                    else {
                        Toast.makeText(this, "Ошибка авторизации.",
                                Toast.LENGTH_SHORT).show()

                    }
                }
    }

    fun sendEmail() {
        val user = auth!!.currentUser
        user!!.sendEmailVerification()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Проверьте вашу почту для подтверждения регистрации.",
                                Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, "Ошибка отправки почтового сообщения.",
                                Toast.LENGTH_SHORT).show()
                    }
                }

    }

    fun showBeginAction() {
        val user = auth!!.currentUser
        val mEmail = user!!.email

        txtTitle!!.visibility = View.GONE
        edEmail!!.visibility = View.GONE
        edPassord!!.visibility = View.GONE
        btnEnter!!.visibility = View.GONE
        btnRegistration!!.visibility = View.GONE

        txtWelcome!!.visibility = View.VISIBLE
        btnExit!!.visibility = View.VISIBLE

        if(user!!.isEmailVerified) {
            btnBegin!!.visibility = View.VISIBLE
            txtWelcome!!.text = "Добро пожаловать $mEmail в систему. Вы можите начать работать."

        } else {
            txtWelcome!!.text = "Добро пожаловать $mEmail. Вам необходимо подтвердить регистрацию."
        }
    }

    fun showRegistration() {
        txtTitle!!.visibility = View.VISIBLE
        edEmail!!.visibility = View.VISIBLE
        edPassord!!.visibility = View.VISIBLE
        btnEnter!!.visibility = View.VISIBLE
        btnRegistration!!.visibility = View.VISIBLE

        txtWelcome!!.visibility = View.GONE
        btnBegin!!.visibility = View.GONE
        btnExit!!.visibility = View.GONE
    }

    fun onClickExit(view: View) {
        auth!!.signOut()
        edEmail!!.setText("")
        edPassord!!.setText("")

        showRegistration()
        
    }

}