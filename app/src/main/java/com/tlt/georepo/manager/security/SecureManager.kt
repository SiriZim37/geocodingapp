package com.tlt.georepo.manager.security

import android.content.Context
import android.os.Build
import android.util.Base64
import com.kazakago.cryptore.CipherAlgorithm
import com.kazakago.cryptore.Cryptore
import java.security.SecureRandom

class SecureManager private constructor() {

    private fun getCryptoreRSA(): Cryptore {
        val builder = Cryptore.Builder(ALIAS_RSA, CipherAlgorithm.RSA)
        builder.context = context
        return builder.build()
    }

    private fun getCryptoreAES(): Cryptore {
        val builder = Cryptore.Builder(ALIAS_AES, CipherAlgorithm.AES)
        return builder.build()
    }

    @Throws(Exception::class)
    private fun encrypt(plainByte: ByteArray): String {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> encryptAES(plainByte)
            else -> encryptRSA(plainByte)
        }
    }

    @Throws(Exception::class)
    private fun decrypt(encryptedStr: String): ByteArray {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> decryptAES(encryptedStr)
            else -> decryptRSA(encryptedStr)
        }
    }

    @Throws(Exception::class)
    private fun encryptRSA(plainByte: ByteArray): String {
        val result = getCryptoreRSA().encrypt(plainByte)
        return Base64.encodeToString(result.bytes, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    private fun encryptAES(plainByte: ByteArray): String {
        val result = getCryptoreAES().encrypt(plainByte)
        cipherIV = result.cipherIV
        return Base64.encodeToString(result.bytes, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    private fun decryptRSA(encryptedStr: String): ByteArray {
        val encryptedByte = Base64.decode(encryptedStr, Base64.DEFAULT)
        val result = getCryptoreRSA().decrypt(encryptedByte, null)
        return result.bytes
    }

    @Throws(Exception::class)
    private fun decryptAES(encryptedStr: String): ByteArray {
        val encryptedByte = Base64.decode(encryptedStr, Base64.DEFAULT)
        val result = getCryptoreAES().decrypt(encryptedByte, cipherIV)
        return result.bytes
    }

    fun getAppKey(): ByteArray {
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

        sharedPreferences.getString(KEY_PREF, null)?.let {
            return decrypt(it)
        }

        val key = ByteArray(64).apply {
            SecureRandom().nextBytes(this)
        }

        sharedPreferences.edit()
                .putString(KEY_PREF, encrypt(key))
                .apply()

        return key
    }

    fun generateSalt() = BCrypt.gensalt()

    fun hash(password: String, salt: String) = BCrypt.hashpw(password, salt)

    companion object {
        private val KEY_PREF = "TLT"
        private val ALIAS_RSA = "CIPHER_RSA"
        private val ALIAS_AES = "CIPHER_AES"
        private val CIPHER_IV_PREF = "cipher_iv"
        private val secureManager = SecureManager()
        private lateinit var context: Context

        fun init(context: Context) {
            this.context = context
        }

        fun getInstance() = secureManager
    }

    private var cipherIV: ByteArray?
        get() {
            val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

            sharedPreferences.getString(CIPHER_IV_PREF, null)?.let {
                return Base64.decode(it, Base64.DEFAULT)
            }
            return null
        }
        set(value) {
            context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                    .edit()
                    .putString(CIPHER_IV_PREF, Base64.encodeToString(value, Base64.DEFAULT))
                    .apply()
        }
}