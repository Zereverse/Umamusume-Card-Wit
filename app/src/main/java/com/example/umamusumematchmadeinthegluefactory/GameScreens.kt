package com.example.umamusumematchmadeinthegluefactory

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.umamusumematchmadeinthegluefactory.gameLogic.CardModel
import com.example.umamusumematchmadeinthegluefactory.gameLogic.GameLogic
import com.example.umamusumematchmadeinthegluefactory.gameLogic.PopulateCard

@Composable
fun Easy(onNavigateBack: () -> Unit) {
    GameScreen(rows = 4, cols = 4, onNavigateBack = onNavigateBack)
}

@Composable
fun Medium(onNavigateBack: () -> Unit) {
    GameScreen(rows = 5, cols = 4, onNavigateBack = onNavigateBack)
}

@Composable
fun Hard(onNavigateBack: () -> Unit) {
    GameScreen(rows = 6, cols = 6, onNavigateBack = onNavigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(rows: Int, cols: Int, onNavigateBack: () -> Unit) {
    val numUniqueCards = (rows * cols) / 2
    val populateCard = remember { PopulateCard(numUniqueCards) }

    var cards by remember { mutableStateOf(populateCard.populateCard()) }
    var score by remember { mutableIntStateOf(0) }
    var showWinDialog by remember { mutableStateOf(false) }

    val gameLogic = remember {
        GameLogic(
            onScoreUpdate = { score = it },
            onFlipBack = { _, _ ->
                cards = cards.toList().toMutableList() as ArrayList<CardModel>
            },
            onMatchFound = { _, _ ->
                cards = cards.toList().toMutableList() as ArrayList<CardModel>
            },
            onGameEnd = { showWinDialog = true }
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
                items(cards) { card ->
                    MemoryCard(card = card, onClick = {
                        gameLogic.onCardTapped(card)
                        // Force UI update
                        cards = cards.toList().toMutableList() as ArrayList<CardModel>
                    })
                }
            }
        }
    }

    if (showWinDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("You Win!") },
            text = { Text("Final Score: $score") },
            confirmButton = {
                Button(onClick = onNavigateBack) {
                    Text("Main Menu")
                }
            }
        )
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
