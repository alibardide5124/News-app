package com.phoenix.newsapp.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.phoenix.newsapp.R

@Composable
fun Headline() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        val (imageRef, titleRef, authorRef, strokeRef) = createRefs()
        val imageGuideline = createGuidelineFromStart(.3f)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://example.com/image.jpg")
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
                    end.linkTo(imageGuideline)
                    bottom.linkTo(authorRef.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        )
        Text(
            text = "NVIDIA Stock is a Winding Up for a Record Setting Second Half",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top, 4.dp)
                start.linkTo(imageRef.end, 12.dp)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
        Text(
            text = "Jaclyn DeJohn",
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
//@Composable
//fun Headline() {
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Row(modifier = Modifier.fillMaxWidth()) {
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data("https://example.com/image.jpg")
//                    .crossfade(true)
//                    .build(),
//                placeholder = painterResource(R.drawable.placeholder),
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth(0.3f)
//                    .clip(RoundedCornerShape(8.dp))
//                    .aspectRatio(18 / 9f)
//            )
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        Spacer(modifier = Modifier
//            .height(2.dp)
//            .background(Color.Black))
//    }
//}

@Preview(showBackground = true)
@Composable
private fun HeadlinePreview() {
    Headline()
}