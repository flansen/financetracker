package flhan.de.financemanager.ui.main.shoppingitems.createedit

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import butterknife.ButterKnife
import butterknife.OnClick
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.base.IntentDelegate
import kotlinx.android.synthetic.main.activity_create_edit_shopping_item.*
import javax.inject.Inject

class CreateEditShoppingItemActivity : BaseActivity() {

    val id by lazy { intent.id }

    @Inject
    lateinit var factory: CreateEditShoppingItemViewModelFactory

    lateinit var viewModel: CreateEditShoppingItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_edit_shopping_item)
        ButterKnife.bind(this)
        viewModel = ViewModelProviders.of(this, factory).get(CreateEditShoppingItemViewModel::class.java)
        setSupportActionBar(toolbar)
        supportActionBar?.title = if (id == null) getString(R.string.create_edit_shopping_item_create) else getString(R.string.create_edit_shopping_item_edit)
    }

    @OnClick(R.id.create_edit_shopping_item_save)
    fun onSaveClicked() {
        viewModel.save()
    }

    companion object {
        var Intent.id by IntentDelegate.String("id")

        fun createIntent(context: Context, id: String?): Intent {
            return Intent(context, CreateEditShoppingItemActivity::class.java)
                    .apply { this.id = id }
        }
    }
}
