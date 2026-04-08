package com.example.umamusumematchmadeinthegluefactory

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.umamusumematchmadeinthegluefactory.ui.theme.UmamusumeMatchMadeInTheGlueFactoryTheme

// REGISTERED USERS GO HERE (I hope)
data class User(var name: String, var password: String)

val users = mutableStateListOf(
    User("admin", "54321"),
    User("test", "12345"),
    User("user", "password")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            UmamusumeMatchMadeInTheGlueFactoryTheme {
                val sharedPref = getSharedPreferences("auth", MODE_PRIVATE)
                val isLoggedIn = sharedPref.getBoolean("IS_LOGGED_IN", false)

                if (isLoggedIn) {
                    startActivity(Intent(this, MainMenu::class.java))
                    finish() // prevents the app from going back to the login screen
                }

                BackgroundImage()

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Routes.MAIN) {
                    composable(Routes.MAIN) {
                        MainScreen(
                            onNavigateToLogIn = {
                                navController.navigate(Routes.LOGINSCREEN)
                            },
                            onNavigateToSignUp = {
                                navController.navigate(Routes.SIGNUPSCREEN)
                            }
                        )
                    }

                    composable(Routes.LOGINSCREEN) {
                        LogIn (
                            onNavigateToSignUp = {
                                navController.navigate(Routes.SIGNUPSCREEN)
                            }
                        )
                    }

                    composable(Routes.SIGNUPSCREEN){
                        SignUp(
                            onNavigateToLogIn = {
                                navController.navigate(Routes.LOGINSCREEN)
                            }
                        )
                    }
                }
            }
        }
    }
}

//Functions for Activity Content
@Composable
fun BackgroundImage(){
    Image(
        painter = painterResource(id = R.drawable.mainmenu),
        contentDescription = "Background Image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun PlaymatImage(){
    Image(
        painter = painterResource(id = R.drawable.playmat),
        contentDescription = "Background Image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun Logo(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box (
            modifier = Modifier
                .background(
                    // 0.0f = fully transparent, 1.0f = fully opaque
                    color = Color(193, 193, 193).copy(alpha = 0.5f)
                )
                .padding(10.dp)
        ) {
            Text(
                text = "Umamusume Match Made in the Glue Factory",
                fontSize = 32.sp,
                textAlign = TextAlign.Right,
                color = Color.White,
                letterSpacing = 1.sp,
                lineHeight = 50.sp,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun MainScreen(onNavigateToLogIn: () -> Unit, onNavigateToSignUp: () -> Unit) {
    Logo()
    Clickable(onNavigateToLogIn, onNavigateToSignUp)
}

@Composable
fun Clickable(onNavigateToLogIn: () -> Unit, onNavigateToSignUp: () -> Unit){
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 128.dp)
    ){
        //LogIn
        Button (
            onClick = onNavigateToLogIn,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(140, 205, 60),
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = "Log In"
            )
        }

        //SignUp
        Button (
            onClick = onNavigateToSignUp,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(140, 205, 60),
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = "Sign Up"
            )
        }
    }
}

@Composable
fun LogIn(onNavigateToSignUp: () -> Unit){
    //Temporary Values for checking
    var errorMessage by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(235, 235, 235)
            ),
            modifier = Modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(24.dp)
            ) {
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Text(
                    text = "Log In",
                    fontSize = 32.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 30.dp)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    )
                )

                Button(
                    onClick = {
                        val isAuthorized = users.any { it.name == username && it.password == password }

                        if (isAuthorized) {
                            errorMessage = ""
                            // When login is successful, save it
                            val sharedPref = context.getSharedPreferences("auth", MODE_PRIVATE)

                            //saves the data locally
                            sharedPref.edit {
                                putBoolean("IS_LOGGED_IN", true)
                                putString("USER_NAME", username)
                            }

                            //Passes specified values to the MainMenu(next) activity
                            val intent = Intent(context, MainMenu::class.java).apply {
                                putExtra("USER_NAME", username)
                                putExtra("IS_LOGGED_IN", true)
                            }

                            context.startActivity(intent)
                            (context as Activity).finish() //Destroys the Login Activity

                        }
                        else {
                            errorMessage = "Error: Username or Password do not match"
                        }
                    },
                    modifier = Modifier
                        .padding(top = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(140, 205, 60),
                        contentColor = Color.White
                    )
                ) {
                    Text("Log In")
                }

                Button(
                    onClick = onNavigateToSignUp,
                    modifier = Modifier
                        .padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(140, 205, 60),
                        contentColor = Color.White
                    )
                ){
                    Text("Sign Up")
                }
            }
        }
    }
}

@Composable
fun SignUp(onNavigateToLogIn: () -> Unit){
    //Temporary Values for checking
    var errorMessage by remember { mutableStateOf("") }
    var newUsername by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(235, 235, 235)
            ),
            modifier = Modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(24.dp)
            ) {
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Text(
                    text = "Sign Up",
                    fontSize = 32.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 30.dp)
                )

                OutlinedTextField(
                    value = newUsername,
                    onValueChange = { newUsername = it },
                    label = { Text("Username") },
                    textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    )
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Password") },
                    textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    )
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Password") },
                    textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    )
                )

                Button(
                    onClick = {
                        if (newUsername != "" && newPassword != "" && newPassword == confirmPassword) {
                            users.add(User(newUsername, newPassword))
                            errorMessage = ""
                            onNavigateToLogIn()
                        }
                        else if (newPassword != confirmPassword) {
                            errorMessage = "Error: Passwords do not match"
                        }
                        else {
                            errorMessage = "Error: Fields cannot be empty"
                        }
                    },
                    modifier = Modifier
                        .padding(top = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(140, 205, 60),
                        contentColor = Color.White
                    )
                ) {
                    Text("Sign Up")
                }

                Button(
                    onClick = onNavigateToLogIn,
                    modifier = Modifier
                        .padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(140, 205, 60),
                        contentColor = Color.White
                    )
                ){
                    Text("Log In")
                }
            }
        }
    }
}
