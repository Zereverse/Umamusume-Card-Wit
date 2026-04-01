package com.example.umamusumematchmadeinthegluefactory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umamusumematchmadeinthegluefactory.ui.theme.UmamusumeMatchMadeInTheGlueFactoryTheme

class CardList : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
            Uma("Seiun Sky", R.drawable.sky),
            Uma("Silence Suzuka", R.drawable.suzuka),
            Uma("Tokai Teio", R.drawable.teio),
            Uma("Special Week", R.drawable.week),
            Uma("Daitaku Helios (Wei)", R.drawable.wei)
        )

        setContent {
            UmamusumeMatchMadeInTheGlueFactoryTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Background image
                        Image(
                            painter = painterResource(id = R.drawable.mainmenu),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

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
        }
    }
}

// paste this to the list cards section
@Composable
fun ListItem(name: String, imageRes: Int) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
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
                horizontalAlignment = Alignment.CenterHorizontally
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
                        .padding(24.dp, 24.dp, 24.dp, 30.dp),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListItemPreview() {
    UmamusumeMatchMadeInTheGlueFactoryTheme {
        ListItem("Haru Urara", R.drawable.haru)
    }
}