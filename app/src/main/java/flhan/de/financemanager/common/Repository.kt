package flhan.de.financemanager.common

import javax.inject.Inject

/**
 * Created by fhansen on 13.09.17.
 */
interface Repository {

}

//https://firebase.google.com/docs/database/android/offline-capabilities
class RepositoryImpl @Inject constructor(
        private val firebaseClient: FirebaseClient
) : Repository {

}