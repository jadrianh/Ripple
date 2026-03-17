package org.ripple.ui.addedit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.ripple.data.ContactRepository
import org.ripple.data.local.ContactEntity
import org.ripple.data.local.ContactWithDetails
import org.ripple.data.local.PhoneNumberEntity
import org.ripple.data.local.RippleDatabase
import org.ripple.data.local.SocialNetworkEntity

sealed class ContactUiState {
    data object Loading : ContactUiState()
    data class Success(val contact: ContactWithDetails?) : ContactUiState()
    data class Error(val message: String) : ContactUiState()
}

data class PhoneNumberInput(
    val suffix: String? = null,
    val number: String
)

data class SocialNetworkInput(
    val network: String,
    val username: String
)

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactRepository =
        ContactRepository(RippleDatabase.getInstance(application).contactDao())

    private val selectedContactId = MutableStateFlow<Int?>(null)

    private val _uiState = MutableStateFlow<ContactUiState>(ContactUiState.Loading)
    val uiState: StateFlow<ContactUiState> = _uiState

    val selectedContact: StateFlow<ContactWithDetails?> =
        selectedContactId
            .flatMapLatest { id ->
                if (id == null) flowOf(null) else repository.getContactById(id)
            }
            .catch { t ->
                _uiState.value = ContactUiState.Error(t.message ?: "Error cargando contacto")
                emit(null)
            }
            .onEach { contact ->
                val id = selectedContactId.value
                _uiState.value = when {
                    id == null -> ContactUiState.Success(null)
                    contact != null -> ContactUiState.Success(contact)
                    else -> ContactUiState.Error("Contacto no encontrado")
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = null
            )

    fun loadContact(id: Int) {
        _uiState.value = ContactUiState.Loading
        selectedContactId.value = id
    }

    fun saveContact(
        id: Int? = null,
        firstName: String,
        lastName: String,
        phoneNumbers: List<PhoneNumberInput>,
        photoUri: String? = null,
        company: String? = null,
        email: String? = null,
        birthday: String? = null,
        address: String? = null,
        notes: String? = null,
        tags: String? = null,
        socialNetworks: List<SocialNetworkInput> = emptyList()
    ) {
        val fn = firstName.trim()
        val ln = lastName.trim()
        val phones = phoneNumbers
            .map { PhoneNumberInput(suffix = it.suffix?.trim()?.ifBlank { null }, number = it.number.trim()) }
            .filter { it.number.isNotBlank() }

        if (fn.isBlank() || ln.isBlank() || phones.isEmpty()) {
            _uiState.value = ContactUiState.Error("Completa los campos obligatorios: nombre, apellido y teléfono")
            return
        }

        val contactEntity = ContactEntity(
            id = id ?: 0,
            firstName = fn,
            lastName = ln,
            photoUri = photoUri?.trim()?.ifBlank { null },
            company = company?.trim()?.ifBlank { null },
            email = email?.trim()?.ifBlank { null },
            birthday = birthday?.trim()?.ifBlank { null },
            address = address?.trim()?.ifBlank { null },
            notes = notes?.trim()?.ifBlank { null },
            tags = tags?.trim()?.ifBlank { null }
        )

        val phoneEntities = phones.map {
            PhoneNumberEntity(
                id = 0,
                contactId = id ?: 0,
                suffix = it.suffix,
                number = it.number
            )
        }

        val socialEntities = socialNetworks
            .map {
                SocialNetworkInput(
                    network = it.network.trim(),
                    username = it.username.trim()
                )
            }
            .filter { it.network.isNotBlank() && it.username.isNotBlank() }
            .map {
                SocialNetworkEntity(
                    id = 0,
                    contactId = id ?: 0,
                    network = it.network,
                    username = it.username
                )
            }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.value = ContactUiState.Loading
                val finalId = if (id == null) {
                    repository.insertContactWithDetails(
                        contact = contactEntity,
                        phoneNumbers = phoneEntities,
                        socialNetworks = socialEntities
                    )
                } else {
                    repository.updateContactWithDetails(
                        contact = contactEntity,
                        phoneNumbers = phoneEntities,
                        socialNetworks = socialEntities
                    )
                    id
                }
                selectedContactId.value = finalId
            } catch (t: Throwable) {
                _uiState.value = ContactUiState.Error(t.message ?: "Error guardando contacto")
            }
        }
    }

    fun deleteContact(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.value = ContactUiState.Loading
                repository.deleteContact(id)
                selectedContactId.value = null
                _uiState.value = ContactUiState.Success(null)
            } catch (t: Throwable) {
                _uiState.value = ContactUiState.Error(t.message ?: "Error eliminando contacto")
            }
        }
    }
}

