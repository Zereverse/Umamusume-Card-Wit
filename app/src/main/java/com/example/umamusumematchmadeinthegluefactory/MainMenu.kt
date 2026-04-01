package com.example.umamusumematchmadeinthegluefactory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.umamusumematchmadeinthegluefactory.ui.theme.UmamusumeMatchMadeInTheGlueFactoryTheme
import androidx.core.content.edit

class MainMenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPref = getSharedPreferences("auth", MODE_PRIVATE)
        val userName = sharedPref.getString("USER_NAME", "Guest") ?: "Guest"

        setContent {
            UmamusumeMatchMadeInTheGlueFactoryTheme {
                BackgroundImage()
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = "Hello welcome to the Glue Factory $userName"
                    )
                    LogOut()
                }
            }
        }
    }
}

@Composable
fun LogOut(){
    val context = LocalContext.current
    Button(
        onClick = {
            val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            sharedPref.edit { clear() }

            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("IS_LOGGED_IN", true)
            }
            context.startActivity(intent)
        }
    ){
        Text("Log Out")
    }
}