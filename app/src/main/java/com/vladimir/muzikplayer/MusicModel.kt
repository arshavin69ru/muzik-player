package com.vladimir.muzikplayer

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

class MusicModel(
    val title: String,
    val artist: String,
    private val duration: Long,
    private val id: String
) {
    fun getUri(): Uri {
        return ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            id.toLong()
        ) }

    public fun getMinutes(): String {
        return if (duration > 0) {
            val min: Long = (duration / 1000) / 60
            val sec: Long = (duration / 1000) % 60
            String.format("%d min %d sec", min, sec)
        } else {
            "?"
        }

    }
}