package com.example.charactercreator

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun CharacterTextEntry(
    creator: CharacterCreatorState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        EnterTextField(
            inputText = creator.name,
            onValueChange = onNameChange,
            labelId = R.string.label_name,
            iconId = R.drawable.name,
            modifier = Modifier.fillMaxWidth()
        )

        // enter char description
        EnterTextField(
            inputText = creator.description,
            onValueChange = onDescriptionChange,
            labelId = R.string.label_description,
            iconId = R.drawable.description,
            modifier = Modifier.fillMaxWidth()
        )


        // optional: Image button or placeholder
    }
}

@Composable
fun EnterTextField(
    inputText: String,
    onValueChange: (String) -> Unit,
    @StringRes labelId: Int,
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier
) {
    TextField(
        value = inputText,
        onValueChange = onValueChange,   // newInput is the parameter passed

        label = { Text(stringResource(id = labelId)) },//,
        //singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null
            )
        },
        maxLines = 2,
        modifier = modifier
    )
}

@Composable
fun CharacterTextEntryScreen(
    modifier: Modifier = Modifier,
){
    CharacterTextEntry(
        creator = CharacterCreatorState(),
        onNameChange = {},
        onDescriptionChange = {}
    )
}

@Preview
@Composable
fun CharacterTextEntryPreview() {
    CharacterTextEntryScreen(
        modifier = Modifier.fillMaxSize()
    )
}