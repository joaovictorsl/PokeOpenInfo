package me.joaovictorsl.pokeopeninfo.datasource

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import me.joaovictorsl.pokeopeninfo.model.Pokemon
import kotlinx.coroutines.CoroutineScope
import me.joaovictorsl.pokeopeninfo.retrofit.PokemonImageEndpoint
import me.joaovictorsl.pokeopeninfo.retrofit.getRetrofitInstance

class PokemonBitmapFetcher(
    private val context: Context
    ) {
    private val TAG = "PokemonBitmapFetcher"
    private val pkmImgService = getRetrofitInstance("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/")
        .create(PokemonImageEndpoint::class.java)

    suspend fun fetch(pkmList: List<Pokemon>) {
        for (idx in 0..pkmList.lastIndex) {
            val pkm = pkmList[idx]

            if (noLocalImgAvailable(pkm.pokeApiId)) {
                val byteArray = fetchImg(pkm)
                if (byteArray != null) storeInFilesDir(pkm.pokeApiId, byteArray)
            }
        }
    }

    private fun noLocalImgAvailable(pkmId: Int) = "$pkmId.png" !in context.fileList()

    private suspend fun fetchImg(pkm: Pokemon): ByteArray? {
        var imgByteArray: ByteArray? = null

        pkm.imgUrl?.let { _ ->
            try {
                val res = pkmImgService.getImage(pkm.pokeApiId)
                val imgByteStream = res.byteStream()

                // Get the image dimensions without decoding the entire image
//            val options = BitmapFactory.Options()
//            options.inJustDecodeBounds = true
//            BitmapFactory.decodeStream(imgByteStream, null, options)

                // Set the inSampleSize property to reduce the image size
//            options.inSampleSize = calculateInSampleSize(options, 100, 100)

                // Decode the image with the calculated inSampleSize
                imgByteArray = imgByteStream.readBytes()
                res.close()
                imgByteStream.close()

                Log.i(TAG, "Fetching Pokemon bitmap completed ${imgByteArray?.size} $pkm")
            } catch (e: retrofit2.HttpException) {
                Log.e(TAG, "Image not found for ${pkm.name} (${pkm.pokeApiId})")
            }

        }

        return imgByteArray
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        maxWidth: Int,
        maxHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > maxHeight || width > maxWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= maxHeight && halfWidth / inSampleSize >= maxWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun storeInFilesDir(pkmId:Int, imgAsByteArray: ByteArray) {
        context.openFileOutput("$pkmId.png", Context.MODE_PRIVATE).use {
            it.write(imgAsByteArray)
            Log.i(TAG, "Writing end")
        }
    }
}
