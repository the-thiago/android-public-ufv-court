package com.ufv.court.app.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ufv.court.R

val rubikFamily = FontFamily(
    Font(R.font.rubik_light, FontWeight.Light),
    Font(R.font.rubik_regular, FontWeight.Normal),
    Font(R.font.rubik_medium, FontWeight.Medium)
)

val poppinsFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

val Typography = Typography(
    /**
     * Used for large titles in Login Team
     */
    h3 = TextStyle(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        color = BlackRock
    ),
    /**
     * Used for large titles in Store
     */
    h4 = TextStyle(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 48.sp,
        color = BlackRock
    ),
    /**
     * Used in Toolbar Title and PublicationDetailTitle
     */
    h5 = TextStyle(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 30.sp,
        color = BlackRock
    ),
    h6 = TextStyle(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    /**
     * Title in teams cards
     */
    subtitle1 = TextStyle(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 32.sp,
        color = BlackRock
    ),
    /**
     * Title for pictures in teams
     */
    subtitle2 = TextStyle(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        color = BlackRock
    ),
    /**
     * TextStyle for large body
     * When used on background or next to a cation use [ShipCove]
     * If on surface use [BlackRock]
     */
    body1 = TextStyle(
        fontFamily = rubikFamily,
        fontWeight = FontWeight.Light,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    /**
     * text body used on comments of publication
     */
    body2 = TextStyle(
        fontFamily = rubikFamily,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    /**
     * text for buttons
     */
    button = TextStyle(
        fontFamily = rubikFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    /**
     * Text used in labels of textFields, chips
     */
    caption = TextStyle(
        fontFamily = rubikFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 16.sp,
    ),
    overline = TextStyle(
        fontFamily = rubikFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 14.sp,
    )
)