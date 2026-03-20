package org.ripple.ui.tags

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.ripple.ui.theme.RippleDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsScreen(
    onBack: () -> Unit,
    viewModel: TagsViewModel = viewModel()
) {
    val tags by viewModel.tags.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var tagToDelete by remember { mutableStateOf<String?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Etiquetas") },
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva etiqueta")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            if (tags.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay etiquetas", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tags) { tag ->
                        TagRow(
                            tag = tag,
                            onDelete = {
                                tagToDelete = tag.name
                                scope.launch {
                                    if (viewModel.isTagUsed(tag.name)) {
                                        showDeleteConfirmation = true
                                    } else {
                                        viewModel.deleteTag(tag.name)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddTagDialog(
            existingTags = tags.map { it.name },
            onDismiss = { showAddDialog = false },
            onConfirm = { name ->
                viewModel.addTag(name)
                showAddDialog = false
            }
        )
    }

    if (showDeleteConfirmation && tagToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Eliminar etiqueta") },
            text = { Text("Esta etiqueta está siendo usada por algunos contactos. ¿Estás seguro de que quieres eliminarla?") },
            confirmButton = {
                TextButton(onClick = {
                    tagToDelete?.let { viewModel.deleteTag(it) }
                    showDeleteConfirmation = false
                    tagToDelete = null
                }) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showDeleteConfirmation = false
                    tagToDelete = null
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun TagRow(tag: TagModel, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = tag.color.copy(alpha = 0.6f)
        ) {
            Text(
                text = tag.name,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                color = RippleDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Close, contentDescription = "Eliminar", tint = Color.Gray)
        }
    }
}

@Composable
private fun AddTagDialog(
    existingTags: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    val isDuplicate = existingTags.any { it.equals(name.trim(), ignoreCase = true) }
    val isValid = name.trim().isNotBlank() && !isDuplicate

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva etiqueta") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    isError = isDuplicate,
                    supportingText = {
                        if (isDuplicate) Text("Esta etiqueta ya existe")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                if (name.isNotBlank()) {
                    Text("Vista previa:", fontSize = 12.sp, color = Color.Gray)
                    val previewTag = createTagModel(name)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = previewTag.color.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = name,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = RippleDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name) },
                enabled = isValid
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
