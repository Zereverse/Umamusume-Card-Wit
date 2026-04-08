package com.example.umamusumematchmadeinthegluefactory

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.core.content.edit
import com.example.umamusumematchmadeinthegluefactory.gameLogic.CardModel
import com.example.umamusumematchmadeinthegluefactory.gameLogic.GameLogic
import com.example.umamusumematchmadeinthegluefactory.gameLogic.PopulateCard

@Composable
fun Easy(onNavigateBack: () -> Unit, onNavigateToScoreBoard: () -> Unit) {
    GameScreen(rows = 4, cols = 4, difficulty = "Easy", onNavigateBack = onNavigateBack, onNavigateToScoreBoard = onNavigateToScoreBoard)
}

@Composable
fun Medium(onNavigateBack: () -> Unit, onNavigateToScoreBoard: () -> Unit) {
    GameScreen(rows = 5, cols = 4, difficulty = "Medium", onNavigateBack = onNavigateBack, onNavigateToScoreBoard = onNavigateToScoreBoard)
}

@Composable
fun Hard(onNavigateBack: () -> Unit, onNavigateToScoreBoard: () -> Unit) {
    GameScreen(rows = 6, cols = 6, difficulty = "Hard", onNavigateBack = onNavigateBack, onNavigateToScoreBoard = onNavigateToScoreBoard)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(rows: Int, cols: Int, difficulty: String, onNavigateBack: () -> Unit, onNavigateToScoreBoard: () -> Unit) {
    val context = LocalContext.current
    val numUniqueCards = (rows * cols) / 2
    val populateCard = remember { PopulateCard(numUniqueCards) }

    var cards by remember { mutableStateOf(populateCard.populateCard()) }
    var score by remember { mutableIntStateOf(0) }
    var showWinDialog by remember { mutableStateOf(false) }

    val gameLogic = remember {
        GameLogic(
            onScoreUpdate = { score = it },
            onFlipBack = { _, _ ->
                cards = cards.toList()
            },
            onMatchFound = { _, _ ->
                cards = cards.toList()
            },
            onGameEnd = {
                saveScore(context, difficulty, score)
                showWinDialog = true
            }
        )
    }

    LaunchedEffect(Unit) {
        gameLogic.initGame(rows * cols)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Score: $score", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    Button(onClick = onNavigateBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            BackgroundImage()
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(cols),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(cards, key = { it.id }) { card ->
                    MemoryCard(card = card, onClick = {
                        gameLogic.onCardTapped(card)
                    })
                }
            }
        }
    }

    if (showWinDialog) {
        EndGameScreen(score = score, onNavigateToScoreBoard = onNavigateToScoreBoard)
    }
}

private fun saveScore(context: Context, difficulty: String, score: Int) {
    val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    val userName = sharedPref.getString("USER_NAME", "Guest") ?: "Guest"
    
    val scorePref = context.getSharedPreferences("scoreboard", Context.MODE_PRIVATE)
    val currentScores = scorePref.getString("scores", "") ?: ""
    val newScore = "$userName:$difficulty:$score"
    val updatedScores = if (currentScores.isEmpty()) newScore else "$currentScores|$newScore"
    
    scorePref.edit {
        putString("scores", updatedScores)
    }
}

@Composable
fun EndGameScreen(score: Int, onNavigateToScoreBoard: () -> Unit){
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
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
                Text(
                    text = "You Win!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(140, 205, 60)
                )

                Text(
                    text = "Final Score: $score",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )

                Button(
                    onClick = onNavigateToScoreBoard,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(140, 205, 60))
                ){
                    Text(
                        text = "Continue to Scoreboard",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreBoard(onNavigateToMainMenu: () -> Unit){
    val context = LocalContext.current
    val scorePref = context.getSharedPreferences("scoreboard", Context.MODE_PRIVATE)
    val scoresString = scorePref.getString("scores", "") ?: ""
    
    val scoreList = scoresString.split("|")
        .filter { it.isNotEmpty() }
        .map {
            val parts = it.split(":")
            Triple(parts[0], parts[1], parts[2])
        }
        .sortedByDescending { it.third.toIntOrNull() ?: 0 }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Scoreboard",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Card(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Player", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text("Difficulty", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                            Text("Score", fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f), textAlign = TextAlign.End)
                        }
                        HorizontalDivider()
                    }

                    items(scoreList) { (name, difficulty, score) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(name, modifier = Modifier.weight(1f), fontSize = 18.sp)
                            
                            val diffColor = when (difficulty.lowercase()) {
                                "easy" -> Color(0xFF4CAF50)
                                "medium" -> Color(0xFFFFC107)
                                "hard" -> Color(0xFFF44336)
                                else -> Color.Black
                            }
                            
                            Text(
                                text = difficulty,
                                color = diffColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                            
                            Text(
                                text = score,
                                modifier = Modifier.weight(0.7f),
                                textAlign = TextAlign.End,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNavigateToMainMenu,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(140, 205, 60))
            ) {
                Text("Back to Main Menu", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MemoryCard(card: CardModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(0.75f)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (card.isFlipped || card.isMatched) {
                Image(
                    painter = painterResource(id = card.frontImg),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = card.backImg),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
