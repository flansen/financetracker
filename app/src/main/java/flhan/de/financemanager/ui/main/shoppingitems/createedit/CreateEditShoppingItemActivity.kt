package flhan.de.financemanager.ui.main.shoppingitems.createedit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import flhan.de.financemanager.R
import flhan.de.financemanager.base.IntentDelegate
import kotlinx.android.synthetic.main.activity_create_edit_shopping_item.*

class CreateEditShoppingItemActivity : AppCompatActivity() {

    val id by lazy { intent.id }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_edit_shopping_item)
        setSupportActionBar(toolbar)
        supportActionBar?.title = if (id == null) getString(R.string.create_edit_shopping_item_create) else getString(R.string.create_edit_shopping_item_edit)
    }

    companion object {
        var Intent.id by IntentDelegate.String("id")

        fun createIntent(context: Context, id: String?): Intent {
            return Intent(context, CreateEditShoppingItemActivity::class.java)
                    .apply { this.id = id }
        }
    }
}
