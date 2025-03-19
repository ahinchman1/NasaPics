package com.example.overlay.astronomyappnodependencies

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class SingleFragmentActivity : AppCompatActivity() {

    abstract val layoutResId: Int

    abstract fun createFragment(): Fragment

    /**
     * Bundle v. PersistableBundle
     * - Bundle passes key-value data between components, holds primitive data.
     *     - For Activities, Fragments, and other temporary components
     * - PersistableBundle passes key-value data that persists between device reboots.
     *      - For tasks like JobScheduler, JobIntentService, or Broadcast receiver
     */
    override fun onCreate(savedInstanceState: Bundle?, persistableBundle: PersistableBundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutResId)
    }
}