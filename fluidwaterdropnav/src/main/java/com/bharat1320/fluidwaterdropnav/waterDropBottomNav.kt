package com.bharat1320.fluidwaterdropnav

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlinx.coroutines.launch

data class BottomNavItem(
    val title: String? = null,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
    val contentDescription: String? = title // Default content description
)

// Default list as a public constant for easy use, but allow override
val DefaultWaterDropBottomNavItems = listOf(
    BottomNavItem("Home", Icons.Outlined.Home, Icons.Default.Home),
    BottomNavItem("Calendar", Icons.Outlined.DateRange, Icons.Default.DateRange), // Renamed from DateRange
    BottomNavItem("Favorite", Icons.Outlined.FavoriteBorder, Icons.Default.Favorite),
    BottomNavItem("Email", Icons.Outlined.Email, Icons.Default.Email),
    BottomNavItem("Profile", Icons.Outlined.AccountCircle, Icons.Default.AccountCircle), // Renamed from AccountCircle
)

@Preview
@Composable
fun WaterDropBottomNav(
    // 1. Navigation items
    items: List<BottomNavItem> = DefaultWaterDropBottomNavItems,

    // 2. Colors
    accentColor: Color = Color(0xFFa476ff), // Main accent color
    backgroundColor: Color = Color.White,

    // 3. Sizes
    height: Dp = 64.dp,
    iconSize: Dp = 24.dp, // Size of the actual icons within their space
    bottomPaddingForDrop: Float = 4f, // Padding for the small circle inside the bubble
    initialIconScale: Float = 0.4f, // Start scale for pop-in animation

    // 4. Animation timings & specs
    animationDurationMillis: Int = 300,
    bubbleExpandDurationMillis: Int = 280, // For the water bubble height animation
    outlinedToFilledIconAnimDurationMillis: Int = 80,

    bubbleContractDampingRatio: Float = DampingRatioMediumBouncy,
    bubbleContractStiffness: Float = 300f,
    iconPopInDampingRatio: Float = DampingRatioMediumBouncy,
    iconPopInStiffness: Float = 200f,

    // 5. Initial selection (optional, useful if you want to control it externally)
    initialSelectedIndex: Int = 0,

    // 6. Callback for selection changes
    onItemSelected: (index: Int, item: BottomNavItem) -> Unit = { _, _ -> }
) {
    require(items.isNotEmpty()) { "Items list cannot be empty" }
    require(initialSelectedIndex in items.indices) {
        "Initial selected index must be within items range"
    }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val iconWidth = screenWidth / items.size
    val coroutineScope = rememberCoroutineScope()

    var elementSelectedPos by remember { mutableStateOf(initialSelectedIndex) }
    var currentElementSelectedPos by remember { mutableStateOf(0) }
    var lastElementSelectedPos by remember { mutableStateOf(-1) }

    var animateWaterBubble by remember { mutableStateOf(false) } // Controls water bubble height/width animation
    val waterDropBottomPadding = remember { Animatable(bottomPaddingForDrop) }

    val animateStartPadding by animateDpAsState(
        targetValue = currentElementSelectedPos * iconWidth,
        animationSpec = tween(
            durationMillis = animationDurationMillis,
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            coroutineScope.launch {
                waterDropBottomPadding.apply {
                    animateTo(
                        targetValue = 40f,
                        animationSpec = tween(
                            durationMillis = bubbleExpandDurationMillis,
                            easing = FastOutSlowInEasing
                        ),
                    )
                    // After water bubble animation completes, trigger the new icon's pop-in animation
                    elementSelectedPos = currentElementSelectedPos
                    snapTo(bottomPaddingForDrop)
                }
            }
        }
    )

    val waterBubbleDefaultWidth = iconWidth - 22.dp
    val animatedWidth by animateFloatAsState(
        targetValue = if (animateWaterBubble) 0.86f else 1f,
        animationSpec = spring(
            dampingRatio = bubbleContractDampingRatio,
            stiffness = bubbleContractStiffness
        ),
        finishedListener = {
            animateWaterBubble = false
        }
    )
    val animatedHeight by animateDpAsState(
        targetValue = if (animateWaterBubble) 58.dp else 46.dp,
        animationSpec = spring(
            dampingRatio = bubbleContractDampingRatio,
            stiffness = bubbleContractStiffness
        )
    )

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .background(backgroundColor)
        .padding(top = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .height(iconSize)
                .align(Alignment.TopStart)
        ) {
            items.mapIndexed { index, item ->
                val filledIconAlpha = remember(index) { Animatable(0f) }
                val filledIconScale = remember(index) { Animatable(1f) }
                val outlinedIconAlpha = remember(index) { Animatable(1f) }

                if(index == lastElementSelectedPos) {
                    // this should be in coroutines because it needs to run
                    // even when index would have changed the value
                    // LaunchedEffect will simply cancel it
                    coroutineScope.launch {
                        outlinedIconAlpha.snapTo(1f)
                        filledIconAlpha.animateTo(0f, tween(outlinedToFilledIconAnimDurationMillis))
                    }
                }

                if(index == elementSelectedPos) {
                    // this should be launched effect cuz it should only run on updated value of index
                    LaunchedEffect(elementSelectedPos) {
                        launch {
                            filledIconAlpha.apply {
                                snapTo(0f)
                                animateTo(
                                    targetValue = 1f,
                                    animationSpec = tween(
                                        durationMillis = animationDurationMillis,
                                    )
                                )
                            }
                        }
                        launch {
                            filledIconScale.apply {
                                snapTo(initialIconScale)
                                animateTo(
                                    targetValue = 1f,
                                    animationSpec = spring(dampingRatio = iconPopInDampingRatio, stiffness = iconPopInStiffness)
                                )
                            }
                        }
                        launch {
                            outlinedIconAlpha.apply {
                                snapTo(1f)
                                animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(
                                        durationMillis = animationDurationMillis,
                                    )
                                )
                            }
                        }
                    }
                }

                Box(Modifier
                    .size(iconSize)
                    .weight(1f)
                    .clickable {
                        lastElementSelectedPos = elementSelectedPos
                        animateWaterBubble = true
                        currentElementSelectedPos = index
                        onItemSelected(index, item)
                    }
                ) {
                    Icon(
                        imageVector = item.filledIcon,
                        contentDescription = item.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                alpha = filledIconAlpha.value
                                scaleY = filledIconScale.value
                                scaleX = filledIconScale.value
                            },
                        tint = accentColor,
                    )

                    Icon(
                        imageVector = item.outlinedIcon,
                        contentDescription = item.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                alpha = outlinedIconAlpha.value
                            },
                        tint = accentColor,
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(start = animateStartPadding)
                .align(Alignment.BottomStart)
                .size(iconWidth)
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = waterDropBottomPadding.value.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .align(Alignment.BottomCenter)
                    .background(accentColor)
            )

            Canvas(
                modifier = Modifier
                    .width(waterBubbleDefaultWidth)
                    .height(animatedHeight)
                    .align(Alignment.BottomCenter)
                    .graphicsLayer {
                        scaleX = animatedWidth
                    }
            ) {
                val h = size.height
                val w = size.width

                drawPath(
                    path = Path().apply {
                        moveTo(0f * w, 1.0027715f * h)
                        cubicTo(
                            0.25813386f * w, 0.9052535f * h,
                            0.39711207f * w, 0.621295f * h,
                            0.5513535f * w, 0.7207979f * h
                        )
                        cubicTo(
                            0.7231695f * w, 0.83163834f * h,
                            0.78498775f * w, 0.9030204f * h,
                            0.9972351f * w, 1.0009656f * h,
                        )
                    },
                    color = accentColor,
                    style = Fill
                )
            }
        }
    }
}