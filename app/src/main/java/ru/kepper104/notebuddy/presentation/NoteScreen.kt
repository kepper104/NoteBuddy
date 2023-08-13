package ru.kepper104.notebuddy.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.kepper104.notebuddy.domain.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    noteViewModel: NoteViewModel = viewModel()
) {
    if (!noteViewModel.mainState.isEditing){
        MainView()
    }else{
        EditView()
    }
}

@Composable
fun NoteComposable(note: Note) {
    Column{
        Divider()
        Text(
            text = note.title,
            fontSize = 40.sp
        )
        Text(
            text = note.text,
            fontSize = 30.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    noteViewModel: NoteViewModel = viewModel()
){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                noteViewModel.createNote()
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add a note")
            }
        }
    ) {paddingValues ->
        Column {
            Text(text = "My Notes")
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ){
                for (note in noteViewModel.mainState.notes){
                    item {
                        NoteComposable(note = note)
                    }
                }
            }
        }


    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditView(
    noteViewModel: NoteViewModel = viewModel()
){
    var colorsMenuExpanded by remember {
        mutableStateOf(false)
    }

    var colorFieldSize by remember {
        mutableStateOf(Size.Zero)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = "Save the note")
            }
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Text(text = "Edit a note")

            TextField(
                value = noteViewModel.editState.titleText,
                onValueChange ={ newTitle -> noteViewModel.editState.titleText = newTitle; println("Editing: ${noteViewModel.editState.titleText}: $newTitle") }
            )

            TextField(
                value = noteViewModel.editState.bodyText,
                onValueChange ={ newText -> noteViewModel.editState.bodyText = newText }
            )

            OutlinedTextField(value = noteViewModel.editState.colorText,
                onValueChange = { noteViewModel.editState.colorText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        // This value is used to assign to
                        // the DropDown the same width
                        colorFieldSize = coordinates.size.toSize()
                    },
                label = {Text("Label")},
                trailingIcon = {
                    Icon(imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Expand",
                        modifier = Modifier.clickable { colorsMenuExpanded = !colorsMenuExpanded })
                }
            )
            DropdownMenu(
                expanded = colorsMenuExpanded,
                onDismissRequest = { colorsMenuExpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current){colorFieldSize.width.toDp()})
            ) {
                for(color in NoteColors.colors){
                    DropdownMenuItem(
                        text = {
                            Text(text = colorToString(color))
                        },
                        onClick = {
                            noteViewModel.editState.color = color
                            noteViewModel.editState.colorText = colorToString(color)
                            colorsMenuExpanded = false
                        })
                }
            }
        }
    }
}