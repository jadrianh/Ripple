package org.ripple.ui.addedit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ripple.data.phone.CountryCode
import org.ripple.data.phone.countryCodes
import org.ripple.data.tags.TagModel
import org.ripple.ui.tags.TagsViewModel
import org.ripple.ui.theme.RippleAccent
import org.ripple.ui.theme.RippleDark
import org.ripple.ui.theme.RippleGradientBrush
import java.util.Calendar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditContactScreen(
    contactId: Int?,
    viewModel: ContactViewModel,
    onCancel: () -> Unit,
    onSaved: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val contact by viewModel.selectedContact.collectAsState()
    val tagsViewModel: TagsViewModel = viewModel()
    val allAvailableTags by tagsViewModel.tags.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumbers by remember { mutableStateOf(listOf(PhoneNumberInput("+503", ""))) }
    var socialNetworks by remember { mutableStateOf(listOf<SocialNetworkInput>()) }
    var birthday by remember { mutableStateOf("") } // Format: YYYY-MM
    var address by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedTagNames by remember { mutableStateOf(listOf<String>()) }

    var showAddTagDialog by remember { mutableStateOf(false) }
    var showBirthdayPicker by remember { mutableStateOf(false) }
    var countryPickerIndex by remember { mutableStateOf<Int?>(null) }
    var isSavingInitiated by remember { mutableStateOf(false) }

    LaunchedEffect(contactId) {
        if (contactId != null) {
            viewModel.loadContact(contactId)
        }
    }

    LaunchedEffect(contact) {
        contact?.let { c ->
            firstName = c.contact.firstName
            lastName = c.contact.lastName
            company = c.contact.company ?: ""
            email = c.contact.email ?: ""
            birthday = c.contact.birthday?.take(7) ?: "" // Parse YYYY-MM
            address = c.contact.address ?: ""
            notes = c.contact.notes ?: ""
            selectedTagNames = c.contact.tags?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()
            
            if (c.phoneNumbers.isNotEmpty()) {
                phoneNumbers = c.phoneNumbers.map { PhoneNumberInput(it.suffix ?: "+503", it.number) }
            }
            if (c.socialNetworks.isNotEmpty()) {
                socialNetworks = c.socialNetworks.map { SocialNetworkInput(it.network, it.username) }
            }
        }
    }

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
                            birthday = birthday, // Saves as YYYY-MM
                            address = address,
                            notes = notes,
                            tags = selectedTagNames.joinToString(","),
                            socialNetworks = socialNetworks
                        )
                    }
                ) {
                    Text("Guardar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

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
                            val currentCountry = countryCodes.find { it.dial == phone.suffix } ?: countryCodes[0]
                            
                            Surface(
                                modifier = Modifier
                                    .height(56.dp)
                                    .clickable { countryPickerIndex = index },
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                color = Color.Transparent
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = currentCountry.flag, fontSize = 18.sp)
                                    Text(text = currentCountry.dial, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

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
                        phoneNumbers = phoneNumbers + PhoneNumberInput("+503", "")
                    }

                    SectionTitle("Etiquetas")
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allAvailableTags.forEach { tag ->
                            val isSelected = selectedTagNames.contains(tag.name)
                            TagChip(
                                tag = tag,
                                isSelected = isSelected,
                                onClick = {
                                    selectedTagNames = if (isSelected) {
                                        selectedTagNames.filter { it != tag.name }
                                    } else {
                                        selectedTagNames + tag.name
                                    }
                                }
                            )
                        }
                        
                        Surface(
                            modifier = Modifier.clickable { showAddTagDialog = true },
                            shape = RoundedCornerShape(16.dp),
                            color = Color.Transparent,
                            border = androidx.compose.foundation.BorderStroke(1.dp, RippleAccent)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = RippleAccent, modifier = Modifier.size(16.dp))
                                Text("+ Nueva", color = RippleAccent, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }
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
                    
                    // BIRTHDAY PICKER
                    val birthdayDisplay = if (birthday.length >= 7) {
                        val parts = birthday.split("-")
                        "${parts[1]}/${parts[0]}"
                    } else ""
                    
                    OutlinedTextField(
                        value = birthdayDisplay,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Cumpleaños") },
                        leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null, tint = RippleAccent) },
                        trailingIcon = { 
                            IconButton(onClick = { showBirthdayPicker = true }) {
                                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = RippleAccent)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showBirthdayPicker = true },
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledLeadingIconColor = RippleAccent,
                            disabledTrailingIconColor = RippleAccent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    RippleTextField(address, { address = it }, "Dirección", Icons.Default.LocationOn)
                    
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
        
        if (showAddTagDialog) {
            org.ripple.ui.tags.AddTagDialog(
                existingTags = allAvailableTags.map { it.name },
                onDismiss = { showAddTagDialog = false },
                onConfirm = { name ->
                    tagsViewModel.addTag(name)
                    selectedTagNames = selectedTagNames + name
                    showAddTagDialog = false
                }
            )
        }

        if (showBirthdayPicker) {
            MonthYearPickerDialog(
                currentValue = birthday,
                onDismiss = { showBirthdayPicker = false },
                onConfirm = { newDate ->
                    birthday = newDate
                    showBirthdayPicker = false
                }
            )
        }

        countryPickerIndex?.let { index ->
            CountryPickerDialog(
                onDismiss = { countryPickerIndex = null },
                onSelect = { country ->
                    val newList = phoneNumbers.toMutableList()
                    newList[index] = phoneNumbers[index].copy(suffix = country.dial)
                    phoneNumbers = newList
                    countryPickerIndex = null
                }
            )
        }
    }
}

