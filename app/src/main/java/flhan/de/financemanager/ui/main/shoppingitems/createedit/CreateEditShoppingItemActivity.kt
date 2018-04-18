package flhan.de.financemanager.ui.main.shoppingitems.createedit

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTextChanged
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.base.IntentDelegate
import flhan.de.financemanager.common.extensions.visible
import kotlinx.android.synthetic.main.activity_create_edit_shopping_item.*
import kotlinx.android.synthetic.main.toolbar.*
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
        supportActionBar?.apply {
            title = if (id == null) getString(R.string.create_edit_shopping_item_create) else getString(R.string.create_edit_shopping_item_edit)
            this.setDisplayHomeAsUpEnabled(true)
        }

        viewModel.itemName.observe(this, Observer { name ->
            if (name != create_edit_shopping_item_text.text.toString()) {
                create_edit_shopping_item_text.setText(name ?: "")
            }
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            loadingView.visible(isLoading ?: true)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            false
        }
    }

    @OnClick(R.id.create_edit_shopping_item_save)
    fun onSaveClicked() {
        viewModel.save { finish() }
    }

    @OnTextChanged(R.id.create_edit_shopping_item_text, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    fun onCauseChanged(cause: Editable) {
        viewModel.itemName.value = cause.toString()
    }

    companion object {
        var Intent.id by IntentDelegate.String("id")

        fun createIntent(context: Context, id: String?): Intent {
            return Intent(context, CreateEditShoppingItemActivity::class.java)
                    .apply { this.id = id }
        }
    }
}
