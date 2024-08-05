package com.maxiputz.keychainclient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.maxiputz.keychainclient.nav.screen.GetScreen
import com.maxiputz.keychainclient.nav.screen.SetScreen
import com.maxiputz.keychainclient.store.Store


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController, store: Store) {
    NavHost(navController = navController, startDestination = "passwords") {
        composable(
            route = "passwords"
        ) {
            PasswordView(navController=navController, store)
        }
        
        composable(
            route = "get"
        ) {
            GetScreen(navController = navController, viewModel = store)
        }

        composable(
            route = "set",
        ) {
            SetScreen(navController, store)
        }
    }
}
