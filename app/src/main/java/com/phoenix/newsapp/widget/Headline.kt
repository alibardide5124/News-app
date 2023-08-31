package com.phoenix.newsapp.widget

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.phoenix.newsapp.R
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.data.model.Source

@Composable
fun Headline(
    article: Article,
    onClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        val (imageRef, titleRef, authorRef) = createRefs()
        val imageGuideline = createGuidelineFromStart(.3f)

        val imagePainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.urlToImage)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop
            )
        val transition = rememberInfiniteTransition(label = "")
        val colorAnimation by transition.animateColor(
            initialValue = MaterialTheme.colorScheme.surfaceVariant,
            targetValue = MaterialTheme.colorScheme.outline,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 500,
                    delayMillis = 250
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = ""
        )

        Box(
            modifier = Modifier
                .constrainAs(imageRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(imageGuideline)
                    width = Dimension.fillToConstraints
                }
                .aspectRatio(4 / 3f)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (imagePainter.state is AsyncImagePainter.State.Error)
                    Icon(
                        painterResource(R.drawable.ic_image),
                        null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(8.dp)
                    )

            Image(
                painter = imagePainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        when (imagePainter.state) {
                            is AsyncImagePainter.State.Loading -> colorAnimation
                            else -> Color.Transparent
                        }
                    )
            )
        }
        Text(
            text = article.title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
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
    }
}

@Preview(showBackground = true)
@Composable
private fun HeadlinePreview() {
    val article = Article(
        0,
        "Jaclyn DeJohn",
        "",
        "",
        "2022-08-26",
        Source(null, null),
        "NVIDIA Stock is a Winding Up for a Record Setting Second Half",
        "www.example.com",
        "www.example.com/image"
    )
    Headline(article) {}
}