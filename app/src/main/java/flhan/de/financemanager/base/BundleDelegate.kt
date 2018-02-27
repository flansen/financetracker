package flhan.de.financemanager.base

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

sealed class BundleDelegate<T>(protected val key: kotlin.String) : ReadWriteProperty<Bundle, T> {

    class Int(key: kotlin.String) : BundleDelegate<kotlin.Int?>(key) {
        override fun getValue(thisRef: Bundle, property: KProperty<*>): kotlin.Int? {
            return if (thisRef.containsKey(key)) thisRef.getInt(key) else null
        }

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: kotlin.Int?) {
            value ?: return
            thisRef.putInt(key, value)
        }
    }

    class String(key: kotlin.String) : BundleDelegate<kotlin.String?>(key) {
        override fun getValue(thisRef: Bundle, property: KProperty<*>): kotlin.String? {
            return if (thisRef.containsKey(key)) thisRef.getString(key) else null
        }

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: kotlin.String?) {
            value ?: return
            thisRef.putString(key, value)
        }
    }

}