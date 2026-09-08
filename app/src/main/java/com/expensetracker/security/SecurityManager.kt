package com.expensetracker.security

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.GeneralSecurityException
import java.io.IOException

/**
 * SecurityManager handles biometric authentication and encrypted data storage
 */
class SecurityManager(private val context: Context) {

    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val encryptedPrefs by lazy {
        try {
            EncryptedSharedPreferences.create(
                context,
                "encrypted_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: GeneralSecurityException) {
            null
        } catch (e: IOException) {
            null
        }
    }

    /**
     * Check if biometric authentication is available
     */
    fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    /**
     * Get biometric authentication status
     */
    fun getBiometricStatus(): BiometricStatus {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricStatus.AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricStatus.NO_HARDWARE
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricStatus.HW_UNAVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricStatus.NONE_ENROLLED
            else -> BiometricStatus.UNAVAILABLE
        }
    }

    /**
     * Show biometric prompt for authentication
     */
    fun showBiometricPrompt(
        title: String = "Authenticate",
        subtitle: String = "Use your biometric to continue",
        negativeButtonText: String = "Cancel",
        onSuccess: () -> Unit,
        onError: (errorCode: Int, errorMessage: CharSequence) -> Unit,
        onFailed: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(context)
        
        val biometricPrompt = BiometricPrompt(
            context as? android.app.Activity ?: return,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode != BiometricPrompt.ERROR_USER_CANCELED && 
                        errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        onError(errorCode, errString)
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText(negativeButtonText)
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    /**
     * Save sensitive data securely
     */
    fun saveSecureData(key: String, value: String): Boolean {
        return try {
            encryptedPrefs?.edit()?.putString(key, value)?.apply()
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Retrieve sensitive data securely
     */
    fun getSecureData(key: String, defaultValue: String? = null): String? {
        return encryptedPrefs?.getString(key, defaultValue)
    }

    /**
     * Remove sensitive data
     */
    fun removeSecureData(key: String): Boolean {
        return try {
            encryptedPrefs?.edit()?.remove(key)?.apply()
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Check if app lock is enabled
     */
    fun isAppLockEnabled(): Boolean {
        return encryptedPrefs?.getBoolean("app_lock_enabled", false) ?: false
    }

    /**
     * Enable/disable app lock
     */
    fun setAppLockEnabled(enabled: Boolean) {
        encryptedPrefs?.edit()?.putBoolean("app_lock_enabled", enabled)?.apply()
    }

    /**
     * Set custom PIN for app lock (fallback)
     */
    fun setAppPin(pin: String): Boolean {
        return try {
            encryptedPrefs?.edit()?.putString("app_pin", pin)?.apply()
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Verify PIN
     */
    fun verifyPin(pin: String): Boolean {
        val storedPin = encryptedPrefs?.getString("app_pin", "")
        return storedPin == pin
    }

    enum class BiometricStatus {
        AVAILABLE,
        NO_HARDWARE,
        HW_UNAVAILABLE,
        NONE_ENROLLED,
        UNAVAILABLE
    }
}
