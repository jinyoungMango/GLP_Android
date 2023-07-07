package com.ssafy.gumi_life_project.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ssafy.gumi_life_project.R
import com.ssafy.gumi_life_project.data.model.Tip
import com.ssafy.gumi_life_project.databinding.FragmentHomeBinding
import com.ssafy.gumi_life_project.ui.home.crosswalk.CrossWalkBottomSheet
import com.ssafy.gumi_life_project.ui.main.MainViewModel
import com.ssafy.gumi_life_project.util.template.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    R.layout.fragment_home
) {
    private val viewModel by viewModels<HomeViewModel>()
    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@HomeFragment.viewModel
            mainViewModel = this@HomeFragment.activityViewModel
        }
    }

    override fun init() {
        observeData()
        viewModel.loadAndSetTriggerTimes()
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

                    val bottomSheetDialogFragment =
                        CrossWalkBottomSheet(signalLight, title, content)
                    bottomSheetDialogFragment.show(childFragmentManager, "CrossWorkBottomSheet")
                }
            }
        }

        with(activityViewModel) {
            tip.observe(viewLifecycleOwner) { tip ->
                    val randomTip = getRandomTip(tip)
                    bindingNonNull.textviewTipContent.text = limitStringLength(randomTip.subject)

                    bindingNonNull.linearlayoutTip.setOnClickListener {
                        val bottomSheetDialogFragment =
                            TipBottomSheet(randomTip.subject, randomTip.description)
                        bottomSheetDialogFragment.show(childFragmentManager, "TipBottomSheet")
                    }
            }

            weather.observe(viewLifecycleOwner) { weather ->
                    bindingNonNull.textviewTodayWeatherTemperature.text = weather.data.temperature + "º"
                    makeWeatherIcon(weather.data.precipitationType)
            }
        }
    }

    private fun getRandomTip(tips: List<Tip>): Tip {
        return if (tips.isNotEmpty()) tips[Random.nextInt(tips.size)] else Tip()
    }

    private fun limitStringLength(input: String, maxLength: Int = 23): String {
        return if (input.length > maxLength) input.substring(0, maxLength) + "..." else input
    }

    private fun makeWeatherIcon(type: String) {
        when (type) {
            "없음" -> bindingNonNull.imageviewTodayWeatherImg.setImageResource(R.drawable.icon_sunny)
            "비" -> bindingNonNull.imageviewTodayWeatherImg.setImageResource(R.drawable.icon_rainy)
            "눈" -> bindingNonNull.imageviewTodayWeatherImg.setImageResource(R.drawable.icon_snow)
        }
    }
}