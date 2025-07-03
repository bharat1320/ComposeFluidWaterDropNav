package com.bharat1320.fluidwaterdropnav.sample

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bharat1320.fluidwaterdropnav.DefaultWaterDropBottomNavItems
import com.bharat1320.fluidwaterdropnav.WaterDropBottomNav
import com.bharat1320.fluidwaterdropnav.sample.ui.theme.FluidWaterDropNavProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FluidWaterDropNavProjectTheme { // Use your app's theme
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SampleAppContent(innerPadding)
                }
            }
        }
    }
}

@Composable
fun SampleAppContent(innerPadding: PaddingValues) {
    var selectedIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = innerPadding.calculateTopPadding())
    ) {
        Text(
            text = "Content for ${DefaultWaterDropBottomNavItems[selectedIndex].title}",
            modifier = Modifier.align(Alignment.Center)
        )

        Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
            WaterDropBottomNav(
                items = DefaultWaterDropBottomNavItems,
                initialSelectedIndex = selectedIndex,
                accentColor = Color(0xFF6200EE),
                onItemSelected = { index, item ->
                    selectedIndex = index
                    android.util.Log.d("FluidNavSample", "Selected: ${item.title} at index $index")
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FluidWaterDropNavProjectTheme {
        SampleAppContent(PaddingValues(0.dp))
    }
}