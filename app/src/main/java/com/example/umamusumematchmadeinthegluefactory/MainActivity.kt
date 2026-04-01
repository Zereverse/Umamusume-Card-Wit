package com.example.umamusumematchmadeinthegluefactory

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.umamusumematchmadeinthegluefactory.ui.theme.UmamusumeMatchMadeInTheGlueFactoryTheme


object Routes {
    const val MAIN = "main"
    const val LOGINSCREEN = "login"
    const val SIGNUPSCREEN = "signup"
}

// REGISTERED USERS GO HERE
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
                        LogIn(
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

                // Activity Content

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
                            println("Success! Access Granted.")
                            errorMessage = ""
//                            MainMenu()
                        } else {
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

//@Composable
//fun UserChecker(){
//
//}

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

//Previews Debugging Purposes
@Preview(showBackground = true)
@Composable
fun LogoPreview() {
    UmamusumeMatchMadeInTheGlueFactoryTheme {
        BackgroundImage()
        Logo()
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ClickablePreview() {
//    UmamusumeMatchMadeInTheGlueFactoryTheme {
//        BackgroundImage()
//        Clickable()
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun LogInPreview() {
//    UmamusumeMatchMadeInTheGlueFactoryTheme {
//        BackgroundImage()
//        LogIn()
//    }
//}