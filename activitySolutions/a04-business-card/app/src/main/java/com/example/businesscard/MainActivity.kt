package com.example.businesscard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.businesscard.ui.theme.BusinessCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BusinessCardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BusinessCardApp(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun BusinessCardApp(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(colorResource(R.color.wright_state_green))
            .padding(24.dp),

        // TODO: Arrange the two sections vertically.
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileSection(
            nameId = R.string.name,
            titleId = R.string.title
        )

        ContactSection()
    }
}

@Composable
fun ProfileSection(
    @StringRes nameId: Int,
    @StringRes titleId: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,

        // TODO: Center the items horizontally.
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(R.drawable.img_0035),
            contentDescription = stringResource(R.string.android_logo_description),

            // TODO: Give the image an appropriate size.
            alignment = BiasAlignment(horizontalBias = 0.0f, verticalBias = -0.5f),
            contentScale = ContentScale.Crop,
            modifier = Modifier.aspectRatio(1f)
        )

        Text(
            text = stringResource(nameId),

            // TODO: Customize the name's font size and appearance.
            fontSize = 42.sp,
            color = colorResource(id = R.color.wright_state_gold)

        )

        Text(
            text = stringResource(titleId),

            // TODO: Customize the title's appearance.
            fontSize = 24.sp,
            color = colorResource(id = R.color.wright_state_gold)
        )
    }
}

@Composable
fun ContactSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        ContactRow(
            icon = Icons.Default.Call,
            textId = R.string.number
        )

        // TODO: Add rows for the social handle and email address.
        ContactRow(
            icon = Icons.Default.LocationOn,
            textId = R.string.social,
        )

        ContactRow(
            icon = Icons.Default.Email,
            textId = R.string.email,
        )
    }
}

@Composable
fun ContactRow(
    icon: ImageVector,
    @StringRes textId: Int,
    modifier: Modifier = Modifier
) {
    Row(

        // TODO: Vertically align the icon and text.
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorResource(id = R.color.wright_state_gold)

        )

        Text(
            text = stringResource(textId),

            // TODO: Add space between the icon and text.
            modifier = Modifier.padding(start = 16.dp),
            color = colorResource(id = R.color.wright_state_gold)

        )
    }
}

@Preview(showBackground = true)
@Composable
fun BusinessCardPreview() {
    BusinessCardTheme {
        BusinessCardApp(modifier = Modifier.fillMaxSize())
    }
}