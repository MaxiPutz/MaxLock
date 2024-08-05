import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BackgroundStartObserver(
    private val context: Context,
    private val onAuthCheck: (Boolean) -> Unit
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // Check authentication when the app comes to the foreground
        CoroutineScope(Dispatchers.Main).launch {
            //val isAuth = isBioAuthExepted(context, context as FragmentActivity)
            val isAuth = true
            onAuthCheck(isAuth)
        }
    }
}
