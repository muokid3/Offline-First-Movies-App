package com.dm.berxley.offlinefirstmoviesapp.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dm.berxley.offlinefirstmoviesapp.R
import com.dm.berxley.offlinefirstmoviesapp.movieslist.presentation.MovieListUiEvent
import com.dm.berxley.offlinefirstmoviesapp.movieslist.presentation.MovieListViewModel
import com.dm.berxley.offlinefirstmoviesapp.movieslist.util.Screen
import org.w3c.dom.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController) {
    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieState = movieListViewModel.movieListState.collectAsState().value

    val bottomNavController = rememberNavController()


    Scaffold(
        bottomBar = {
            BottomNavBar(
                bottomNavController = bottomNavController,
                onEvent = movieListViewModel::onEvent
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = movieState.currentScreenTitle,
                        fontSize = 20.sp
                    )
                },
                modifier = Modifier.shadow(2.dp),
                colors = topAppBarColors(
                    MaterialTheme.colorScheme.inverseOnSurface
                )

            )
        }
    ) {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()){
            NavHost(navController = bottomNavController, startDestination = Screen.PopularMovieList.route){
                composable(Screen.PopularMovieList.route){
                    PopularMoviesScreen()
                }
                composable(Screen.UpcomingMovieList.route){
                    UpcomingMoviesScreen()
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(
    bottomNavController: NavHostController,
    onEvent: (MovieListUiEvent) -> Unit
) {
    val movieListViewModel = hiltViewModel<MovieListViewModel>()

    val selected = rememberSaveable { mutableIntStateOf(0) }
    val items = listOf(
        BottomItem(
            title = stringResource(R.string.popular),
            selectedIcon = Icons.Filled.Movie,
            unselectedicon = Icons.Outlined.Movie
        ),
        BottomItem(
            title = stringResource(R.string.upcoming),
            selectedIcon = Icons.Filled.Upcoming,
            unselectedicon = Icons.Outlined.Upcoming
        )
    )


    NavigationBar {
        Row(modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)) {
            items.forEachIndexed { index, bottomItem ->
                NavigationBarItem(
                    selected = selected.intValue == index,
                    onClick = {
                        selected.intValue = index
                        when (selected.intValue) {
                            0 -> {
                                onEvent(MovieListUiEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screen.PopularMovieList.route)
                                movieListViewModel.setCurrentTitle("Popular Movies")
                            }

                            1 -> {
                                onEvent(MovieListUiEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screen.UpcomingMovieList.route)
                                movieListViewModel.setCurrentTitle("Upcoming Movies")
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (selected.intValue == index) bottomItem.selectedIcon else bottomItem.unselectedicon,
                            contentDescription = bottomItem.title,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(
                            text = bottomItem.title,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    })
            }
        }
    }


}

data class BottomItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedicon: ImageVector
)