package com.example.kochraj.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kochraj.R
import com.example.kochraj.ui.theme.Aztec
import com.example.kochraj.widgets.CarouselItem
import com.example.kochraj.widgets.GridCard
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavHostController) {
    val carouselImages = arrayListOf(
        R.drawable.aa,
        R.drawable.bb,
        R.drawable.cc,
        R.drawable.dd,
    )

    val gridImages = arrayListOf(
        R.drawable.mercury,
        R.drawable.venus,
        R.drawable.earth,
        R.drawable.mars,
        R.drawable.jupiter,
        R.drawable.saturn,
        R.drawable.uranus,
        R.drawable.neptune
    )

    val pagerState = rememberPagerState { carouselImages.size }
    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % carouselImages.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Aztec),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Hello Deepak",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp, top = 25.dp, end = 16.dp)
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    CarouselItem(imageResId = carouselImages[page], index = page)
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(carouselImages.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .size(8.dp)
                                .background(color, CircleShape)
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Explore our culture",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp, top = 10.dp, end = 16.dp, bottom = 16.dp)
            )
        }

        // Grid items directly in the LazyColumn instead of nested grid
        gridItems(gridImages, 2) { index, imageResId ->
            GridCard(
                title = "Item ${index + 1}",
                imageResId = imageResId,
                onClick = { }
            )
        }
    }
}


// Extension function to create grid-like items in LazyColumn
fun LazyListScope.gridItems(
    items: List<Int>,
    columns: Int,
    content: @Composable (index: Int, item: Int) -> Unit
) {
    val itemCount = items.size
    val rows = (itemCount + columns - 1) / columns // Calculate rows needed

    items(rows) { rowIndex ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            for (columnIndex in 0 until columns) {
                val itemIndex = rowIndex * columns + columnIndex
                if (itemIndex < itemCount) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        content(itemIndex, items[itemIndex])
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

