package org.ripple.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ripple.data.local.ContactWithDetails

private val avatarColors = listOf(
    Color(0xFF378ADD), Color(0xFF1D9E75), Color(0xFFD85A30),
    Color(0xFF7F77DD), Color(0xFFBA7517), Color(0xFF993556)
)

@Composable
fun ContactCard(
    contact: ContactWithDetails,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // LEFT: Circular avatar
            val initials = ((contact.contact.firstName.take(1)) + (contact.contact.lastName.take(1))).uppercase()
            val bgColor = avatarColors[contact.contact.id % 6]

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(bgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // RIGHT: Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${contact.contact.firstName} ${contact.contact.lastName}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                val phone = contact.phoneNumbers.firstOrNull()?.let {
                    if (!it.suffix.isNullOrEmpty()) "${it.suffix} ${it.number}" else it.number
                } ?: ""

                Text(
                    text = phone,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
