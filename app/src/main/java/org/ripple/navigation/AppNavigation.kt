package org.ripple.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.ripple.ui.addedit.ContactViewModel
import org.ripple.ui.home.HomeViewModel

sealed class RippleRoute(val route: String) {
    data object Home : RippleRoute("home")

    data object ContactProfile : RippleRoute("contactProfile/{contactId}") {
        const val ARG_CONTACT_ID = "contactId"
        fun createRoute(contactId: Int): String = "contactProfile/$contactId"
    }

    data object AddContact : RippleRoute("addContact")

    data object EditContact : RippleRoute("editContact/{contactId}") {
        const val ARG_CONTACT_ID = "contactId"
        fun createRoute(contactId: Int): String = "editContact/$contactId"
    }

    data object Settings : RippleRoute("settings")
}

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = RippleRoute.Home.route
    ) {
        composable(route = RippleRoute.Home.route) {
            val homeViewModel: HomeViewModel = viewModel()
            HomeScreen(
                viewModel = homeViewModel,
                onContactClick = { id -> navController.navigate(RippleRoute.ContactProfile.createRoute(id)) },
                onAddContact = { navController.navigate(RippleRoute.AddContact.route) },
                onOpenSettings = { navController.navigate(RippleRoute.Settings.route) }
            )
        }

        composable(
            route = RippleRoute.ContactProfile.route,
            arguments = listOf(
                navArgument(RippleRoute.ContactProfile.ARG_CONTACT_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val contactId =
                backStackEntry.arguments?.getInt(RippleRoute.ContactProfile.ARG_CONTACT_ID) ?: return@composable
            val contactViewModel: ContactViewModel = viewModel()
            ContactProfileScreen(
                contactId = contactId,
                viewModel = contactViewModel,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(RippleRoute.EditContact.createRoute(contactId)) },
                onDeleted = { navController.popBackStack(RippleRoute.Home.route, inclusive = false) }
            )
        }

        composable(route = RippleRoute.AddContact.route) {
            val contactViewModel: ContactViewModel = viewModel()
            AddEditContactScreen(
                contactId = null,
                viewModel = contactViewModel,
                onCancel = { navController.popBackStack() },
                onSaved = { newId ->
                    navController.navigate(RippleRoute.ContactProfile.createRoute(newId)) {
                        popUpTo(RippleRoute.Home.route)
                    }
                }
            )
        }

        composable(
            route = RippleRoute.EditContact.route,
            arguments = listOf(
                navArgument(RippleRoute.EditContact.ARG_CONTACT_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val contactId =
                backStackEntry.arguments?.getInt(RippleRoute.EditContact.ARG_CONTACT_ID) ?: return@composable
            val contactViewModel: ContactViewModel = viewModel()
            AddEditContactScreen(
                contactId = contactId,
                viewModel = contactViewModel,
                onCancel = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(route = RippleRoute.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onContactClick: (Int) -> Unit,
    onAddContact: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Box(Modifier.fillMaxSize())
}

@Composable
fun ContactProfileScreen(
    contactId: Int,
    viewModel: ContactViewModel,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDeleted: () -> Unit
) {
    Box(Modifier.fillMaxSize())
}

@Composable
fun AddEditContactScreen(
    contactId: Int?,
    viewModel: ContactViewModel,
    onCancel: () -> Unit,
    onSaved: (Int) -> Unit
) {
    Box(Modifier.fillMaxSize())
}

@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    Box(Modifier.fillMaxSize())
}

