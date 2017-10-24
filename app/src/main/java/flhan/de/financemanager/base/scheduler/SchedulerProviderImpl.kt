package flhan.de.financemanager.base.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by fhansen on 24.10.17.
 */
class SchedulerProviderImpl : SchedulerProvider {
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun main(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}