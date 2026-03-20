package org.ripple.ui.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ripple.data.local.ContactWithDetails
import org.ripple.ui.addedit.ContactUiState
import org.ripple.ui.addedit.ContactViewModel
import org.ripple.ui.theme.RippleAccent
import org.ripple.ui.theme.RippleDark
import org.ripple.ui.theme.RippleGradientBrush
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.alpha
import org.ripple.data.tags.createTagModel

private val avatarColors = listOf(
    Color(0xFF378ADD), Color(0xFF1D9E75), Color(0xFFD85A30),
    Color(0xFF7F77DD), Color(0xFFBA7517), Color(0xFF993556)
)

@Composable
fun ContactProfileScreen(
    contactId: Int,
    viewModel: ContactViewModel,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDeleted: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val contact by viewModel.selectedContact.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(contactId) {
        viewModel.loadContact(contactId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RippleGradientBrush)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        when (val state = uiState) {
            is ContactUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
            is ContactUiState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(state.message, color = Color.White)
                    Button(onClick = onBack) { Text("Volver") }
                }
            }
            is ContactUiState.Success -> {
                state.contact?.let { contactData ->
                    ProfileContent(
                        contact = contactData,
                        onBack = onBack,
                        onEdit = onEdit,
                        onDeleteClick = { showDeleteDialog = true }
                    )
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar contacto") },
                text = { Text("¿Estás seguro de que quieres eliminar este contacto? Esta acción no se puede deshacer.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteContact(contactId)
                            showDeleteDialog = false
                            onDeleted()
                        }
                    ) {
                        Text("Eliminar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
private fun ProfileContent(
    contact: ContactWithDetails,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Custom Header with Avatar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                    }
                }
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val initials = (contact.contact.firstName.take(1) + contact.contact.lastName.take(1)).uppercase()
                val bgColor = avatarColors[contact.contact.id % 6]
                
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = bgColor,
                    shadowElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = initials,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "${contact.contact.firstName} ${contact.contact.lastName}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                if (!contact.contact.company.isNullOrBlank()) {
                    Text(
                        text = contact.contact.company,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Action Buttons Row
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White.copy(alpha = 0.2f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val primaryPhone = contact.phoneNumbers.firstOrNull()?.number ?: ""
                val primaryEmail = contact.contact.email ?: ""

                ActionButton(Icons.Default.Call, "Llamar") {
                    if (primaryPhone.isNotBlank()) launchIntent(context, Intent(Intent.ACTION_DIAL, Uri.parse("tel:$primaryPhone")))
                }
                ActionButton(Icons.Default.Message, "SMS") {
                    if (primaryPhone.isNotBlank()) launchIntent(context, Intent(Intent.ACTION_VIEW, Uri.parse("sms:$primaryPhone")))
                }
                ActionButton(Icons.Default.Email, "Email") {
                    if (primaryEmail.isNotBlank()) launchIntent(context, Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$primaryEmail")))
                }
                
                val hasWhatsApp = contact.socialNetworks.any { it.network.equals("WhatsApp", ignoreCase = true) }
                ActionButton(Icons.Default.Chat, "WhatsApp", enabled = hasWhatsApp) {
                    val waNumber = contact.socialNetworks.firstOrNull { it.network.equals("WhatsApp", ignoreCase = true) }?.username ?: primaryPhone
                    launchIntent(context, Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$waNumber")))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Details Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = 24.dp, bottomEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Información de contacto", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))

                // Phone Numbers
                contact.phoneNumbers.forEach { phone ->
                    InfoRow(Icons.Default.Phone, "${phone.suffix ?: ""} ${phone.number}".trim(), "Móvil")
                }

                // Email
                if (!contact.contact.email.isNullOrBlank()) {
                    InfoRow(Icons.Default.Email, contact.contact.email, "Correo electrónico")
                }

                // Birthday
                if (!contact.contact.birthday.isNullOrBlank()) {
                    InfoRow(Icons.Default.Cake, contact.contact.birthday, "Cumpleaños")
                }

                // Address
                if (!contact.contact.address.isNullOrBlank()) {
                    InfoRow(Icons.Default.LocationOn, contact.contact.address, "Dirección")
                }

                // Social Networks Chips
                if (contact.socialNetworks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Redes Sociales", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        contact.socialNetworks.forEach { social ->
                            SocialChip(social.network, social.username)
                        }
                    }
                }

                // Notes
                if (!contact.contact.notes.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Notas", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(contact.contact.notes, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                }

                // Tags
                val tagList = contact.contact.tags?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()
                if (tagList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Etiquetas", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tagList.forEach { tagName ->
                            val tag = createTagModel(tagName)
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = tag.color.copy(alpha = 0.6f)
                            ) {
                                Text(
                                    text = tag.name,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    color = RippleDark,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButton(icon: ImageVector, label: String, enabled: Boolean = true, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(enabled = enabled) { onClick() }.alpha(if (enabled) 1f else 0.4f)
    ) {
        Icon(icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = Color.White, fontSize = 11.sp)
    }
}

@Composable
private fun InfoRow(icon: ImageVector, value: String, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = RippleAccent, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(value, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SocialChip(network: String, username: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$network: ",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = RippleAccent
            )
            Text(
                text = username,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun launchIntent(context: Context, intent: Intent) {
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        // Handle error (e.g., app not found)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        content = { content() }
    )
}
