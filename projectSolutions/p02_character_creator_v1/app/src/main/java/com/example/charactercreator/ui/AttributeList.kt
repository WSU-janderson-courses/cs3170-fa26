package com.example.charactercreator

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun AttributeList(
    character: CharacterCreatorState,
    modifier: Modifier = Modifier
) {
    //val statList = attributes.map{ (key, value) -> key to value}

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 64.dp)
    ) {
            AttributeRow("Power", character.power, attributeIconId = R.drawable.power)
        AttributeRow("Toughness", character.toughness, attributeIconId = R.drawable.toughness)
        AttributeRow("Cost", character.cost, attributeIconId = R.drawable.cost)

    }
}

@Composable
fun AttributeRow(
    attribute: String,
    value: Int,
    @DrawableRes attributeIconId: Int,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier.padding(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        ) {
            Icon(
                painterResource(id = attributeIconId),
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = "${attribute}:", modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(text = "$value")
        }
    }
}

@Preview(showBackground = true)
@Composable fun AttributeListPreview() {
    AttributeList(
        character = CharacterCreatorState()
    )
}