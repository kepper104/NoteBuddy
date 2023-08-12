package ru.kepper104.notebuddy.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.kepper104.notebuddy.domain.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    noteViewModel: NoteViewModel = viewModel()
) {
    if (!noteViewModel.state.isEditing){
        mainView()
    }else{
        editView()
    }
}

@Composable
fun NoteComposable(note: Note) {
    Column{
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
fun mainView(
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
                for (note in noteViewModel.state.notes){
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
fun editView(
    noteViewModel: NoteViewModel = viewModel()
){
    var title by remember {
        mutableStateOf("")
    }
    var bodyText by remember {
        mutableStateOf("")
    }

//    var availableColors by remember{
//        mutableListOf()
//    }
    var colorsMenuExpanded by remember {
        mutableStateOf(false)
    }
    Column {
        Text(text = "Edit a note")
        TextField(
            value = title, 
            onValueChange ={ newTitle -> title = newTitle }
        )
        TextField(
            value = bodyText, 
            onValueChange ={ newText -> bodyText = newText }
        )
        DropdownMenu(
            expanded = colorsMenuExpanded,
            onDismissRequest = { colorsMenuExpanded = false },
            ) {
            for(color in NoteColors.colors){
                DropdownMenuItem(
                    text = {
                        Text(text = color.toString())
                    },
                    onClick = {
                        val newNote = noteViewModel.state.editedNote
                        newNote.color = color
                        noteViewModel.state.copy(editedNote = newNote)
                        colorsMenuExpanded = false
                    })
            }
            
        }
    }
}