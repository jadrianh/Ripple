package org.ripple.ui.settings

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.ripple.data.local.ContactWithDetails
import org.ripple.data.local.RippleDatabase
import org.ripple.ui.theme.RippleThemePreferences
import org.ripple.util.VcfExporter

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    val allContacts: StateFlow<List<ContactWithDetails>> =
        RippleDatabase.getInstance(application).contactDao().getAllContactsWithDetails()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showAboutDialog by remember { mutableStateOf(false) }
    
    val isDarkTheme by RippleThemePreferences.darkThemeEnabledFlow(context)
        .collectAsState(initial = false)
    
    val contacts by viewModel.allContacts.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // Theme Toggle
            SettingsItem(
                icon = Icons.Default.DarkMode,
                label = "Tema oscuro",
                trailing = {
                    Switch(
                        checked = isDarkTheme ?: false,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                RippleThemePreferences.setDarkThemeEnabled(context, enabled)
                            }
                        }
                    )
                }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // Export Contacts
            SettingsItem(
                icon = Icons.Default.Upload,
                label = "Exportar contactos (.vcf)",
                trailing = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                onClick = {
                    if (contacts.isEmpty()) {
                        Toast.makeText(context, "No tienes contactos para exportar", Toast.LENGTH_SHORT).show()
                    } else {
                        try {
                            Toast.makeText(context, "Exportando ${contacts.size} contactos...", Toast.LENGTH_SHORT).show()
                            VcfExporter.export(context, contacts)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error al exportar: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // About
            SettingsItem(
                icon = Icons.Default.Info,
                label = "Acerca de Ripple",
                trailing = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                onClick = { showAboutDialog = true }
            )
        }
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("Ripple") },
            text = {
                Column {
                    Text("Versión 1.0.0")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tu agenda personal. Organiza tus contactos de forma fácil y rápida.")
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    label: String,
    trailing: @Composable () -> Unit,
    onClick: (() -> Unit)? = null
) {
    ListItem(
        modifier = if (onClick != null) Modifier.fillMaxWidth().clickable { onClick() } else Modifier.fillMaxWidth(),
        headlineContent = { Text(label) },
        leadingContent = { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
        trailingContent = trailing,
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}
