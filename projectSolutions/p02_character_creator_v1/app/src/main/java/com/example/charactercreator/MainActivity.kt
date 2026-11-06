package com.example.charactercreator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charactercreator.model.CharacterClass
import com.example.charactercreator.ui.ClassSelector
import com.example.charactercreator.ui.theme.CharacterCreatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CharacterCreatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CharacterCreatorApp(
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CharacterCreatorApp(modifier: Modifier = Modifier) {
    var creator by remember {
        mutableStateOf(CharacterCreatorState())
    }

    // TODO: Create the character creator interface

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(8.dp)
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp,

        )
        CharacterTextEntry(
            creator = creator,
            onNameChange = { newName: String ->
                creator = creator.copy(name = newName)
            },
            onDescriptionChange = { newDescription: String ->
                creator = creator.copy(description = newDescription)
            },
        )

        ClassSelector(
            selectedClass = creator.characterClass,
            onClassChange = {newClass: CharacterClass ->
                creator = creator.copy(characterClass = newClass)
            }
        )

        StatButtons(
            stats = creator.stats,
            onStatChange = {stat, delta -> creator = creator.updateStats(stat, delta)}
        )

        Text("Remaining Points: ${creator.remainingPoints}/${CharacterCreatorState.MAX_POINTS}")

        AttributeList(
            character = creator
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterCreatorAppPreview() {
    CharacterCreatorTheme(
        darkTheme = false,
    ) {
        CharacterCreatorApp(
            modifier = Modifier.fillMaxSize()
        )
    }
}