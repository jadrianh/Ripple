package org.ripple.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import org.ripple.data.ContactRepository
import org.ripple.data.local.ContactWithDetails
import org.ripple.data.local.RippleDatabase
import org.ripple.data.tags.TagsRepository

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactRepository

    val searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    val contacts: StateFlow<List<ContactWithDetails>> =
        searchQuery
            .flatMapLatest { query ->
                val trimmed = query.trim()
                if (trimmed.isBlank()) {
                    repository.getAllContacts()
                } else {
                    repository.searchContacts(trimmed)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = emptyList()
            )

    val pinnedTags: StateFlow<List<String>> =
        TagsRepository.getPinnedTagsFlow(application.applicationContext)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    init {
        val dao = RippleDatabase.getInstance(application).contactDao()
        repository = ContactRepository(dao)
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }
}
