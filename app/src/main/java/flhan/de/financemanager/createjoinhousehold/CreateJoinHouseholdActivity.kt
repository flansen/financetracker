package flhan.de.financemanager.createjoinhousehold

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import flhan.de.financemanager.R
import kotlinx.android.synthetic.main.activity_create_join_household.*

class CreateJoinHouseholdActivity : AppCompatActivity() {
    private val nameText: EditText by lazy { create_join_household_create_name_text }
    private val emailText: EditText by lazy { create_join_household_join_mail_text }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_join_household)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_join_household_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.create_join_household_action_done) {
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
