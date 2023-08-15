package ru.kepper104.notebuddy.presentation

import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.kepper104.notebuddy.domain.model.Note
import ru.kepper104.notebuddy.formatDate
import ru.kepper104.notebuddy.makeToast

@Composable
fun NoteScreen(
    noteViewModel: NoteViewModel = viewModel()
) {
    val loggerTag = "LoggerScreen"

    val snackbarHostState = remember { SnackbarHostState() }

    val composeContext = LocalContext.current

    HandleEvents(noteViewModel = noteViewModel, composeContext, snackbarHostState)


    if (!noteViewModel.mainState.isEditing){
        MainView(snackbarHostState = snackbarHostState)
    } else {
        EditView()
    }
}

@Composable
fun HandleEvents(noteViewModel: NoteViewModel, composeContext: Context, snackbarHostState: SnackbarHostState) {
    val loggerTag = "LoggerEvents"
    LaunchedEffect(key1 = true) {
        noteViewModel.sharedFlow.collect { event ->
            when (event) {
                NoteViewModel.ScreenEvent.NoteSavedToast -> {
                    Log.v(loggerTag, "NoteSaved event received!")

                    makeToast("Note Saved!", composeContext)
                }
                NoteViewModel.ScreenEvent.NoteDeletedSnackBar -> {
                    Log.v(loggerTag, "NoteDeleted event received!")

                    Log.d(loggerTag, "Showing snackbar!")

                    noteViewModel.mainState = noteViewModel.mainState.copy(
                        isAbleToDeleteNotes = false
                    )
                    val snackBarResult = snackbarHostState.showSnackbar(
                        message = "Note Deleted!",
                        actionLabel = "Undo",
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )

                    Log.v(loggerTag, "Finished showing snackbar!")

                    noteViewModel.mainState = noteViewModel.mainState.copy(
                        isAbleToDeleteNotes = true
                    )

                    when(snackBarResult){
                        SnackbarResult.Dismissed -> {
                            Log.d(loggerTag, "Note deleted!")
                        }

                        SnackbarResult.ActionPerformed -> {
                            noteViewModel.restoreLastDeletedNote()
//                            Log.d(TAG, "Note restored!")
                        }
                    }
                    Log.v(loggerTag, "End of snackbar launched effect")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    noteViewModel: NoteViewModel = viewModel(),
    snackbarHostState: SnackbarHostState
){
    val loggerTag = "LoggerMainView"

    Surface(
        color = colorGray.c(),
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    noteViewModel.createNote()
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add a note")
                }
            }
        ) { paddingValues ->
            Column (
                modifier = Modifier
                    .background(colorGray.c())
                    .fillMaxSize()
            ){
                Text(
                    text = "My Notes",
                    fontSize = 40.sp,
                    color = colorLightGray.c(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth())

                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                ){
                    // All the notes
                    for (note in noteViewModel.mainState.notes){
                        item {
                            NoteComposable(note = note, viewModel = noteViewModel)
                        }
                    }
                    // Last element of LazyColumn to have some space at the bottom
                    item {
                        Spacer(modifier = Modifier.height(50.dp))
                        Text(
                            text =
                            if(noteViewModel.mainState.notes.isNotEmpty())
                            {
                                "Those are all the notes!"
                            } else {
                                "Tap the + to create some notes!"
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            color = colorLightGray.c()
                        )
                        Spacer(modifier = Modifier.height(50.dp))
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
    val loggerTag = "LoggerEditView"

    BackHandler(
        onBack = {noteViewModel.disableEditing()}
    )

    Surface (
        color = noteViewModel.getSelectedColor().c(),
        modifier = Modifier.fillMaxSize()
    ){
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        noteViewModel.saveNote()
                        Log.d(loggerTag, "Note saved!")
                    },
                )
                {
                    Icon(imageVector = Icons.Default.Save, contentDescription = "Save the note")
                }
            }
        ) {paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(
                        noteViewModel
                            .getSelectedColor()
                            .c()
                    )
                    .fillMaxSize()
            ) {
                Text(
                    text = "Edit a Note",
                    fontSize = 40.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                ColorDIYRadioButtons(
                    colorsRadioButtonsList = noteViewModel.editState.colorsRadioButtonsList,
                    setSelectedColor = noteViewModel::setSelectedColor
                )
                Spacer(modifier = Modifier.height(15.dp))

                TextField(
                    value = noteViewModel.editState.titleText,
                    onValueChange ={ newTitle ->

                        noteViewModel.editState = noteViewModel.editState.copy(titleText = newTitle)},
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = noteViewModel.getSelectedColor().c(),
                        textColor = Color.Black,
                        placeholderColor = Color.Gray,
                        focusedIndicatorColor = Color.Black,
                        focusedLeadingIconColor = Color.Black,
                        focusedSupportingTextColor = Color.Black,
                        focusedTrailingIconColor = Color.Black,
                        cursorColor = Color.Black

                    ),
                    textStyle = TextStyle.Default.copy(fontSize = 30.sp),
                    placeholder = {
                        Text(text = "Title",
                            fontSize = 20.sp)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = noteViewModel.editState.bodyText,
                    onValueChange ={ newText ->
                        noteViewModel.editState = noteViewModel.editState.copy(bodyText = newText) },
                    placeholder = {
                        Text(text = "Start typing",
                            fontSize = 20.sp)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = noteViewModel.getSelectedColor().c(),
                        textColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        placeholderColor = Color.Gray,
                        cursorColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}
 class ToggleableInfo(
     val initiallySelected: MutableState<Boolean> = mutableStateOf(false),
     val color: CustomColor
){
     var isSelected by mutableStateOf(initiallySelected)
 }
@Composable
fun ColorRadioButtons(viewModel: NoteViewModel) {

    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        viewModel.editState.colorsRadioButtonsList.forEachIndexed() { index, info ->
            RadioButton(
                selected = info.isSelected.value,
                onClick = {
                    viewModel.setSelectedColor(info.color)
                },
                )
        }
    }
}

@Composable
fun ColorDIYRadioButtons(
    colorsRadioButtonsList: MutableList<ToggleableInfo>,
    setSelectedColor: (color: CustomColor) -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        colorsRadioButtonsList.forEachIndexed() { index, info ->
            Log.d("LoggerRadioButtons", "$index: ${info.color.name()}, ${info.isSelected.value}")
            Box(modifier = Modifier
                .size(50.dp)
                .shadow(15.dp, CircleShape)
                .clip(CircleShape)
                .background(info.color.c())
                .border(
                    width = 3.dp,
                    color = if (info.isSelected.value) {
                        Color.Black
                    } else {
                        Color.Transparent
                    },
                    shape = CircleShape
                )
                .clickable { setSelectedColor(info.color) })

        }
    }


}
@Preview
@Composable
fun NoteComposable(note: Note = Note(null, "Just a preview", "Hello world!", colorGreen), viewModel: NoteViewModel? = null) {
    val loggerTag = "LoggerNoteComposable"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .padding(vertical = 10.dp)
                .background(note.color.c(), RoundedCornerShape(5))
                .padding(10.dp)
                .padding(horizontal = 10.dp)


        ){
            Text(
                text = formatDate(note.last_modified, "dd.MM.yyyy HH:mm"),
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.End)
            )
            Text(
                text = note.title,
                fontSize = 30.sp
            )
            Text(
                text = note.text,
                fontSize = 20.sp,
                maxLines = 7
            )
            Row(
                modifier = Modifier.align(Alignment.End)
            ){
                TextButton(
                    onClick = {
                        viewModel!!.enableEditing(note)
                    },) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit a note",
                        tint = Color.Black)
                }
                Spacer(
                    modifier = Modifier.width(10.dp)
                )
                TextButton(
                    onClick = {
                        viewModel!!.deleteNote(note)
                    },
                    enabled = viewModel!!.mainState.isAbleToDeleteNotes,
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete a note",
                        tint = if (viewModel.mainState.isAbleToDeleteNotes)
                        {Color.Black} else {Color.Gray})
                }

            }
        }
    }

}

@Preview
@Composable
fun DropdownMenuItemContent(color: CustomColor = colorGreen) {
    Row {
        val imageVector = Icons.Filled.Circle

        Icon(
            imageVector = imageVector,
            contentDescription = "Icon of ${color.name()}",
            tint = color.c())

        Text(text = color.name())

    }
}

