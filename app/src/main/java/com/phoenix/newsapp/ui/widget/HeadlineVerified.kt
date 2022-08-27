package com.phoenix.newsapp.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.phoenix.newsapp.R
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.data.model.Source

@Composable
fun HeadlineVerified(article: Article) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .clip(RoundedCornerShape(8.dp))
            .shadow(1.dp, shape = RoundedCornerShape(8.dp))
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth().aspectRatio(18 / 9f)) {
            val (imageRef, shadowRef, titleRef, authorRef) = createRefs()
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.urlToImage)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .constrainAs(imageRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            )
            Box(
                modifier = Modifier
                    .constrainAs(shadowRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black
                            )
                        )
                    )
            )
            Text(
                text = article.title,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(titleRef) {
                        bottom.linkTo(authorRef.top, 4.dp)
                        start.linkTo(parent.start, 12.dp)
                        end.linkTo(parent.end, 12.dp)
                        width = Dimension.fillToConstraints
                    }
            )
            if (article.author != null) {
                Text(
                    text = article.author,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .alpha(.54f)
                        .constrainAs(authorRef) {
                            bottom.linkTo(parent.bottom, 16.dp)
                            start.linkTo(parent.start, 20.dp)
                            end.linkTo(parent.end, 12.dp)
                            width = Dimension.fillToConstraints
                        }
                )
            } else if (article.source.name != null) {
                Text(
                    text = article.source.name,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .alpha(.54f)
                        .constrainAs(authorRef) {
                            bottom.linkTo(parent.bottom, 16.dp)
                            start.linkTo(parent.start, 20.dp)
                            end.linkTo(parent.end, 12.dp)
                            width = Dimension.fillToConstraints
                        }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HeadlineVerifiedPreview() {
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
    HeadlineVerified(article)
}