package org.ripple.ui.addedit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ripple.ui.theme.RippleAccent
import org.ripple.ui.theme.RippleDark
import org.ripple.ui.theme.RippleGradientBrush

@Composable
fun AddEditContactScreen(
    contactId: Int?,
    viewModel: ContactViewModel,
    onCancel: () -> Unit,
    onSaved: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val contact by viewModel.selectedContact.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumbers by remember { mutableStateOf(listOf(PhoneNumberInput(null, ""))) }
    var socialNetworks by remember { mutableStateOf(listOf<SocialNetworkInput>()) }
    var birthday by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }

    var isSavingInitiated by remember { mutableStateOf(false) }

    LaunchedEffect(contactId) {
        if (contactId != null) {
            viewModel.loadContact(contactId)
        }
    }

    // Populate fields when contact is loaded
    LaunchedEffect(contact) {
        contact?.let { c ->
            firstName = c.contact.firstName
            lastName = c.contact.lastName
            company = c.contact.company ?: ""
            email = c.contact.email ?: ""
            birthday = c.contact.birthday ?: ""
            address = c.contact.address ?: ""
            notes = c.contact.notes ?: ""
            tags = c.contact.tags ?: ""
            
            if (c.phoneNumbers.isNotEmpty()) {
                phoneNumbers = c.phoneNumbers.map { PhoneNumberInput(it.suffix, it.number) }
            }
            if (c.socialNetworks.isNotEmpty()) {
                socialNetworks = c.socialNetworks.map { SocialNetworkInput(it.network, it.username) }
            }
        }
    }

    // Handle navigation after save
    LaunchedEffect(uiState) {
        if (isSavingInitiated && uiState is ContactUiState.Success) {
            val savedContact = (uiState as ContactUiState.Success).contact
            if (savedContact != null) {
                onSaved(savedContact.contact.id)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RippleGradientBrush)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TOP BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onCancel) {
                    Text("Cancelar", color = Color.White, fontSize = 16.sp)
                }
                
                Text(
                    text = if (contactId == null) "Nuevo contacto" else "Editar contacto",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                TextButton(
                    onClick = {
                        isSavingInitiated = true
                        viewModel.saveContact(
                            id = contactId,
                            firstName = firstName,
                            lastName = lastName,
                            phoneNumbers = phoneNumbers,
                            company = company,
                            email = email,
                            birthday = birthday,
                            address = address,
                            notes = notes,
                            tags = tags,
                            socialNetworks = socialNetworks
                        )
                    }
                ) {
                    Text("Guardar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            // FORM
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (uiState is ContactUiState.Error) {
                        Text(
                            text = (uiState as ContactUiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    SectionTitle("Información básica")
                    RippleTextField(firstName, { firstName = it }, "Nombre *", Icons.Default.Person)
                    RippleTextField(lastName, { lastName = it }, "Apellido *", Icons.Default.Person)
                    RippleTextField(company, { company = it }, "Empresa", Icons.Default.Business)

                    SectionTitle("Teléfonos")
                    phoneNumbers.forEachIndexed { index, phone ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = phone.suffix ?: "",
                                onValueChange = { s ->
                                    val newList = phoneNumbers.toMutableList()
                                    newList[index] = phone.copy(suffix = s)
                                    phoneNumbers = newList
                                },
                                label = { Text("+503") },
                                modifier = Modifier.width(80.dp),
                                shape = RoundedCornerShape(12.dp)
                            )
                            RippleTextField(
                                value = phone.number,
                                onValueChange = { n ->
                                    val newList = phoneNumbers.toMutableList()
                                    newList[index] = phone.copy(number = n)
                                    phoneNumbers = newList
                                },
                                label = "Número *",
                                icon = Icons.Default.Phone,
                                modifier = Modifier.weight(1f)
                            )
                            if (phoneNumbers.size > 1) {
                                IconButton(onClick = {
                                    phoneNumbers = phoneNumbers.filterIndexed { i, _ -> i != index }
                                }) {
                                    Icon(Icons.Default.RemoveCircleOutline, contentDescription = null, tint = Color.Red)
                                }
                            }
                        }
                    }
                    AddButton("Añadir teléfono") {
                        phoneNumbers = phoneNumbers + PhoneNumberInput(null, "")
                    }

                    SectionTitle("Redes Sociales")
                    socialNetworks.forEachIndexed { index, social ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RippleTextField(
                                value = social.network,
                                onValueChange = { n ->
                                    val newList = socialNetworks.toMutableList()
                                    newList[index] = social.copy(network = n)
                                    socialNetworks = newList
                                },
                                label = "Red (WhatsApp...)",
                                icon = Icons.Default.Share,
                                modifier = Modifier.weight(0.4f)
                            )
                            RippleTextField(
                                value = social.username,
                                onValueChange = { u ->
                                    val newList = socialNetworks.toMutableList()
                                    newList[index] = social.copy(username = u)
                                    socialNetworks = newList
                                },
                                label = "Usuario/Número",
                                icon = Icons.Default.AlternateEmail,
                                modifier = Modifier.weight(0.6f)
                            )
                            IconButton(onClick = {
                                socialNetworks = socialNetworks.filterIndexed { i, _ -> i != index }
                            }) {
                                Icon(Icons.Default.RemoveCircleOutline, contentDescription = null, tint = Color.Red)
                            }
                        }
                    }
                    AddButton("Añadir red social") {
                        socialNetworks = socialNetworks + SocialNetworkInput("", "")
                    }

                    SectionTitle("Más detalles")
                    RippleTextField(email, { email = it }, "Correo electrónico", Icons.Default.Email)
                    RippleTextField(birthday, { birthday = it }, "Cumpleaños (YYYY-MM-DD)", Icons.Default.Cake)
                    RippleTextField(address, { address = it }, "Dirección", Icons.Default.LocationOn)
                    RippleTextField(tags, { tags = it }, "Etiquetas (separadas por comas)", Icons.Default.Label)
                    
                    RippleTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = "Notas",
                        icon = Icons.Default.Notes,
                        singleLine = false,
                        modifier = Modifier.height(120.dp)
                    )
                }
            }
        }

        if (uiState is ContactUiState.Loading) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black.copy(alpha = 0.3f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = RippleAccent,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun RippleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = RippleAccent) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = singleLine,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = RippleAccent,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = RippleAccent,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun AddButton(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(Icons.Default.AddCircle, contentDescription = null, tint = RippleAccent, modifier = Modifier.size(20.dp))
        Text(label, color = RippleAccent, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
