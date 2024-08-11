package com.stenisway.wan_android.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stenisway.wan_android.R
import com.stenisway.wan_android.activity.MainActivity
import com.stenisway.wan_android.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var activity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity = requireActivity() as MainActivity
        binding = FragmentSettingBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        舊的方法會閃退，暫時不會更新
//        binding.custViewSendadvice.setOnClickListener { view1 -> sentEmail(requireContext()) }
        binding.txtThankTeacherHuang.setOnClickListener { v ->
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse("https://sites.google.com/site/ronforwork/Home/android-2")
            startActivity(intent)
        }
    }

    fun sentEmail(context: Context) {
        val emailUrl = "qazse753753@gmail.com"
        val emailSubject = "Wan Android Advices"
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.type = "text/html"
        intent.putExtra(Intent.EXTRA_EMAIL, emailUrl)
        intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
        context.startActivity(intent)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        activity.changeTitle(R.string.settings)
    }

    companion object {
        val instance: SettingFragment
            get() = SettingFragment()
    }
}