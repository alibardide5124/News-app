package com.phoenix.newsapp

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.phoenix.newsapp.components.AppNavRail
import com.phoenix.newsapp.screen.NavGraphs
import com.phoenix.newsapp.screen.destinations.AboutScreenDestination
import com.phoenix.newsapp.screen.destinations.FavoriteRouteDestination
import com.phoenix.newsapp.screen.destinations.HomeRouteDestination
import com.phoenix.newsapp.screen.destinations.SearchRouteDestination
import com.phoenix.newsapp.ui.theme.AppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.utils.currentDestinationAsState

@Composable
fun NewsApp(widthSizeClass: WindowWidthSizeClass) {
    AppTheme {
        val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
        val engine = rememberNavHostEngine()
        val navController = engine.rememberNavController()
        val currentRoute =
            navController.currentDestinationAsState().value?.route ?: NavGraphDestination.HOME_ROUTE

        Row {
            if (isExpandedScreen) {
                AppNavRail(
                    currentRoute = currentRoute,
                    navigateToHome = { navController.navigate(HomeRouteDestination) {
                        launchSingleTop = true
                        restoreState = true
                    } },
                    navigateToSearch = { navController.navigate(SearchRouteDestination) {
                        launchSingleTop = true
                    }},
                    navigateToSaves = { navController.navigate(FavoriteRouteDestination) {
                        launchSingleTop = true
                    } },
                    navigateToAbout = { navController.navigate(AboutScreenDestination) {
                        launchSingleTop = true
                    } }
                )
            }
            DestinationsNavHost(
                navGraph = NavGraphs.root,
                engine = engine,
                navController = navController
            )
        }
    }
}