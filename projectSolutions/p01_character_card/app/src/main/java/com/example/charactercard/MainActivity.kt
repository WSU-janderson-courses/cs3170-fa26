package com.example.charactercard

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.charactercard.ui.theme.CharacterCardTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CharacterCardTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val character = Character(
                        name = stringResource(R.string.drinky_name),
                        cost = integerResource(id = R.integer.castingCost),
                        characterClass = CharacterClass.DRINKY,
                        artworkResId = R.drawable.drinky,
                        description = stringResource(R.string.drinky_description),
                        power = integerResource(id = R.integer.powerStat),
                        toughness = integerResource(id = R.integer.toughnessStat),
                        characterIconResId = R.drawable.bolt
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CharacterCard(
                            character = character,
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CardHeader(
    title: String,
    cost: Int,
    @DrawableRes characterIconResId: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black)
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$cost",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Icon(
                painter = painterResource(characterIconResId),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun CharacterClassLine(
    className: String,
    @DrawableRes classIconResId: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black)
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = className,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )

        Icon(
            painter = painterResource(classIconResId),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
    }
}


@Composable
fun CardDetails(
    description: String,
    power: Int,
    toughness: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black)
            .background(Color(0xFFF2EBDC))
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            modifier = Modifier.padding(
                start = 12.dp,
                end = 12.dp,
                top = 10.dp,
                bottom = 42.dp
            )
        )

        Text(
            text = "$power/$toughness",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(6.dp)
                .border(
                    1.dp,
                    Color.Black,
                    RoundedCornerShape(4.dp)
                )
                .background(
                    Color(0xFFE0D5BE),
                    RoundedCornerShape(4.dp)
                )
                .padding(
                    horizontal = 14.dp,
                    vertical = 4.dp
                )
        )
    }
}



@Composable
fun CharacterCard(
    character: Character,
    modifier: Modifier = Modifier
) {
    val frameColor = colorResource(
        character.characterClass.frameColorResId
    )

    Column(
        modifier = modifier
            .widthIn(max = 340.dp)
            .fillMaxWidth()
            .aspectRatio(5f / 7f)
            .clip(RoundedCornerShape(12.dp))
            .background(frameColor)
            .border(
                3.dp,
                Color.Black,
                RoundedCornerShape(12.dp)
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        CardHeader(
            title = character.name,
            cost = character.cost,
            characterIconResId = character.characterIconResId
        )

        Image(
            painter = painterResource(character.artworkResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
                .border(2.dp, Color.Black)
        )

        CharacterClassLine(
            className = stringResource(
                character.characterClass.nameResId
            ),
            classIconResId = character.characterClass.iconResId
        )

        CardDetails(
            description = character.description,
            power = character.power,
            toughness = character.toughness,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun CharacterCardApp(modifier: Modifier = Modifier) {
        val character = Character(
            name = stringResource(R.string.drinky_name),
            cost = 5,
            characterClass = CharacterClass.DRINKY,
            artworkResId = R.drawable.drinky,
            description = stringResource(R.string.drinky_description),
            power = 3,
            toughness = 2,
            characterIconResId = R.drawable.bolt
        )

        CharacterCard(
            character = character,
            modifier = modifier
        )
}

@Preview(showBackground = true)
@Composable
fun CharacterCardPreview() {
    CharacterCardTheme {
        CharacterCardApp()

    }
}