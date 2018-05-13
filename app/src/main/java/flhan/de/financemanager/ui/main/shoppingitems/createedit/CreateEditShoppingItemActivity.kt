package flhan.de.financemanager.ui.main.shoppingitems.createedit

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
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

        viewModel.selectedTag.observe(this, Observer {
            val tagString = it ?: return@Observer
            if (tagString != create_edit_shopping_item_tag.text.toString()) {
                create_edit_shopping_item_tag.run {
                    val wasEmpty = text.isEmpty()
                    setText(tagString)
                    if (wasEmpty) dismissDropDown()
                }
            }
        })

        val adapter = TagAdapter(this)
        viewModel.tags.observe(this, Observer {
            it ?: return@Observer
            adapter.items = it
        })

        create_edit_shopping_item_tag.run {
            threshold = 1
            setAdapter(adapter)
            setOnEditorActionListener { _, actionId, event ->
                return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    create_edit_shopping_item_text.requestFocus()
                    true
                } else {
                    false
                }
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && !isPopupShowing && !adapter.isEmpty) {
                    showDropDown()
                }
            }
            setOnClickListener {
                if (!isPopupShowing && !adapter.isEmpty)
                    showDropDown()
            }
            setOnItemClickListener { parent, _, position, _ ->
                val item = parent.getItemAtPosition(position) as TagItem
                setText(item.name)
                viewModel.selectedTag.value = item.name
                create_edit_shopping_item_text.requestFocus()
            }
        }
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

    class TagAdapter(context: Context) : ArrayAdapter<TagItem>(context, 0) {

        var items: List<TagItem> = listOf()
            set(value) {
                field = value
                notifyDataSetChanged()
                filteredItems = value
            }

        private var filteredItems: List<TagItem> = listOf()

        override fun getItem(position: Int) = filteredItems[position]

        override fun getCount() = filteredItems.size

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView
                    ?: LayoutInflater.from(parent?.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            view.findViewById<TextView>(android.R.id.text1).text = getItem(position).name
            return view
        }

        override fun hasStableIds() = true

        override fun getItemId(position: Int) = filteredItems[position].id.hashCode().toLong()

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    results?.run {
                        if (count > 0) {
                            val items = values as List<TagItem>
                            filteredItems = items
                            notifyDataSetChanged()
                        } else {
                            notifyDataSetInvalidated()
                        }
                    }
                }

                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val result = FilterResults()
                    val filteredItems = if (constraint == null) {
                        items
                    } else {
                        items.filter { it.name.contains(constraint) }
                    }
                    result.values = filteredItems
                    result.count = filteredItems.size
                    return result
                }
            }
        }
    }
}
