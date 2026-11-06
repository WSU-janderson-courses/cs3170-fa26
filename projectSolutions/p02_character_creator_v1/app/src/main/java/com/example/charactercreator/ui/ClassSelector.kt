package com.example.charactercreator.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.charactercreator.Stat
import com.example.charactercreator.model.CharacterClass
import com.example.charactercreator.ui.theme.CharacterCreatorTheme
import androidx.compose.foundation.lazy.grid.items
import androidx.constraintlayout.helper.widget.Grid

@Composable
fun ClassSelector(
    selectedClass: CharacterClass,
    onClassChange: (CharacterClass) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ){

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
        ) {
            items(CharacterClass.entries) { characterClass ->
                ClassSelectionRow(
                    name = stringResource(characterClass.nameResId),
                    colorId = characterClass.frameColorResId,
                    iconId = characterClass.iconResId,
                    selected = selectedClass == characterClass,
                    onClick = { onClassChange(characterClass) }
                )
            }
        }
    }

}

@Composable
fun ClassSelectionRow(
    name: String,
    colorId: Int,
    iconId: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .width(166.dp)
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        )
        {
            RadioButton(
                selected = selected,
                onClick = onClick
            )
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "${name} icon",
                tint = colorResource(colorId),
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(
                text = name,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClassSelectorPreview() {
    CharacterCreatorTheme() {
        ClassSelector(
            selectedClass = CharacterClass.ROGUE,
            onClassChange = {}
        )
    }
}