package flhan.de.financemanager.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import java.lang.ref.WeakReference


class FragmentAdapter internal constructor(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val instantiatedFragments = SparseArray<WeakReference<Fragment>>()
    private val fragmentList = mutableListOf<Fragment>()
    private val fragmentTitleList = mutableListOf<String?>()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    internal fun addFragment(fragment: Fragment, title: String? = null) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        instantiatedFragments.put(position, WeakReference(fragment))
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        instantiatedFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }
}
