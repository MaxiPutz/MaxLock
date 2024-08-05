package com.maxiputz.keychainclient.controller

import android.os.Build
import androidx.annotation.RequiresApi
import com.maxiputz.keychainclient.structs.ResponsData
import java.security.MessageDigest
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@RequiresApi(Build.VERSION_CODES.O)
fun tryDecryptCheck(password: String, responsData: ResponsData) : ResponsData  {
    try {
        val decryptedCheck = decrypt(password, responsData.check)
        if (decryptedCheck == "pass") {
            responsData.check = decryptedCheck
            responsData.secret = decrypt(password = password, responsData.secret)
        } else {
            return responsData
        }
    } catch (e: Exception) {
        return responsData
    }
    return responsData
}


@RequiresApi(Build.VERSION_CODES.O)
private fun decrypt(password: String, cryptoText: String): String {
    val key = deriveKey( password)

    val decodedCipherText = Base64.getDecoder().decode(cryptoText)
    val iv = decodedCipherText.copyOfRange(0, 16)
    val cipherText = decodedCipherText.copyOfRange(16, decodedCipherText.size)

    val secretKey = SecretKeySpec(key, "AES")

    val cipher = Cipher.getInstance("AES/CFB/NoPadding")
    val ivSpec = IvParameterSpec(iv)

    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

    val decryptedBytes = cipher.doFinal(cipherText)


    return String(Base64.getDecoder().decode(decryptedBytes), Charsets.UTF_8)
}


private fun deriveKey(password: String): ByteArray {
    val digest = MessageDigest.getInstance("SHA-256")
    val key = digest.digest(password.toByteArray(Charsets.UTF_8))
    println(password)
    println("Derived key in Andriod app: ${key.toString()}")
    return key
}
