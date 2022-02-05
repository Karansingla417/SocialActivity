package com.example.sociallyactive

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import com.example.sociallyactive.daos.Userdao
import com.example.sociallyactive.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_ina_ativity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignInaAtivity : AppCompatActivity() {

    private val RC_SIGN_IN: Int = 123
    private val TAG= "SignINActivity Tag"
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_ina_ativity)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth= Firebase.auth

        signinbutton.setOnClickListener {
            signIn()
        }

    }

    override fun onStart() {
        super.onStart()
        val currentuser=auth.currentUser
        updateUI(currentuser)
    }
        private fun signIn() {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignINresult(task)
        }
    }

    private fun handleSignINresult(completedtask: Task<GoogleSignInAccount>) {
        try {

            val account =
                completedtask.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w(TAG, "Google sign in failed" + e.statusCode)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential =GoogleAuthProvider.getCredential(idToken,null)
        signinbutton.visibility=View.GONE
        progresdbar.visibility=View.VISIBLE

        GlobalScope.launch (Dispatchers.IO){
            val auth=auth.signInWithCredential(credential).await()
            val firebaseUser=auth.user
            withContext(Dispatchers.Main){
                updateUI(firebaseUser)
            }

        }

    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
           if(firebaseUser!=null)
           {
               val user =User(firebaseUser.uid,firebaseUser.displayName,firebaseUser.photoUrl.toString())
               val usersdao=Userdao()
               usersdao.adduser(user)
               val mainActivityIntent =Intent(this,MainActivity::class.java)
               startActivity(mainActivityIntent)
               finish()
           }
        else{
               signinbutton.visibility=View.VISIBLE
               progresdbar.visibility=View.GONE

           }
    }
}


