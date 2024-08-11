package com.stenisway.wan_android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stenisway.wan_android.activity.MainActivity

open class BaseFragment : Fragment() {

    private lateinit var activity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity = requireActivity() as MainActivity
        super.onViewCreated(view, savedInstanceState)
    }

    open fun changeTitle(title: Int) {
        activity.changeTitle(title)
    }


}