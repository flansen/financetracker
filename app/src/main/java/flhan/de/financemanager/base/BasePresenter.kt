package flhan.de.financemanager.base

import flhan.de.financemanager.base.scheduler.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by fhansen on 24.10.17.
 */
abstract class BasePresenter(protected val scheduler: SchedulerProvider) {
    protected val disposables: CompositeDisposable = CompositeDisposable()
}