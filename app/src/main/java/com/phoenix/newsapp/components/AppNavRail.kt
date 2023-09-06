package com.phoenix.newsapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.phoenix.newsapp.NavGraphDestination
import com.phoenix.newsapp.R

@Composable
fun AppNavRail(
    currentRoute: String,
    navigateToHome: () -> Unit,
    navigateToSearch: () -> Unit,
    navigateToSaves: () -> Unit,
    navigateToAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        header = {
            Icon(
                painter = painterResource(R.drawable.ic_browser),
                contentDescription = null,
                modifier = Modifier.padding(vertical = 12.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier
    ) {
        Spacer(Modifier.weight(1f))
        NavigationRailItem(
            selected = currentRoute == NavGraphDestination.HOME_ROUTE,
            onClick = { navigateToHome() },
            icon = { Icon(Icons.Filled.Home, stringResource(R.string.home_title)) },
            label = { Text(stringResource(R.string.home_title)) },
            alwaysShowLabel = false
        )
        NavigationRailItem(
            selected = currentRoute == NavGraphDestination.SEARCH_ROUTE,
            onClick = { navigateToSearch() },
            icon = { Icon(Icons.Default.Search, stringResource(R.string.search_title)) },
            label = { Text(stringResource(R.string.search_title)) },
            alwaysShowLabel = false
        )
        NavigationRailItem(
            selected = currentRoute == NavGraphDestination.SAVES_ROUTE,
            onClick = { navigateToSaves() },
            icon = { Icon(painterResource(R.drawable.ic_bookmark), stringResource(R.string.favorite_title)) },
            label = { Text(stringResource(R.string.saves_title)) },
            alwaysShowLabel = false
        )
        Column(verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Bottom)) {
            NavigationRailItem(
                selected = currentRoute == NavGraphDestination.ABOUT_ROUTE,
                onClick = { navigateToAbout() },
                icon = { Icon(Icons.Default.Info, stringResource(R.string.about_title)) },
                label = { Text(stringResource(R.string.about_title)) },
                alwaysShowLabel = false
            )
        }
        Spacer(Modifier.weight(1f))
    }
}