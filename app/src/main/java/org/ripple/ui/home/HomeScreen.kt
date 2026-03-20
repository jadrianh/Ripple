package org.ripple.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.ripple.ui.components.ContactCard
import org.ripple.ui.theme.RippleAccent
import org.ripple.ui.theme.RippleGradientBrush
import org.ripple.ui.theme.RippleGray

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onContactClick: (Int) -> Unit,
    onAddContact: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenTags: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Move gradient and insets OUTSIDE the drawer
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RippleGradientBrush)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = true,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .width(260.dp)
                        .statusBarsPadding(),
                    drawerContainerColor = MaterialTheme.colorScheme.surface,
                    drawerShape = RoundedCornerShape(0.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "Ripple",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = RippleAccent
                        )
                        Text(
                            text = "Tu agenda personal",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))

                    DrawerItem(Icons.Default.Label, "Etiquetas") {
                        scope.launch { drawerState.close() }
                        onOpenTags()
                    }
                    DrawerItem(Icons.Default.Settings, "Ajustes") {
                        scope.launch { drawerState.close() }
                        onOpenSettings()
                    }
                }
            }
        ) {
            // Main Content Area
            Column(modifier = Modifier.fillMaxSize()) {
                // TOP BAR
                HomeTopBar(
                    viewModel = viewModel,
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )

                // TAG FILTER - Dynamic from pinned tags
                val pinnedTags by viewModel.pinnedTags.collectAsState()
                val tags = listOf("Todos") + pinnedTags
                var selectedTag by remember { mutableStateOf("Todos") }

                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tags) { tag ->
                        TagChip(
                            tag = tag,
                            isSelected = selectedTag == tag,
                            onClick = { selectedTag = tag }
                        )
                    }
                }

                // CONTACT LIST
                val contacts by viewModel.contacts.collectAsState()
                val searchQuery by viewModel.searchQuery.collectAsState()

                val filteredContacts = remember(contacts, selectedTag) {
                    if (selectedTag == "Todos") {
                        contacts
                    } else {
                        contacts.filter { it.contact.tags?.contains(selectedTag, ignoreCase = true) == true }
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    if (filteredContacts.isEmpty()) {
                        EmptyState(
                            isSearching = searchQuery.isNotBlank() || selectedTag != "Todos"
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredContacts, key = { it.contact.id }) { contact ->
                                ContactCard(
                                    contact = contact,
                                    onClick = { onContactClick(contact.contact.id) }
                                )
                            }
                        }
                    }

                    // FAB
                    FloatingActionButton(
                        onClick = onAddContact,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(24.dp),
                        shape = CircleShape,
                        containerColor = RippleAccent,
                        contentColor = Color.White
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar contacto",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = null, tint = RippleAccent) },
        label = { Text(label, color = RippleAccent) },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp),
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent
        )
    )
}

@Composable
private fun HomeTopBar(
    viewModel: HomeViewModel,
    onOpenDrawer: () -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onOpenDrawer) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
        }

        Surface(
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)

                Box(contentAlignment = Alignment.CenterStart) {
                    if (searchQuery.isEmpty()) {
                        Text("Buscar contacto...", color = RippleGray, fontSize = 14.sp)
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.onSearchQueryChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        }
    }
}

@Composable
private fun TagChip(
    tag: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable { onClick() }
            .then(
                if (!isSelected) Modifier.border(1.dp, Color.White, RoundedCornerShape(16.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Color.White else Color.Transparent
    ) {
        Text(
            text = tag,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            color = if (isSelected) RippleAccent else Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun EmptyState(isSearching: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isSearching) {
            Text(
                text = "Sin resultados",
                color = Color.White,
                fontSize = 16.sp
            )
        } else {
            Icon(
                imageVector = Icons.Default.People,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = "Todavía no tienes contactos",
                color = Color.White,
                fontSize = 16.sp
            )
            Text(
                text = "Toca + para agregar el primero",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp
            )
        }
    }
}
