package com.example.chatbotapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatbotapp.Pages.HomePage
import com.example.chatbotapp.Pages.LoginPage
import com.example.chatbotapp.Pages.SignInPage

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier,authViewModel: AuthViewModel){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login" ){
        composable("login"){
            LoginPage(modifier,navController,authViewModel)
        }
        composable("signin"){
            SignInPage(modifier,navController,authViewModel)
        }
        composable("home"){
            HomePage(modifier,navController,authViewModel)
        }
    }

}