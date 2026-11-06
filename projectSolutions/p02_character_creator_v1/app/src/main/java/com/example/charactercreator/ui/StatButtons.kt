package com.example.charactercreator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charactercreator.ui.theme.CharacterCreatorTheme

@Composable
fun StatButtons(
    stats: Map<Stat, Int>,
    onStatChange: (Stat, Int) -> Unit,
    modifier: Modifier = Modifier
) {

        Column(modifier = modifier.padding(8.dp)) {
            Stat.entries.forEach { stat ->
                StatUpdater(
                    stat = stat,
                    value = stats[stat] ?: 0,
                    onPlusClick = {
                        onStatChange(stat, 1)
                    },
                    onMinusClick = {
                        onStatChange(stat, -1)
                    }
                )
            }
        }
}

@Composable
fun StatUpdater(
    stat: Stat,
    value: Int,
    onPlusClick: () -> Unit,
    onMinusClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.padding(4.dp)
    ){

    Row(
        modifier = modifier.fillMaxWidth().padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = stat.iconId),
                contentDescription = stat.name
            )
            Text(
                text = stat.name,
                //fontSize = 24.sp,
                modifier = modifier.padding(start = 4.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onPlusClick,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_up),
                    contentDescription = "${stat.name} up"
                )
            }

            Text(
                text = "$value",
                //fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 4.dp).width(32.dp)
            )

            Button(onClick = onMinusClick) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_down),
                    contentDescription = "${stat.name} down"
                )
            }
        }
    }
    }
}

@Preview(showBackground = true)
@Composable
fun StatUpdaterPreview() {
    CharacterCreatorTheme() {
        StatButtons(
            stats = Stat.entries.associateWith { 0 },
            onStatChange = { stat, value -> },
            modifier = Modifier.fillMaxSize()
        )
    }
}