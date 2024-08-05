package com.maxiputz.keychainclient

import BackgroundStartObserver
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.maxiputz.keychainclient.store.Store
import com.maxiputz.keychainclient.ui.theme.KeyChainClientTheme

class MainActivity : FragmentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("CoroutineCreationDuringComposition", "RememberReturnType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KeyChainClientTheme {
                val store: Store = viewModel()
                val navController = rememberNavController()

                var isAuth by remember {
                    mutableStateOf(false)
                }

                val context = LocalContext.current


                val lifecycleObserver = remember {
                    BackgroundStartObserver(context) { authResult ->
                        isAuth = authResult
                    }
                }

                DisposableEffect(Unit) {
                    lifecycle.addObserver(lifecycleObserver)
                    onDispose {
                        lifecycle.removeObserver(lifecycleObserver)
                    }
                }



                if (isAuth) {
                    NavGraph(navController = navController, store = store)
                } else {
                    Scaffold (
                        topBar = { TopAppBar(title = { Text("You are not Authenticate")}) },
                        content = {padding -> Box(modifier = Modifier.padding(padding)) { Text(text = "")} }
                    )
                }

            }
        }
    }
}

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PasswordView(navController: NavHostController, store: Store) {
        Scaffold(
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Text(
                        text = "Passwords",
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    var query by remember { mutableStateOf("") }
                    var active by remember { mutableStateOf(false) }



                    Row(modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.weight(1f)) {
                            PasswordCard(
                                title = "Passwords",
                                count = store.items.size,
                                color = Color.Green,
                                onClick = { navController.navigate("get") }
                            )
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            PasswordCard(
                                title = "Admin",
                                count = 0,
                                color = Color.Blue,
                                onClick = { navController.navigate("set") }
                            )
                        }

                    }
                }


            }
        )

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PasswordCard(title: String, count: Int, color: Color, onClick: () -> Unit) {
        Card(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = color
            ),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = count.toString(),
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }

    @Composable
    fun PasswordDetailsView(title: String, count: Int) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Details for $title",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Count: $count",
                style = TextStyle(
                    fontSize = 20.sp
                )
            )
        }
    }

