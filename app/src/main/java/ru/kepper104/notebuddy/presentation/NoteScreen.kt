package ru.kepper104.notebuddy.presentation

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.kepper104.notebuddy.domain.model.Note
import ru.kepper104.notebuddy.formatDate
import java.util.Calendar

@Composable
fun NoteScreen(
    noteViewModel: NoteViewModel = viewModel()
) {

    val composeContext = LocalContext.current

    val messageState by noteViewModel.message.observeAsState()

    if (messageState?.getContentIfNotHandled() != null){
        makeToast(messageState?.peekContent()!!, composeContext)
    }

    if (!noteViewModel.mainState.isEditing){
        MainView()
    } else {
        EditView()
    }
}

@Preview
@Composable
fun NoteComposable(note: Note = Note(null, "Just a preview", "Hello world!", Color.Green), viewModel: NoteViewModel? = null) {
    Column{
        Divider()
        Text(
            text = formatDate(note.last_modified, "dd.MM.yyyy HH:mm"),
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.End)
        )
        Text(
            text = note.title,
            fontSize = 40.sp
        )
        Text(
            text = note.text,
            fontSize = 30.sp
        )
        Row(
            modifier = Modifier.align(Alignment.End)
        ){
            Button(
                onClick = { viewModel!!.enableEditing(note) }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit a note")
            }
            Spacer(
                modifier = Modifier.width(10.dp)
            )
            Button(
                onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete a note")
            }

        }
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
    ) { paddingValues ->
        Column {
            Text(text = "My Notes")

            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ){
                for (note in noteViewModel.mainState.notes){
                    item {
                        NoteComposable(note = note, viewModel = noteViewModel)
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

    BackHandler(
        onBack = {noteViewModel.disableEditing()}
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { noteViewModel.saveNote() },
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
                onValueChange ={
                        newTitle -> noteViewModel.editState = noteViewModel.editState.copy(titleText = newTitle)}
            )

            TextField(
                value = noteViewModel.editState.bodyText,
                onValueChange ={ newText -> noteViewModel.editState = noteViewModel.editState.copy(bodyText = newText) }
            )

            ExposedDropdownMenuBox(
                expanded = colorsMenuExpanded,
                onExpandedChange = {colorsMenuExpanded = !colorsMenuExpanded}) {

                TextField(
                    value = noteViewModel.editState.colorText,
                    onValueChange = {  },
                    modifier = Modifier.menuAnchor(),
                    readOnly = true
                )

                ExposedDropdownMenu(
                    expanded = colorsMenuExpanded,
                    onDismissRequest = { colorsMenuExpanded = false }) {

                    for(color in NoteColors.colors) {
                        DropdownMenuItem(
                            text = {
                                DropdownMenuItemContent(color)
                            },
                            onClick = {
                                noteViewModel.editState.color = color
                                noteViewModel.editState.colorText = colorToString(color)
                                colorsMenuExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DropdownMenuItemContent(color: Color = Color.Green) {
    Row {
        val imageVector = Icons.Filled.Circle

        Icon(
            imageVector = imageVector,
            contentDescription = "Icon of ${colorToString(color)}",
            tint = color)

        Text(text = colorToString(color))

    }
}

fun makeToast(message: String, ctx: Context){
    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
}