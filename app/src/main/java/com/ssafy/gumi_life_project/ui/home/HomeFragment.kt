package com.ssafy.gumi_life_project.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ssafy.gumi_life_project.R
import com.ssafy.gumi_life_project.databinding.FragmentHomeBinding
import com.ssafy.gumi_life_project.ui.home.crosswalk.CrossWalkBottomSheet
import com.ssafy.gumi_life_project.util.CrossWorkTimeList
import com.ssafy.gumi_life_project.util.setCrossWorkTimeList
import com.ssafy.gumi_life_project.util.template.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    R.layout.fragment_home
) {
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@HomeFragment.viewModel
        }
    }

    override fun init() {
        observeData()
        setCrossWorkTimeList()
    }

    private fun observeData() {
        with(viewModel) {
            errorMsg.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let {
                    showToast(it)
                }
            }

            showBottomSheetEvent.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { signalLight ->
                    val titleResId = signalLight.titleResId
                    val contentResId = signalLight.contentResId

                    val title = getString(titleResId)
                    val content = getString(contentResId)

                    val bottomSheetDialogFragment = CrossWalkBottomSheet(title, content)
                    bottomSheetDialogFragment.show(childFragmentManager, "CrossWorkBottomSheet")
                }
            }
        }
    }
}