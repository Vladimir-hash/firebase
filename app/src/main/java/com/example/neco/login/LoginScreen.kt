package com.example.neco.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Composable
fun LoginScreen() {

    val auth = Firebase.auth
    val emailState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }
    Log.d("my lod", "User email: ${auth.currentUser?.email}")


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = emailState.value, onValueChange = {
            emailState.value = it
        })
        Spacer(modifier = Modifier.height(10.dp))
        TextField(value = passwordState.value, onValueChange = {
            passwordState.value = it
        })
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            signIn(auth, emailState.value, passwordState.value)
        }){
            Text(text = "Sign Ip")
        }
        Button(onClick = {
            signUp(auth, emailState.value, passwordState.value)
        }) {
            Text(text = "Sign Up")
        }
        Button(onClick = {
            signOut(auth)
        }){
            Text(text = "Sign Out")
        }
        Button(onClick = {
            deleteAccount(auth, emailState.value, passwordState.value)
        }){
            Text(text = "Delete account")
        }
    }
}

private fun signUp(auth: FirebaseAuth, email: String, password: String){
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("MyLOg", "Sign up successful!")
            } else{
                Log.d("MyLOg", "Sign up failure!")
            }

        }
}
private fun signIn(auth: FirebaseAuth, email: String, password: String){
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener {


        }
}
private fun signOut(auth: FirebaseAuth){
        auth.signOut()
}
private fun deleteAccount(auth: FirebaseAuth, email: String, password: String){
    val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener {
            if(it.isSuccessful){
                auth.currentUser?.delete()?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("MyLog", "Account was deleted")
                    } else{
                        Log.d("MyLog", "Failure deleting account!")
                    }
                }
            } else {
                Log.d("MyLog", "Failure reauthenticate")
            }
        }
}