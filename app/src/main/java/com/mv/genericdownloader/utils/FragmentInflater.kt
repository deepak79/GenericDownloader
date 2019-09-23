package com.mv.genericdownloader.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class FragmentInflater<T : Fragment> private constructor() {

    companion object {
        val instance = FragmentInflater<Fragment>()
    }

    fun inflate(fragment: T, activity: AppCompatActivity, resId: Int, tag: String) {
        activity.supportFragmentManager
            .beginTransaction()
            .replace(resId, fragment, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }
}