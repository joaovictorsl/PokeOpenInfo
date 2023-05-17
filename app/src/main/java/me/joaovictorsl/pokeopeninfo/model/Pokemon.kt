package me.joaovictorsl.pokeopeninfo.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.File
import java.io.FileInputStream

@Entity
@Parcelize
data class Pokemon(
    @PrimaryKey val pokeApiId: Int,
    val name: String,
    var imgUrl: String? = null
) : Comparable<Pokemon>, Parcelable {

    override fun compareTo(other: Pokemon): Int {
        return pokeApiId - other.pokeApiId
    }

    fun isUpToDate(remotePkm: Pokemon): Boolean {
        if (this.pokeApiId != remotePkm.pokeApiId)
            throw IllegalArgumentException("Pokemons are not the same, ids are different.")

        if (remotePkm.imgUrl == null) return true

        return name == remotePkm.name &&
                imgUrl == remotePkm.imgUrl
    }

}
