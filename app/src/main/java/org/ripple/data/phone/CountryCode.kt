package org.ripple.data.phone

data class CountryCode(
    val flag: String,    // emoji flag
    val name: String,    // country name in Spanish
    val dial: String     // e.g. "+503"
)

val countryCodes = listOf(
    CountryCode("🇸🇻", "El Salvador", "+503"),
    CountryCode("🇺🇸", "Estados Unidos", "+1"),
    CountryCode("🇲🇽", "México", "+52"),
    CountryCode("🇬🇹", "Guatemala", "+502"),
    CountryCode("🇭🇳", "Honduras", "+504"),
    CountryCode("🇳🇮", "Nicaragua", "+505"),
    CountryCode("🇨🇷", "Costa Rica", "+506"),
    CountryCode("🇵🇦", "Panamá", "+507"),
    CountryCode("🇨🇴", "Colombia", "+57"),
    CountryCode("🇻🇪", "Venezuela", "+58"),
    CountryCode("🇵🇪", "Perú", "+51"),
    CountryCode("🇨🇱", "Chile", "+56"),
    CountryCode("🇦🇷", "Argentina", "+54"),
    CountryCode("🇧🇷", "Brasil", "+55"),
    CountryCode("🇪🇨", "Ecuador", "+593"),
    CountryCode("🇧🇴", "Bolivia", "+591"),
    CountryCode("🇵🇾", "Paraguay", "+595"),
    CountryCode("🇺🇾", "Uruguay", "+598"),
    CountryCode("🇩🇴", "Rep. Dominicana", "+1809"),
    CountryCode("🇨🇺", "Cuba", "+53"),
    CountryCode("🇪🇸", "España", "+34"),
    CountryCode("🇬🇧", "Reino Unido", "+44"),
    CountryCode("🇩🇪", "Alemania", "+49"),
    CountryCode("🇫🇷", "Francia", "+33"),
    CountryCode("🇮🇹", "Italia", "+39"),
    CountryCode("🇨🇳", "China", "+86"),
    CountryCode("🇯🇵", "Japón", "+81"),
    CountryCode("🇰🇷", "Corea del Sur", "+82"),
    CountryCode("🇮🇳", "India", "+91"),
    CountryCode("🇷🇺", "Rusia", "+7"),
    CountryCode("🇨🇦", "Canadá", "+1"),
    CountryCode("🇦🇺", "Australia", "+61")
)
