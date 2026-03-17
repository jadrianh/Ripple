package org.ripple.data

import kotlinx.coroutines.flow.Flow
import org.ripple.data.local.ContactDao
import org.ripple.data.local.ContactEntity
import org.ripple.data.local.ContactWithDetails
import org.ripple.data.local.PhoneNumberEntity
import org.ripple.data.local.SocialNetworkEntity

class ContactRepository(
    private val contactDao: ContactDao
) {
    fun getAllContacts(): Flow<List<ContactWithDetails>> = contactDao.getAllContactsWithDetails()

    fun searchContacts(query: String): Flow<List<ContactWithDetails>> = contactDao.searchContacts(query)

    fun getContactById(id: Int): Flow<ContactWithDetails?> = contactDao.getContactById(id)

    suspend fun insertContactWithDetails(
        contact: ContactEntity,
        phoneNumbers: List<PhoneNumberEntity>,
        socialNetworks: List<SocialNetworkEntity>
    ): Int = contactDao.insertContactWithDetails(
        contact = contact,
        phoneNumbers = phoneNumbers,
        socialNetworks = socialNetworks
    )

    suspend fun updateContactWithDetails(
        contact: ContactEntity,
        phoneNumbers: List<PhoneNumberEntity>,
        socialNetworks: List<SocialNetworkEntity>
    ) {
        contactDao.updateContactWithDetails(
            contact = contact,
            phoneNumbers = phoneNumbers,
            socialNetworks = socialNetworks
        )
    }

    suspend fun deleteContact(id: Int) {
        contactDao.deleteContactById(id)
    }
}

