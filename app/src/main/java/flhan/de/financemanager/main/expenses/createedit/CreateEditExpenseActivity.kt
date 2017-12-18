package flhan.de.financemanager.main.expenses.createedit

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import butterknife.ButterKnife
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import javax.inject.Inject

class CreateEditExpenseActivity : BaseActivity() {

    @Inject
    lateinit var factory: CreateEditExpenseViewModelFactory

    private lateinit var viewModel: CreateEditExpenseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_create_edit)
        ButterKnife.bind(this)

        viewModel = ViewModelProviders.of(this, factory).get(CreateEditExpenseViewModel::class.java)
        viewModel.initialize(intent.getStringExtra(ID_KEY))
    }

    companion object {
        private const val ID_KEY = "id"

        fun createIntent(context: Context, id: String): Intent {
            val intent = Intent(context, CreateEditExpenseActivity::class.java)
            intent.extras.putString(ID_KEY, id)
            return intent
        }
    }
}