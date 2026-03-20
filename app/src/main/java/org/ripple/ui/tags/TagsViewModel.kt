package org.ripple.ui.tags

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.ripple.data.local.RippleDatabase

class TagsViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val contactDao = RippleDatabase.getInstance(application).contactDao()

    val tags: StateFlow<List<TagModel>> = TagsRepository.getTagsFlow(context)
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

    suspend fun isTagUsed(tagName: String): Boolean {
        // Simple scan of all contacts to see if tag is present in their comma-separated tags string
        val allContacts = contactDao.getAllContactsWithDetails().first()
        return allContacts.any { contact ->
            contact.contact.tags?.split(",")?.map { it.trim() }?.contains(tagName) == true
        }
    }
}
