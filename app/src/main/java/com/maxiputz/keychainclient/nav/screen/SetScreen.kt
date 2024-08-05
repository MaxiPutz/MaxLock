package com.maxiputz.keychainclient.nav.screen

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maxiputz.keychainclient.controller.fetchDataFromServer
import com.maxiputz.keychainclient.controller.textToObject
import com.maxiputz.keychainclient.controller.tryDecryptCheck
import com.maxiputz.keychainclient.store.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetScreen(navController: NavController, viewModel: Store)  {
    var serverAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Admin") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors()
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = serverAddress,
                    onValueChange = { serverAddress = it },
                    label = { Text("Server Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                val data = fetchDataFromServer(serverAddress)
                                Toast.makeText(context, "Server data received", Toast.LENGTH_SHORT).show()
                                Log.d("Data", "Received: $data")
                                viewModel.setResponseData(data)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Failed to connect to server", Toast.LENGTH_SHORT).show()
                                Log.e("Error", "Error fetching data", e)
                            }
                        }
                              },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Fetch Data")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val resData = tryDecryptCheck(password, viewModel.getResponseData())
                        Log.d("decypted", resData.check)
                        Log.d("decypted", resData.secret)
                        if (resData.check == "pass") {
                            Toast.makeText(context, "The Pw is correct", Toast.LENGTH_SHORT).show()
                            viewModel.emptyItems()
                            viewModel.addItems( textToObject(resData.secret))
                        }
                              },
                    modifier = Modifier.fillMaxWidth()) {
                    Text("Try Password")
                }
            }
        }
    )
}