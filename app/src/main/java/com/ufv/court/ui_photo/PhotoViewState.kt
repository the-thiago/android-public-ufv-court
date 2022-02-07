package com.ufv.court.ui_photo

data class PhotoViewState(
    val image: String = ""
) {
    companion object {
        val Empty = PhotoViewState()
    }
}
