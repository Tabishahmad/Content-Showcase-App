package com.example.fitpeo.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fitpeo.common.DATABASE_TABLE_NAME
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = DATABASE_TABLE_NAME)
@Parcelize
data class Album(val id: Int,@PrimaryKey
                 val name: String,
                 @SerializedName("poster-image")
                 val posterImage: String,
                 var isFav:Boolean = false): Parcelable