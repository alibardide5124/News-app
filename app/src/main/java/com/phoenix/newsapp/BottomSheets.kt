package com.phoenix.newsapp

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.phoenix.newsapp.data.model.Article
import java.text.SimpleDateFormat
import java.util.*

object BottomSheets {

    @Composable
    fun BottomSheetContent(
        article: Article,
        uiState: NewsSheetUiState,
        onClickSave: () -> Unit
    ) {
        val uriHandler = LocalUriHandler.current
        val context = LocalContext.current
        val clipboardManager = LocalClipboardManager.current

        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.urlToImage)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = article.description,
                modifier = Modifier
                    .aspectRatio(16 / 9f)
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = article.title,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (article.source.name != null)
                        Text(
                            text = "from ${article.source.name}",
                            fontSize = 12.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    if (article.source.name != null && article.author != null)
                        Text(text = ", ", fontSize = 12.sp)
                    if (article.author != null)
                        Text(
                            text = "by ${article.author}",
                            fontSize = 12.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = convertDateToDesiredFormatString(article.publishedAt),
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    onClick = { uriHandler.openUri(article.url) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Open in browser")
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { onClickSave() }) {
                    if (uiState.savedState == SavedState.Loading)
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    else
                        Icon(
                            if (uiState.savedState == SavedState.Saved)
                                painterResource(id = R.drawable.ic_bookmark)
                            else
                                painterResource(id = R.drawable.ic_bookmark_outline),
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp),
                            tint = if (uiState.savedState == SavedState.NotSaved)
                                MaterialTheme.colorScheme.onBackground
                            else
                                MaterialTheme.colorScheme.primary
                        )
                }
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, article.url)
                    }
                    ContextCompat.startActivity(
                        context,
                        Intent.createChooser(intent, "Share With"),
                        null
                    )
                }) {
                    Icon(Icons.Default.Share, contentDescription = null)
                }
                IconButton(onClick = {
                    clipboardManager.setText(AnnotatedString(article.url))
                    Toast.makeText(context, "Link copied!", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        painterResource(id = R.drawable.ic_copy),
                        contentDescription = null,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${article.description}\n${article.content}",
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    private fun convertDateToDesiredFormatString(date: String): String {
        val formatDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .parse(date.replace("T", " ").replace("Z", ""))

        return SimpleDateFormat("MMM dd,yyyy HH:mm", Locale.getDefault())
            .format(formatDate!!)
    }

    data class NewsSheetUiState(
        val savedState: SavedState = SavedState.Loading,
    )

    enum class SavedState {
        Loading, Saved, NotSaved
    }

}