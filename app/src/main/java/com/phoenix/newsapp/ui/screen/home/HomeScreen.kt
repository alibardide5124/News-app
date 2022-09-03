package com.phoenix.newsapp.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.phoenix.newsapp.R
import com.phoenix.newsapp.ui.screen.Screen
import com.phoenix.newsapp.ui.widget.ListComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier,
        topBar = { HomeTopBar(navController) }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            Box(
                modifier = Modifier
                    .height(6.dp)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x21212121),
                                Color.Transparent
                            )
                        )
                    )
            )
            ListComposable(items = viewModel.getTopHeadlines())
        }
    }
}

@Composable
private fun HomeTopBar(navController: NavHostController) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.navigate(Screen.About.route) }) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null
            )
        }
        Text(
            text = stringResource(R.string.app_name),
            color = Color(0xDE000000),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = { navController.navigate(Screen.Search.route) }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomePreview() {
    val navController = rememberNavController()
    HomeScreen(navController)
}