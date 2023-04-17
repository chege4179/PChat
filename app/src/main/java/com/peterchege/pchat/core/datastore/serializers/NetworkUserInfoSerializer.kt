package com.peterchege.pchat.core.datastore.serializers

import androidx.datastore.core.Serializer
import com.peterchege.pchat.domain.models.NetworkUser
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object NetworkUserInfoSerializer : Serializer<NetworkUser?> {
    override val defaultValue: NetworkUser?
        get() = null

    override suspend fun readFrom(input: InputStream): NetworkUser? {
        return try {
            Json.decodeFromString(
                deserializer = NetworkUser.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: NetworkUser?, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = NetworkUser.serializer(),
                value = t ?: NetworkUser(
                    email = "",
                    googleId = "",
                    fullName = "",
                    userId = "",
                    imageUrl = "",


                    )
            ).encodeToByteArray()
        )
    }
}