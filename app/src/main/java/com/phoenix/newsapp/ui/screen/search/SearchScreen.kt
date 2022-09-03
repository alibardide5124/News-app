package com.phoenix.newsapp.ui.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.phoenix.newsapp.R
import com.phoenix.newsapp.ui.widget.ListComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery = viewModel.searchQuery
    val searchedArticles = viewModel.searchedArticles

    var isSearched by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier,
        topBar = {
            SearchTopAppBar(
                searchQuery,
                onTextChange = {
                    viewModel.updateSearchQuery(it)
                },
                onSearchClicked = {
                    viewModel.searchArticles(it)
                    isSearched = true
                },
                onCloseClicked = {
                    navController.popBackStack()
                }
            )
        }
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
            AnimatedVisibility(visible = isSearched.not(), enter = fadeIn(), exit = fadeOut()) {
                EmptySearchIllustration()
            }
            AnimatedVisibility(visible = isSearched, enter = fadeIn(), exit = fadeOut()) {
                ListComposable(items = searchedArticles)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    onCloseClicked: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 6.dp, horizontal = 8.dp),
        value = text,
        onValueChange = { string: String ->
            onTextChange(string)
        },
        placeholder = {
            Text(
                text = "Search in the news...",
                modifier = Modifier.alpha(0.54f),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                Modifier.alpha(0.54f)
            )
        },
        trailingIcon = {
            IconButton(onClick = { onCloseClicked() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchClicked(text) }),
        textStyle = TextStyle(
            color = Color(0xDE000000),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun EmptySearchIllustration() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.search),
            contentDescription = null,
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Search in the news",
            color = Color(0xDE000000),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Search among the thousands of news!\nfrom many different sources",
            color = Color(0x8A000000),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomePreview() {
    val navController = rememberNavController()
    SearchScreen(navController)
}