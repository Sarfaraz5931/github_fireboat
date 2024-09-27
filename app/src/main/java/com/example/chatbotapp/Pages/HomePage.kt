package com.example.chatbotapp.Pages


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.chatbotapp.AuthViewModel
import com.example.chatbotapp.MessageModel
import com.example.chatbotapp.R


@Composable
fun HomePage(modifier: Modifier = Modifier,navController: NavController,authViewModel: AuthViewModel) {
    Column (modifier=Modifier){
        AppHeader(authViewModel)
        MessageList(modifier = Modifier.weight(1f),messageList = authViewModel.messageList)
        MessageInput(onMessageSend = {
            authViewModel.sendMessage(it)

        })
    }

}

@Composable

fun  AppHeader(authViewModel: AuthViewModel){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.primary)){
        Row (modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround){
            Text(text = "Chat Bot", modifier = Modifier.padding(16.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp)

            Text(text = "Logout",
                Modifier.clickable { authViewModel.SignOut() })

        }


            

        }
        



    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(onMessageSend :  (String) -> Unit){
    var message by remember { mutableStateOf("") }

    Row (modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically){
        OutlinedTextField(value = message , onValueChange ={message=it}, modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = colorResource(id = R.color.mix)),
            shape = RoundedCornerShape(20.dp),

        )


        IconButton(onClick = {
            if (message.isNotEmpty()){
            onMessageSend(message)
        message = ""}
        }) {
            Icon(imageVector = Icons.Default.Send, contentDescription ="Send" )

        }

    }
}

@Composable
fun MessageList(modifier:Modifier = Modifier,messageList : List<MessageModel>){
    LazyColumn(modifier = modifier,
        reverseLayout = true){
        items(messageList.reversed()){
          MessageRow(messageModel = it)

        }

    }

}

@Composable
fun MessageRow(messageModel: MessageModel){

    val isModel = messageModel.role == "model"

    Row (verticalAlignment = Alignment.CenterVertically){
        Box (modifier = Modifier.fillMaxWidth()){
            Box (modifier = Modifier
                .padding(

                    start = if (isModel) 8.dp else 70.dp,
                    end = if (isModel) 70.dp else 8.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
                .clip(RoundedCornerShape(48f))
                .background(if (isModel) colorResource(id = R.color.model) else colorResource(id = R.color.mix))
                .padding(16.dp)
                .align(
                    if (isModel) Alignment.BottomStart else Alignment.BottomEnd
                )){
                SelectionContainer {
                    Text(text = messageModel.message, fontWeight = FontWeight.W500)

                }

            }
        }
    }

}
