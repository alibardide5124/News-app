package com.phoenix.newsapp.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.phoenix.newsapp.R
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.data.model.Source

@Composable
fun Headline(article: Article) {
    val uriHandler = LocalUriHandler.current
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { uriHandler.openUri(article.url) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        val (imageRef, titleRef, authorRef, strokeRef) = createRefs()
        val imageGuideline = createGuidelineFromStart(.3f)

        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.urlToImage)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .constrainAs(imageRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(imageGuideline)
                    bottom.linkTo(authorRef.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is AsyncImagePainter.State.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFCDCDCD))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(painterResource(R.drawable.ic_image), null, tint = Color(0xFF212121))
                    }
                }
                else -> SubcomposeAsyncImageContent()
            }
        }
        Text(
            text = article.title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top, 4.dp)
                start.linkTo(imageRef.end, 12.dp)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
        if (article.author != null) {
            Text(
                text = article.author,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .alpha(.54f)
                    .constrainAs(authorRef) {
                        top.linkTo(titleRef.bottom, 4.dp)
                        start.linkTo(imageRef.end, 16.dp)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
            )
        } else if (article.source.name != null) {
            Text(
                text = article.source.name,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .alpha(.54f)
                    .constrainAs(authorRef) {
                        top.linkTo(titleRef.bottom, 4.dp)
                        start.linkTo(imageRef.end, 16.dp)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
            )
        }
        Spacer(
            modifier = Modifier
                .background(Color.Black)
                .constrainAs(strokeRef) {
                    top.linkTo(imageRef.bottom, 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.preferredValue(4.dp)
                }
                .clip(RoundedCornerShape(2.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HeadlinePreview() {
    val article = Article(
        "Jaclyn DeJohn",
        "",
        "",
        "2022-08-26",
        Source(null, null),
        "NVIDIA Stock is a Winding Up for a Record Setting Second Half",
        "www.example.com",
        "www.example.com/image"
    )
    Headline(article)
}