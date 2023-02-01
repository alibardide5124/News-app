package com.phoenix.newsapp.ui.screen.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.phoenix.newsapp.R
import com.phoenix.newsapp.ui.widget.HyperlinkText

@Composable
fun AboutScreen(navController: NavHostController) {
    val uriHandler = LocalUriHandler.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_person),
                contentDescription = null,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            HyperlinkText(
                text = stringResource(R.string.about_desc),
                modifier = Modifier.padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                linkText = listOf("newsapi.org"),
                hyperlinks = listOf("https://newsapi.org")
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Image(
                    painter = painterResource(R.drawable.ic_linked),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { uriHandler.openUri("https://linkedin.com/in/alibardide5124") }
                        .padding(8.dp)
                )
                Image(
                    painter = painterResource(R.drawable.ic_instagram),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { uriHandler.openUri("https://instagram.com/alibardide.5124") }
                        .padding(8.dp)
                )
                Image(
                    painter = painterResource(R.drawable.ic_github),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { uriHandler.openUri("https://github.com/alibardide5124") }
                        .padding(8.dp)
                )
            }
        }
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AboutPreview() {
    val navController = rememberNavController()
    AboutScreen(navController)
}