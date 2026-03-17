package org.ripple.data.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    @ColumnInfo(name = "photo_uri")
    val photoUri: String? = null,

    @ColumnInfo(name = "company")
    val company: String? = null,

    @ColumnInfo(name = "email")
    val email: String? = null,

    /**
     * Stored as ISO-8601 date string: "yyyy-MM-dd"
     */
    @ColumnInfo(name = "birthday")
    val birthday: String? = null,

    @ColumnInfo(name = "address")
    val address: String? = null,

    @ColumnInfo(name = "notes")
    val notes: String? = null,

    /**
     * Comma-separated list of tags, e.g. "Friends,Work"
     */
    @ColumnInfo(name = "tags")
    val tags: String? = null
)

@Entity(
    tableName = "phone_numbers",
    foreignKeys = [
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = ["id"],
            childColumns = ["contact_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["contact_id"])
    ]
)
data class PhoneNumberEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "contact_id")
    val contactId: Int,

    /**
     * Optional suffix, e.g. country code "+503" (El Salvador)
     */
    @ColumnInfo(name = "suffix")
    val suffix: String? = null,

    @ColumnInfo(name = "number")
    val number: String
)

@Entity(
    tableName = "social_networks",
    foreignKeys = [
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = ["id"],
            childColumns = ["contact_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["contact_id"])
    ]
)
data class SocialNetworkEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "contact_id")
    val contactId: Int,

    /**
     * Socialmedia name, e.g. "Instagram", "WhatsApp", "Telegram"
     */
    @ColumnInfo(name = "network")
    val network: String,

    @ColumnInfo(name = "username")
    val username: String
)

/**
 * Aggregated Room relation: Contact + PhoneNumbers + SocialNetworks
 */
data class ContactWithDetails(
    @Embedded
    val contact: ContactEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "contact_id"
    )
    val phoneNumbers: List<PhoneNumberEntity> = emptyList(),

    @Relation(
        parentColumn = "id",
        entityColumn = "contact_id"
    )
    val socialNetworks: List<SocialNetworkEntity> = emptyList()
)