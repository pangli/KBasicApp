package com.kcrason.appbasic.common

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class FastForResultFragment : Fragment() {

    private var mOnActivityResultListener: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)? = null

    fun setActivityResultListener(onActivityResultListener: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit) {
        this.mOnActivityResultListener = onActivityResultListener
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mOnActivityResultListener?.invoke(requestCode, resultCode, data)
    }


    companion object {

        private fun localFindFragmentByTag(fragment: Fragment): Fragment? {
            return fragment.childFragmentManager.findFragmentByTag("FastForResultFragment")
        }

        private fun localFindFragmentByTag(activity: FragmentActivity): Fragment? {
            return activity.supportFragmentManager.findFragmentByTag("FastForResultFragment")
        }

        fun startActivityForResult(
            fragment: Fragment, intent: Intent, requestCode: Int,
            onActivityResultListener: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)
        ) {
            var localFragment = localFindFragmentByTag(fragment)
            if (localFragment == null) {
                localFragment = FastForResultFragment()
                localFragment.setActivityResultListener(onActivityResultListener)
                fragment.childFragmentManager.beginTransaction().add(localFragment, "FastForResultFragment")
                    .commit()
                localFragment.startActivityForResult(intent, requestCode)
            } else {
                if (localFragment is FastForResultFragment) {
                    localFragment.setActivityResultListener(onActivityResultListener)
                }
            }
        }

        fun startActivityForResult(
            fragmentActivity: FragmentActivity, intent: Intent, requestCode: Int,
            onActivityResultListener: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)
        ) {
            var localFragment = localFindFragmentByTag(fragmentActivity)
            if (localFragment == null) {
                localFragment = FastForResultFragment()
                localFragment.setActivityResultListener(onActivityResultListener)
                fragmentActivity.supportFragmentManager
                    .beginTransaction()
                    .add(localFragment, "FastForResultFragment")
                    .commit()
                localFragment.startActivityForResult(intent, requestCode)
            } else {
                if (localFragment is FastForResultFragment) {
                    localFragment.setActivityResultListener(onActivityResultListener)
                }
            }

        }
    }
}