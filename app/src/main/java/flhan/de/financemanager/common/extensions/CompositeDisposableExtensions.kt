package flhan.de.financemanager.common.extensions

import io.reactivex.disposables.CompositeDisposable

fun CompositeDisposable.cleanUp() {
    clear()
    dispose()
}