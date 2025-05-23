package com.doubletapp_hw

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.doubletapp_hw.screens.HabitEditScreen
import com.doubletapp_hw.screens.HabitsPagerScreen
import com.doubletapp_hw.screens.InfoScreen
import com.doubletapp_hw.screens.Routes
import com.doubletapp_hw.ui.theme.Dobletapp_hwTheme
import kotlinx.coroutines.launch

class FirstActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Dobletapp_hwTheme {
                Surface(

                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val viewModel by viewModels<HabitListViewModel>()

        val items = listOf(Routes.Home, Routes.Info)
        val selectedItem = remember { mutableStateOf(items[0]) }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(Modifier.height(12.dp))
                    items.forEach { item ->
                        NavigationDrawerItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(stringResource(item.title)) },
                            selected = item == selectedItem.value,
                            onClick = {
                                scope.launch { drawerState.close() }
                                selectedItem.value = item
                                navController.navigate(item){
                                    popUpTo(0)
                                }
                              },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            },
            content = {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    stringResource( selectedItem.value.title),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Open Navigation Drawer"
                                    )
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    NavHost(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController,
                        startDestination = Routes.Home
                    ) {
                        composable<Routes.Home> {
                            HabitsPagerScreen(viewModel) { id ->
                                navController.navigate(
                                    Routes.HabitEdit(
                                        id
                                    )
                                )
                            } // можно передать лямбду вместо вьюмодел(???) и навконтрл
                        }
                        composable<Routes.Info> {
                            InfoScreen()
                        }
                        composable<Routes.HabitEdit> { backStackEntry ->
                            val edit: Routes.HabitEdit = backStackEntry.toRoute()
                            HabitEditScreen(viewModel = viewModel, habitId = edit.id,
                                onBack = { navController.popBackStack()},
                                onSave = { habit ->
                                val updatedHabit = habit.copy(
                                    id = habit.id
                                )
                                viewModel.saveHabit(updatedHabit)
                                navController.popBackStack()
                            })
                        }
                    }
                }
            }
        )
    }
}
