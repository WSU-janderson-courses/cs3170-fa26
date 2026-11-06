package com.example.firstandroidapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstandroidapp.ui.theme.FirstAndroidAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstAndroidAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WelcomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(color = Cyan)
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Welcome to CS 3170!",
            fontSize = 32.sp
        )
        Text(
            text = "Name: James",
            fontSize = 32.sp
        )

        Text(
            text = "Major: BSCEG",
            fontSize = 32.sp
        )
        
        Text(
            text = "Reddit"
        )
    }
}

@Preview(
    name = "Phone",
    showBackground = true,
    showSystemUi = true,
    device = "id:pixel_6"
    )
@Composable
fun WelcomeScreenPreview() {
    FirstAndroidAppTheme {
        WelcomeScreen()
    }
}