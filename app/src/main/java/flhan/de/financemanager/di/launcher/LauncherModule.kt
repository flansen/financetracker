package flhan.de.financemanager.di.launcher

import dagger.Module
import flhan.de.financemanager.launcher.LauncherContract

/**
 * Created by fhansen on 06.10.17.
 */
@Module
class LauncherModule(
        val view: LauncherContract.View
)