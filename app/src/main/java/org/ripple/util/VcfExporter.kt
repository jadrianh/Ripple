package org.ripple.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import org.ripple.data.local.ContactWithDetails
import java.io.File
import java.io.FileOutputStream

object VcfExporter {
    fun export(context: Context, contacts: List<ContactWithDetails>) {
        try {
            if (contacts.isEmpty()) return

            val vcfString = StringBuilder()
            for (contact in contacts) {
                vcfString.append("BEGIN:VCARD\n")
                vcfString.append("VERSION:3.0\n")
                vcfString.append("FN:${contact.contact.firstName} ${contact.contact.lastName}\n")
                vcfString.append("N:${contact.contact.lastName};${contact.contact.firstName};;;\n")

                contact.phoneNumbers.forEach { phone ->
                    val fullNumber = if (!phone.suffix.isNullOrBlank()) "${phone.suffix}${phone.number}" else phone.number
                    vcfString.append("TEL;TYPE=CELL:$fullNumber\n")
                }

                if (!contact.contact.email.isNullOrBlank()) {
                    vcfString.append("EMAIL;TYPE=INTERNET:${contact.contact.email}\n")
                }

                if (!contact.contact.company.isNullOrBlank()) {
                    vcfString.append("ORG:${contact.contact.company}\n")
                }

                if (!contact.contact.birthday.isNullOrBlank()) {
                    vcfString.append("BDAY:${contact.contact.birthday}\n")
                }

                if (!contact.contact.address.isNullOrBlank()) {
                    vcfString.append("ADR;TYPE=HOME:;;${contact.contact.address};;;;\n")
                }

                if (!contact.contact.notes.isNullOrBlank()) {
                    vcfString.append("NOTE:${contact.contact.notes}\n")
                }

                contact.socialNetworks.forEach { social ->
                    vcfString.append("X-NETWORK;TYPE=${social.network}:${social.username}\n")
                }

                vcfString.append("END:VCARD\n")
            }

            val cacheFile = File(context.cacheDir, "contacts.vcf")
            FileOutputStream(cacheFile).use { fos ->
                fos.write(vcfString.toString().toByteArray())
            }

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "org.ripple.fileprovider",
                cacheFile
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/vcard"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(
                Intent.createChooser(intent, "Exportar contactos")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } catch (e: Exception) {
            Log.e("VcfExporter", "Export failed", e)
            throw e
        }
    }
}
