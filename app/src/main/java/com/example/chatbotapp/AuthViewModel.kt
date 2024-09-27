package com.example.chatbotapp

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbotapp.Pages.Constant
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel:ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    val generativeModel =
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = Constant.apikey
        )


    private val _auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState


    init {
        checkAuthStatus()
    }


    fun checkAuthStatus() {
        if (_auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun Login(email: String, password: String) {

        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or Password can't be empty")
        }


        _authState.value = AuthState.Loading

        _auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated

                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "something went wrong")

                }
            }
    }


    fun SignUp(email: String, password: String) {

        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or Password can't be empty")
        }


        _authState.value = AuthState.Loading

        _auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated

                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "something went wrong")

                }
            }
    }

    fun SignOut() {
        _auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }





    fun sendMessage(question: String) {
        viewModelScope.launch {

            try {
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        content(it.role){text(it.message)}
                    }.toList()
                )

                messageList.add(MessageModel(question,"user"))
                messageList.add(MessageModel("Typing....","model"))

                val response = chat.sendMessage(question)
                messageList.removeLast()

                messageList.add(MessageModel(response.text.toString(),"model"))


            }catch (e:Exception){
                messageList.removeLast()
                messageList.add(MessageModel("Error:"+e.message.toString(),"model"))
            }


        }


    }
}

sealed class AuthState{
    object Authenticated:AuthState()
    object Unauthenticated:AuthState()
    object Loading:AuthState()
    data class Error(val message:String):AuthState()
}