package org.ripple.data.tags

import androidx.compose.ui.graphics.Color
import kotlin.math.absoluteValue

enum class TagColor(val hex: String) {
    RED("#FFCDD2"),
    YELLOW("#FFF9C4"),
    GREEN("#C8E6C9"),
    TEAL("#B2EBF2"),
    BLUE("#BBDEFB"),
    PURPLE("#E1BEE7"),
    GREY("#F5F5F5")
}

data class TagModel(
    val name: String,
    val colorHex: String
) {
    val color: Color get() = Color(android.graphics.Color.parseColor(colorHex))
}

fun createTagModel(name: String): TagModel {
    val colors = TagColor.entries
    val color = colors[name.hashCode().absoluteValue % colors.size]
    return TagModel(name, color.hex)
}
