package com.example.protectedprojectwithcandc

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.security.spec.KeySpec
import java.util.*
import java.util.Base64.getEncoder
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


/**
 * If you want to use some features like lambda or delegating constructors, you’ll have to use C++11. in first time :)
* */

class MainActivity : AppCompatActivity() {

    private val secretKey = "boooooooooom!!!!"
    private val salt = "ssshhhhhhhhhhh!!!!"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method

        sample_text.text = stringFromJNI()
        btn_confirm.setOnClickListener {
            var encrypted = getValueJNI(et_value.text.toString())
//            var decrypted = decrypt(encrypted,secretKey)
            Toast.makeText(
                applicationContext,
                "encrypt : $encrypted\n",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * my refrenc for coding with one of values ->https://howtodoinjava.com/security/aes-256-encryption-decryption/
    * */

    @RequiresApi(Build.VERSION_CODES.O)
    fun encrypt(strToEncrypt: String, secret: String?): String? {
        try {
            val iv = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            val ivspec = IvParameterSpec(iv)
            val factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val spec: KeySpec = PBEKeySpec(
                secretKey.toCharArray(),
                salt.toByteArray(),
                65536,
                256
            )
            val tmp = factory.generateSecret(spec)
            val secretKey =
                SecretKeySpec(tmp.encoded, "AES")
            val cipher =
                Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec)
            return getEncoder()
                .encodeToString(cipher.doFinal(strToEncrypt.toByteArray(charset("UTF-8"))))
        } catch (e: java.lang.Exception) {
            println("Error while encrypting: $e")
        }
        return null
    }
    @RequiresApi(Build.VERSION_CODES.O)
    open fun decrypt(strToDecrypt: String?, secret: String?): String? {
        try {
            val iv = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            val ivspec = IvParameterSpec(iv)
            val factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val spec: KeySpec = PBEKeySpec(
                secretKey.toCharArray(),
                salt.toByteArray(),
                65536,
                256
            )
            val tmp = factory.generateSecret(spec)
            val secretKey =
                SecretKeySpec(tmp.encoded, "AES")
            val cipher =
                Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec)
            return String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)))
        } catch (e: Exception) {
            println("Error while decrypting: $e")
        }
        return null
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String
    external fun getValueJNI(s:String):String

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
