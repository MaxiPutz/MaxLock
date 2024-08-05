import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun isBioAuthExepted(
    context: Context,
    activity: FragmentActivity,
) : Boolean = suspendCoroutine { continuation ->
    if (!isBiometricAvailable(context)) {
        return@suspendCoroutine continuation.resume(false)
    }

    showBiometricPrompt(activity = activity, onError = {
       return@showBiometricPrompt continuation.resume(false)
    }, onSuccess = {
        return@showBiometricPrompt continuation.resume(true)
    } )
}

fun isBiometricAvailable(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
}

fun showBiometricPrompt(
    activity: FragmentActivity,
    onSuccess: () -> Unit,
    onError: () -> Unit
) {
    val executor = ContextCompat.getMainExecutor(activity)
    val biometricPrompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onError()
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric login for my app")
        .setSubtitle("Log in using your biometric credential")
        .setNegativeButtonText("Cancel")
        .build()

    biometricPrompt.authenticate(promptInfo)
}
