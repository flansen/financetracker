package flhan.de.financemanager.ui.main.expenses.createedit

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.AdapterView
import android.widget.ArrayAdapter
import butterknife.*
import butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.common.CreateEditMode
import flhan.de.financemanager.common.CreateEditMode.Create
import flhan.de.financemanager.common.CreateEditMode.Edit
import flhan.de.financemanager.common.extensions.goUp
import flhan.de.financemanager.common.extensions.visible
import kotlinx.android.synthetic.main.activity_expense_create_edit.*
import kotlinx.android.synthetic.main.money_input.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class CreateEditExpenseActivity : BaseActivity() {

    @Inject
    lateinit var factory: CreateEditExpenseViewModelFactory

    @BindString(R.string.create_expense_title)
    lateinit var createTitle: String

    @BindString(R.string.edit_expense_title)
    lateinit var editTitle: String

    private lateinit var viewModel: CreateEditExpenseViewModel
    private var showDeleteAction = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_create_edit)
        ButterKnife.bind(this)

        setupView()
        setupBinding()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                goUp()
                true
            }
            R.id.action_delete -> {
                viewModel.delete { finish() }
                true
            }
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return if (showDeleteAction) {
            menuInflater.inflate(R.menu.create_edit_menu, menu)
            true
        } else {
            super.onCreateOptionsMenu(menu)
        }
    }

    override fun onDestroy() {
        createEditExpenseUserSpinner.adapter = null
        super.onDestroy()
    }

    @OnTextChanged(R.id.causeText, callback = AFTER_TEXT_CHANGED)
    fun onCauseChanged(cause: Editable) {
        viewModel.cause.value = cause.toString()
    }

    @OnTextChanged(R.id.amountEditText, callback = AFTER_TEXT_CHANGED)
    fun onAmountChanged(amount: Editable) {
        viewModel.onAmountChanged(amount.toString())
    }

    @OnEditorAction(R.id.amountEditText)
    fun onAmountEditorAction(actionId: Int): Boolean {
        return if (actionId == IME_ACTION_NEXT) {
            causeText.requestFocus()
            true
        } else {
            false
        }
    }

    @OnFocusChange(R.id.amountEditText)
    fun onEditTextFocusChanged(hasFocus: Boolean) {
        amountDisplayText.isSelected = hasFocus
    }

    @OnClick(R.id.amountDisplayText)
    fun onAmountClicked() {
        amountEditText.requestFocus()
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(amountEditText, SHOW_IMPLICIT)
    }

    @OnClick(R.id.create_edit_expense_save)
    fun onSaveClicked() {
        viewModel.onSaveClicked({ finish() })
    }

    @OnTouch(R.id.createEditExpenseUserSpinner)
    fun onSpinnerTouched(): Boolean {
        createEditExpenseUserSpinner.requestFocusFromTouch()
        return false
    }

    private fun setupBinding() {
        createEditExpenseUserSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.onUserSelected(position)
            }
        }
        viewModel = ViewModelProviders.of(this, factory).get(CreateEditExpenseViewModel::class.java)
        viewModel.mode.observe(this, Observer { mode -> setTitleForMode(mode) })
        viewModel.isLoading.observe(this, Observer { isLoading ->
            createEditExpenseLoadingView.visible(isLoading ?: true)
        })
        viewModel.userItems.observe(this, Observer {
            val names = it?.map { it.name } ?: mutableListOf()
            setUserItems(names)
        })
        viewModel.selectedUserIndex.observe(this, Observer {
            createEditExpenseUserSpinner.setSelection(it ?: 0)
        })
        viewModel.amount.observe(this, Observer { amount ->
            amountDisplayText.text = amount?.displayString ?: ""
            if (amount?.baseString?.equals(amountEditText.text.toString()) == false) {
                amountEditText.setText(amount.baseString)
                amountEditText.setSelection(amount.baseString.length)
            }
        })
        viewModel.cause.observe(this, Observer { cause ->
            if (cause != causeText.text.toString()) {
                causeText.setText(cause ?: "")
            }
        })
    }

    private fun setUserItems(names: List<String>) {
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        createEditExpenseUserSpinner.adapter = adapter
    }

    private fun setupView() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setTitleForMode(mode: CreateEditMode?) {
        when (mode) {
            Create -> supportActionBar!!.title = createTitle
            Edit -> {
                supportActionBar!!.title = editTitle
                showDeleteAction = true
                invalidateOptionsMenu()
            }
        }
    }

    companion object {
        private const val ID_KEY = "id"
        private const val USER_LAYOUT_RESOURCE = android.R.layout.simple_list_item_activated_1

        fun createIntent(context: Context, id: String?): Intent {
            val intent = Intent(context, CreateEditExpenseActivity::class.java)
            intent.putExtra(ID_KEY, id)
            return intent
        }
    }

    fun retrieveExpenseId(): String? {
        return intent.getStringExtra(ID_KEY)
    }
}