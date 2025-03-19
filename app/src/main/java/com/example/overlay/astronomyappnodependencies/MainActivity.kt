package com.example.overlay.astronomyappnodependencies

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.overlay.astronomyappnodependencies.astronomy.AstronomyFragment

/**
 * Implement a photo list of incoming pictures
 */
class MainActivity : SingleFragmentActivity() {

    override val layoutResId: Int
        get() = R.layout.activity_fragment

    override fun createFragment(): Fragment {
        return AstronomyFragment.newInstance()
    }

    fun replaceFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        fm.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null) // optional, if I want to keep the previous fragment
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            fragment = createFragment()
            fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
}