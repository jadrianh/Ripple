package org.ripple.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    // ---- Contacts (base table) ----

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertContact(contact: ContactEntity): Long

    @Update
    suspend fun updateContact(contact: ContactEntity)

    @Delete
    suspend fun deleteContact(contact: ContactEntity)

    @Query("DELETE FROM contacts WHERE id = :contactId")
    suspend fun deleteContactById(contactId: Int)

    @Query("SELECT * FROM contacts WHERE id = :contactId LIMIT 1")
    suspend fun getContactEntityById(contactId: Int): ContactEntity?

    // ---- Phone numbers ----

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoneNumbers(phoneNumbers: List<PhoneNumberEntity>)

    @Query("DELETE FROM phone_numbers WHERE contact_id = :contactId")
    suspend fun deletePhoneNumbersForContact(contactId: Int)

    // ---- Social networks ----

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSocialNetworks(socialNetworks: List<SocialNetworkEntity>)

    @Query("DELETE FROM social_networks WHERE contact_id = :contactId")
    suspend fun deleteSocialNetworksForContact(contactId: Int)

    // ---- Relations ----

    @Transaction
    @Query("SELECT * FROM contacts ORDER BY last_name COLLATE NOCASE, first_name COLLATE NOCASE")
    fun getAllContactsWithDetails(): Flow<List<ContactWithDetails>>

    @Transaction
    @Query("SELECT * FROM contacts WHERE id = :contactId LIMIT 1")
    fun getContactById(contactId: Int): Flow<ContactWithDetails?>

    /**
     * Case-insensitive search by first or last name.
     * Matches substring anywhere, e.g. "an" -> "Andrés", "Santana".
     */
    @Transaction
    @Query(
        """
        SELECT * FROM contacts
        WHERE (first_name LIKE '%' || :query || '%' COLLATE NOCASE)
           OR (last_name  LIKE '%' || :query || '%' COLLATE NOCASE)
        ORDER BY last_name COLLATE NOCASE, first_name COLLATE NOCASE
        """
    )
    fun searchContacts(query: String): Flow<List<ContactWithDetails>>

    // ---- Transactional helpers for full upsert-like operations ----

    /**
     * Inserts a contact and its details (phones + socials) atomically.
     * Returns the new contactId.
     */
    @Transaction
    suspend fun insertContactWithDetails(
        contact: ContactEntity,
        phoneNumbers: List<PhoneNumberEntity>,
        socialNetworks: List<SocialNetworkEntity>
    ): Int {
        val newId = insertContact(contact).toInt()
        if (phoneNumbers.isNotEmpty()) {
            insertPhoneNumbers(phoneNumbers.map { it.copy(contactId = newId) })
        }
        if (socialNetworks.isNotEmpty()) {
            insertSocialNetworks(socialNetworks.map { it.copy(contactId = newId) })
        }
        return newId
    }

    /**
     * Updates a contact and fully replaces its details (phones + socials) atomically.
     */
    @Transaction
    suspend fun updateContactWithDetails(
        contact: ContactEntity,
        phoneNumbers: List<PhoneNumberEntity>,
        socialNetworks: List<SocialNetworkEntity>
    ) {
        updateContact(contact)
        deletePhoneNumbersForContact(contact.id)
        deleteSocialNetworksForContact(contact.id)
        if (phoneNumbers.isNotEmpty()) {
            insertPhoneNumbers(phoneNumbers.map { it.copy(contactId = contact.id, id = 0) })
        }
        if (socialNetworks.isNotEmpty()) {
            insertSocialNetworks(socialNetworks.map { it.copy(contactId = contact.id, id = 0) })
        }
    }
}

