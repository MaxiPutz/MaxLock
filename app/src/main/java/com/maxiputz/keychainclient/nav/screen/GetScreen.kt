package com.maxiputz.keychainclient.nav.screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maxiputz.keychainclient.store.Store
import com.maxiputz.keychainclient.structs.PasswordEntry


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetScreen(navController: NavController, viewModel: Store) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems by remember {
        derivedStateOf {
            viewModel.items.filter { it.url.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Passwords") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors()
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChanged = { query -> searchQuery = query }
                )
                LazyColumn (contentPadding = PaddingValues(vertical = 8.dp)){
                    items(filteredItems) {
                        item -> PasswordCard(passwordEntry = item)
                    }

                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = query,
        onValueChange = onQueryChanged,
        label = { Text(text = "Search by title") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
            }
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordCard(passwordEntry: PasswordEntry) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = passwordEntry.url,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row {
                    Text(
                        text = passwordEntry.username,
                        color = Color.Gray
                    )
                    IconButton(onClick = {
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Username", passwordEntry.username)
                        clipboard.setPrimaryClip(clip)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Copy Password"
                        )
                    }
                }
                Row {


                Text(
                    text = "*".repeat(passwordEntry.password.length),
                    color = Color.Gray
                )

            IconButton(onClick = {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Password", passwordEntry.password)
                clipboard.setPrimaryClip(clip)
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Copy Password")
            }
                }
         }
        }
    }
}