package org.ripple.data.tags

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.ripple.ui.theme.rippleDataStore
import java.io.IOException

object TagsRepository {
    private val USER_TAGS_KEY = stringPreferencesKey("user_tags")
    private val PINNED_TAGS_KEY = stringPreferencesKey("pinned_tags")
    
    private const val DEFAULT_TAGS = "Amigos,Familia,Trabajo,Otro"
    private const val DEFAULT_PINNED = "Amigos,Familia,Trabajo,Otro"

    fun getTagsFlow(context: Context): Flow<List<TagModel>> {
        return context.rippleDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val tagsString = preferences[USER_TAGS_KEY] ?: DEFAULT_TAGS
                if (tagsString.isBlank()) emptyList()
                else tagsString.split(",").map { createTagModel(it.trim()) }
            }
    }

    suspend fun addTag(context: Context, name: String) {
        val trimmedName = name.trim()
        if (trimmedName.isBlank()) return

        context.rippleDataStore.edit { preferences ->
            val currentTags = preferences[USER_TAGS_KEY] ?: DEFAULT_TAGS
            val tagList = currentTags.split(",").map { it.trim() }.toMutableList()
            
            if (!tagList.contains(trimmedName)) {
                tagList.add(trimmedName)
                preferences[USER_TAGS_KEY] = tagList.joinToString(",")
            }
        }
    }

    suspend fun deleteTag(context: Context, name: String) {
        context.rippleDataStore.edit { preferences ->
            val currentTags = preferences[USER_TAGS_KEY] ?: DEFAULT_TAGS
            val tagList = currentTags.split(",").map { it.trim() }.filter { it != name }
            preferences[USER_TAGS_KEY] = tagList.joinToString(",")
            
            // Also unpin if deleted
            val currentPinned = preferences[PINNED_TAGS_KEY] ?: DEFAULT_PINNED
            val pinnedList = currentPinned.split(",").map { it.trim() }.filter { it != name && it.isNotBlank() }
            preferences[PINNED_TAGS_KEY] = pinnedList.joinToString(",")
        }
    }

    fun getPinnedTagsFlow(context: Context): Flow<List<String>> {
        return context.rippleDataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences()) else throw exception
            }
            .map { preferences ->
                val pinnedString = preferences[PINNED_TAGS_KEY] ?: DEFAULT_PINNED
                if (pinnedString.isBlank()) emptyList()
                else pinnedString.split(",").map { it.trim() }.filter { it.isNotBlank() }
            }
    }

    suspend fun pinTag(context: Context, name: String) {
        context.rippleDataStore.edit { preferences ->
            val currentPinned = preferences[PINNED_TAGS_KEY] ?: DEFAULT_PINNED
            val pinnedList = currentPinned.split(",").map { it.trim() }.filter { it.isNotBlank() }.toMutableList()
            if (!pinnedList.contains(name)) {
                pinnedList.add(name)
                preferences[PINNED_TAGS_KEY] = pinnedList.joinToString(",")
            }
        }
    }

    suspend fun unpinTag(context: Context, name: String) {
        context.rippleDataStore.edit { preferences ->
            val currentPinned = preferences[PINNED_TAGS_KEY] ?: DEFAULT_PINNED
            val pinnedList = currentPinned.split(",").map { it.trim() }.filter { it != name && it.isNotBlank() }
            preferences[PINNED_TAGS_KEY] = pinnedList.joinToString(",")
        }
    }
}