@Composable
private fun MonthYearPickerDialog(
    currentValue: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    var selectedYear by remember { 
        mutableStateOf(if (currentValue.length >= 4) currentValue.take(4).toInt() else currentYear) 
    }
    var selectedMonth by remember { 
        mutableStateOf(if (currentValue.length >= 7) currentValue.substring(5, 7).toInt() else 1) 
    }

    val months = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar Fecha") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Year Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { selectedYear-- }) { Icon(Icons.Default.ChevronLeft, null) }
                    Text(text = selectedYear.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = RippleAccent)
                    IconButton(onClick = { if (selectedYear < currentYear) selectedYear++ }) { Icon(Icons.Default.ChevronRight, null) }
                }

                // Month Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(200.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(months.indices.toList()) { index ->
                        val monthNum = index + 1
                        val isSelected = selectedMonth == monthNum
                        Surface(
                            modifier = Modifier.clickable { selectedMonth = monthNum },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) RippleAccent else Color.Transparent,
                            border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
                        ) {
                            Box(modifier = Modifier.padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = months[index],
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val formattedMonth = selectedMonth.toString().padStart(2, '0')
                onConfirm("$selectedYear-$formattedMonth")
            }) { Text("Confirmar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun CountryPickerDialog(
    onDismiss: () -> Unit,
    onSelect: (CountryCode) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredCountries = remember(searchQuery) {
        countryCodes.filter { 
            it.name.contains(searchQuery, ignoreCase = true) || it.dial.contains(searchQuery) 
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar país") },
        text = {
            Column(modifier = Modifier.fillMaxHeight(0.7f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(filteredCountries) { country ->
                        ListItem(
                            modifier = Modifier.clickable { onSelect(country) },
                            headlineContent = { Text(country.name) },
                            leadingContent = { Text(country.flag, fontSize = 24.sp) },
                            trailingContent = { Text(country.dial, color = Color.Gray, fontWeight = FontWeight.Medium) }
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun TagChip(
    tag: TagModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) tag.color.copy(alpha = 0.6f) else Color.Transparent,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
    ) {
        Text(
            text = tag.name,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = RippleDark,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
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
