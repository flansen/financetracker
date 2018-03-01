package flhan.de.financemanager.base

import android.content.Intent
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

sealed class IntentDelegate<T>(protected val key: kotlin.String) : ReadWriteProperty<Intent, T> {

    class String(key: kotlin.String) : IntentDelegate<kotlin.String?>(key) {
        override fun getValue(thisRef: Intent, property: KProperty<*>): kotlin.String? {
            return if (thisRef.extras?.containsKey(key) == true) thisRef.getStringExtra(key) else null
        }

        override fun setValue(thisRef: Intent, property: KProperty<*>, value: kotlin.String?) {
            value ?: return
            thisRef.putExtra(key, value)
        }
    }
}