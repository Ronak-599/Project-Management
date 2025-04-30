package com.example.pms.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pms.data.auth.AuthService
import com.example.pms.ui.screens.auth.AuthViewModel
import com.example.pms.ui.screens.auth.LoginScreen
import com.example.pms.ui.screens.auth.RegisterScreen
import com.example.pms.ui.screens.dashboard.DashboardScreen
import com.example.pms.ui.screens.projects.ProjectDetailsScreen
import com.example.pms.ui.screens.projects.ProjectsScreen
import com.example.pms.ui.screens.tasks.TasksScreen
import com.example.pms.ui.screens.team.TeamScreen
import javax.inject.Inject

@Composable
fun AppNavigation(
    navController: NavHostController, 
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.state.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = if (authState.isLoggedIn) Screen.Dashboard.route else Screen.Login.route
    ) {
        // Auth screens
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = { navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }}
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onRegisterSuccess = { navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Register.route) { inclusive = true }
                }}
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToProjects = { navController.navigate(Screen.Projects.route) },
                onNavigateToTasks = { navController.navigate(Screen.Tasks.route) }
            )
        }
        
        composable(Screen.Projects.route) {
            ProjectsScreen(
                onNavigateToProjectDetails = { projectId ->
                    navController.navigate(Screen.ProjectDetails.createRoute(projectId))
                }
            )
        }
        
        composable(
            route = Screen.ProjectDetails.route,
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            ProjectDetailsScreen(
                projectId = projectId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Tasks.route) {
            TasksScreen()
        }
        
        composable(Screen.Team.route) {
            TeamScreen()
        }
    }
}
