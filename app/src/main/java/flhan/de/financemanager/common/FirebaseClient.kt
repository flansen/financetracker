package flhan.de.financemanager.common

import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

/**
 * Created by Florian on 14.09.2017.
 */
interface FirebaseClient {
    fun init()
}

class FirebaseClientImpl @Inject constructor() : FirebaseClient {
    private val firebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    override fun init() {
        firebaseDatabase.setPersistenceEnabled(true)
    }
}

/*
*                 val mDatabase = FirebaseDatabase.getInstance().getReference()
                val user1 = User("name1","mail1")
                val user2 = User("name2","mail2")
                val users = ArrayList<User>()
                users.apply { add(user1); add(user2) }
                val household = Household("hhname","", users)
                val ref = mDatabase.child("households").push()
                household.id = ref.key
                ref.setValue(household)

*
* */