package flhan.de.financemanager.overview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import flhan.de.financemanager.R
import flhan.de.financemanager.base.app
import flhan.de.financemanager.di.overview.OverviewModule
import javax.inject.Inject


class OverviewActivity : AppCompatActivity(), OverviewContract.View {
    private val component by lazy { app.appComponent.plus(OverviewModule(this)) }

    @Inject
    lateinit var presenter: OverviewContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        component.inject(this)
    }
}
