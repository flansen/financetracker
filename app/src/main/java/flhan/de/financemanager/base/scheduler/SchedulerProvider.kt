package flhan.de.financemanager.base.scheduler

import io.reactivex.Scheduler

/**
 * Created by fhansen on 24.10.17.
 */
interface SchedulerProvider {

    fun io(): Scheduler
    fun computation(): Scheduler
    fun main(): Scheduler

}