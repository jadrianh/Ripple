package org.ripple.ui.tags

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.ripple.data.local.RippleDatabase
import org.ripple.data.tags.TagModel
import org.ripple.data.tags.TagsRepository

sealed class TagsUiEvent {
    data object MaxPinnedReached : TagsUiEvent()
}

class TagsViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val contactDao = RippleDatabase.getInstance(application).contactDao()

    private val _uiEvent = MutableSharedFlow<TagsUiEvent>()
    val uiEvent: SharedFlow<TagsUiEvent> = _uiEvent

    val tags: StateFlow<List<TagModel>> = TagsRepository.getTagsFlow(context)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val pinnedTags: StateFlow<List<String>> = TagsRepository.getPinnedTagsFlow(context)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTag(name: String) {
        viewModelScope.launch {
            TagsRepository.addTag(context, name)
        }
    }

    fun deleteTag(name: String) {
        viewModelScope.launch {
            TagsRepository.deleteTag(context, name)
        }
    }

    fun togglePin(tagName: String) {
        viewModelScope.launch {
            val isCurrentlyPinned = pinnedTags.value.contains(tagName)
            if (isCurrentlyPinned) {
                TagsRepository.unpinTag(context, tagName)
            } else {
                if (pinnedTags.value.size < 6) {
                    TagsRepository.pinTag(context, tagName)
                } else {
                    _uiEvent.emit(TagsUiEvent.MaxPinnedReached)
                }
            }
        }
    }

    suspend fun isTagUsed(tagName: String): Boolean {
        val allContacts = contactDao.getAllContactsWithDetails().first()
        return allContacts.any { contact ->
            contact.contact.tags?.split(",")?.map { it.trim() }?.contains(tagName) == true
        }
    }
}
