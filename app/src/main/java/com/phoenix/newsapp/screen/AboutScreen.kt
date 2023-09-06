package com.phoenix.newsapp.screen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phoenix.newsapp.NavGraphDestination
import com.phoenix.newsapp.R
import com.phoenix.newsapp.components.HyperlinkText
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Destination(route = NavGraphDestination.ABOUT_ROUTE)
@Composable
fun AboutScreen(
    navigator: DestinationsNavigator
) {
    val activity = LocalContext.current as Activity
    val windowWidthSizeClass = calculateWindowSizeClass(activity).widthSizeClass
    val uriHandler = LocalUriHandler.current

    if (windowWidthSizeClass == WindowWidthSizeClass.Compact)
        CompactAboutScreen(
            onClickBack = { navigator.navigateUp() },
            onClickLinkedin = { uriHandler.openUri("https://linkedin.com/in/alibardide5124") },
            onClickInstagram = { uriHandler.openUri("https://instagram.com/alibardide.5124") },
            onClickGithub = { uriHandler.openUri("https://github.com/alibardide5124") }
        )
    else
        ExpandedAboutScreen(
            onClickBack = { navigator.navigateUp() },
            onClickLinkedin = { uriHandler.openUri("https://linkedin.com/in/alibardide5124") },
            onClickInstagram = { uriHandler.openUri("https://instagram.com/alibardide.5124") },
            onClickGithub = { uriHandler.openUri("https://github.com/alibardide5124") }
        )
}

@Composable
private fun CompactAboutScreen(
    onClickBack: () -> Unit,
    onClickLinkedin: () -> Unit,
    onClickInstagram: () -> Unit,
    onClickGithub: () -> Unit,
) {
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
                    .clip(RoundedCornerShape(percent = 50))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Spacer(modifier = Modifier.height(24.dp))
            HyperlinkText(
                text = stringResource(R.string.about_desc),
                modifier = Modifier.padding(horizontal = 24.dp),
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
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onClickLinkedin() }
                        .padding(8.dp)
                )
                Image(
                    painter = painterResource(R.drawable.ic_instagram),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onClickInstagram() }
                        .padding(8.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_github),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onClickGithub() }
                        .padding(8.dp)
                )
            }
        }
        IconButton(onClick = { onClickBack() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun ExpandedAboutScreen(
    onClickBack: () -> Unit,
    onClickLinkedin: () -> Unit,
    onClickInstagram: () -> Unit,
    onClickGithub: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_person),
                    contentDescription = null,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                        .clip(RoundedCornerShape(percent = 50))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
                Spacer(modifier = Modifier.width(24.dp))
                HyperlinkText(
                    text = stringResource(R.string.about_desc),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    linkText = listOf("newsapi.org"),
                    hyperlinks = listOf("https://newsapi.org")
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Image(
                    painter = painterResource(R.drawable.ic_linked),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onClickLinkedin() }
                        .padding(8.dp)
                )
                Image(
                    painter = painterResource(R.drawable.ic_instagram),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onClickInstagram() }
                        .padding(8.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_github),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onClickGithub() }
                        .padding(8.dp)
                )
            }
        }
        IconButton(onClick = { onClickBack() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
            )
        }
    }
}
