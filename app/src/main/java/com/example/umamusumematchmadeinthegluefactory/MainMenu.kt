package com.example.umamusumematchmadeinthegluefactory

import android.app.Activity
import android.content.Context
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.umamusumematchmadeinthegluefactory.ui.theme.UmamusumeMatchMadeInTheGlueFactoryTheme
import androidx.core.content.edit
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainMenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val sharedPref = getSharedPreferences("auth", MODE_PRIVATE)
    val userName = sharedPref.getString("USER_NAME", "Guest") ?: "Guest"

    setContent {
        UmamusumeMatchMadeInTheGlueFactoryTheme {
            BackgroundImage()

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Routes.MAINMENU) {
                    composable(Routes.MAINMENU) {
                        Logo()
                        Main(
                            userName,
                            onNavigateToStartGame = {
                                navController.navigate(Routes.STARTGAME)
                            },
                            onNavigateToCardList = {
                                navController.navigate(Routes.CARDLIST)
                            },
                            onNavigateToQuitGameScreen = {
                                navController.navigate(Routes.QUITGAME)
                            }
                        )
                    }

                    composable(Routes.STARTGAME) {
                        StartGame(
                            onNavigateToMAINMENU = {
                                navController.navigate(Routes.MAINMENU)
                            }
                        )
                    }

                    composable(Routes.CARDLIST) {
                        CardList(
                            onNavigateToMAINMENU = {
                                navController.navigate(Routes.MAINMENU)
                            }
                        )
                    }

                    composable(Routes.QUITGAME) {
                        QuitGameScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun Main(
    userName: String,
    onNavigateToStartGame: () -> Unit,
    onNavigateToCardList: () -> Unit,
    onNavigateToQuitGameScreen: () -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(top = 50.dp, start = 20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Hello welcome to the Glue Factory, $userName"
        )
        LogOut()
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 70.dp)
    ) {
        //Start Game
        Button(
            onClick = {
                onNavigateToStartGame()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(140, 205, 60),
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(top = 20.dp)
                .width(200.dp)
        ){
            Text(
                text = "Start Game"
            )
        }

        //Card List
        Button(
            onClick = {
                onNavigateToCardList()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(140, 205, 60),
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(top = 20.dp)
                .width(200.dp)
        ){
            Text(
                text = "Card List"
            )
        }

        //Quit Game
        Button(
            onClick = {
                onNavigateToQuitGameScreen()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(140, 205, 60),
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(top = 20.dp)
                .width(200.dp)
        ){
            Text(
                text = "Quit Game"
            )
        }
    }
}

@Composable
fun QuitGameScreen(){
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ){
        Text(
            text = "Do you want to Quit and Log out?"
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                    sharedPref.edit { clear() }
                    (context as Activity).finishAffinity()
                },
                modifier = Modifier
                    .padding(end = 20.dp)
            ) {
                Text(
                    text = "Yes"
                )
            }

            Button(
                onClick = {
                    (context as Activity).finish()
                },
                modifier = Modifier
                    .padding(end = 20.dp)
            ) {
                Text(
                    text = "No"
                )
            }
        }
    }
}

@Composable
fun LogOut(){
    val context = LocalContext.current
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp)
    ){
        Button(
            onClick = {
                val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                sharedPref.edit { clear() }

                val intent = Intent(context, MainActivity::class.java).apply {
                    putExtra("IS_LOGGED_IN", true)
                }
                context.startActivity(intent)
                (context as Activity).finish() // terminates main menu
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(140, 205, 60),
                contentColor = Color.White
            )
        ){
            Text("Log Out")
        }
    }
}

@Composable
fun StartGame(onNavigateToMAINMENU: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Card container
        Card(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(28.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // EASY
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4FC3F7),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "EASY", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                // MEDIUM
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFCA28),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "MEDIUM", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                // HARD
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEC407A),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "HARD", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(4.dp))

                // BACK
                Button(
                    onClick = onNavigateToMAINMENU,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(140, 205, 60),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "BACK", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CardList(onNavigateToMAINMENU: () -> Unit) {
    data class Uma(val name: String, val imageRes: Int)

    val umaList = listOf(
        Uma("Haru Urara", R.drawable.haru),
        Uma("Kitasan Black", R.drawable.black),
        Uma("Agnes Tachyon", R.drawable.agtac),
        Uma("Sakura Bakushin", R.drawable.bakushin),
        Uma("Gold Ship", R.drawable.gold),
        Uma("Manhattan Cafe", R.drawable.cafe),
        Uma("Oguri Cap", R.drawable.cap),
        Uma("Espoir City", R.drawable.city),
        Uma("Tamamo Cross", R.drawable.cross),
        Uma("Satono Diamond", R.drawable.diamond),
        Uma("Fenomeno", R.drawable.fenomeno),
        Uma("Matikanetannhauser (Mambo)", R.drawable.mambo),
        Uma("Mejiro McQueen", R.drawable.mcqueen),
        Uma("Twin Turbo", R.drawable.turbo),
        Uma("Silence Suzuka", R.drawable.suzuka),
        Uma("Tokai Teio", R.drawable.teio),
        Uma("Special Week", R.drawable.week),
        Uma("Daitaku Helios (Wei)", R.drawable.wei)
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
        ) {
            BackgroundImage()
            Button(
                onClick = {
                    onNavigateToMAINMENU()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(140, 205, 60),
                    contentColor = Color.White
                )
            ){
                Text(
                    text = "Back",
                )
            }

            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(umaList) { uma ->
                    ListItem(name = uma.name, imageRes = uma.imageRes)
                }
            }
        }
    }
}
@Composable
fun ListItem(name: String, imageRes: Int) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box {
            // Backcard Image
            Image(
                painter = painterResource(id = R.drawable.backcard),
                contentDescription = "Background image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                //Card Image
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "photo of this uma",
                    modifier = Modifier
                        .width(500.dp)
                        .height(500.dp)
                        .padding(15.dp)
                )

                Text(
                    text = name,
                    modifier = Modifier
                        .padding(top = 10.dp),
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


//FOR DEBUGGING PURPOSES

//@Preview(showBackground = true)
//@Composable
//fun StartGamePreview(){
//    UmamusumeMatchMadeInTheGlueFactoryTheme {
//        BackgroundImage()
//        StartGame()
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun ListItemPreview() {
//    UmamusumeMatchMadeInTheGlueFactoryTheme {
//        ListItem("Haru Urara", R.drawable.haru)
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun LogOutPreview(){
//    UmamusumeMatchMadeInTheGlueFactoryTheme {
//        BackgroundImage()
//        LogOut()
//    }
//}