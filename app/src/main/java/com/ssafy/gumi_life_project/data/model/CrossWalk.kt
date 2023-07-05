package com.ssafy.gumi_life_project.data.model

import androidx.annotation.ColorRes
import com.ssafy.gumi_life_project.R

data class CrossWalk(val time: String)

//currentColor 현재 신호등 색, remainingTime 다음 신호등 색까지 남은 시간
data class LightTime(val currentTrafficLightColor: TrafficLightColor, val remainingTime: Long) {
    val remainingTimeString: String
        get() {
            val minutes = remainingTime / 60
            val seconds = remainingTime % 60
            return String.format("%02d:%02d", minutes, seconds)
        }

    @ColorRes
    fun getCurrentColorRes(): Int {
        return currentTrafficLightColor.colorRes
    }
}

data class TriggerTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val second: Int
)

enum class TrafficLightColor(@ColorRes val colorRes: Int) {
    GREEN(R.color.green),
    RED(R.color.red)
}

enum class SignalLight(
    val titleResId: Int,
    val contentResId: Int,
    private val greenDuration: Int,
    private val redDuration: Int
) {
    SIGNAL_LIGHT_1(R.string.cross_walk_title1, R.string.cross_walk_explain1, 25, 155),
    SIGNAL_LIGHT_2(R.string.cross_walk_title2, R.string.cross_walk_explain2, 35, 145),
    SIGNAL_LIGHT_3(R.string.cross_walk_title3, R.string.cross_walk_explain3, 30, 150);

    fun calculateRemainingTime(triggerTimeMillis: Long): LightTime {
        val currentTimeMillis = System.currentTimeMillis()
        val elapsedTimeMillis = currentTimeMillis - triggerTimeMillis
        val remainingTimeMillis = elapsedTimeMillis % ((greenDuration + redDuration) * 1000)

        return if (remainingTimeMillis > greenDuration * 1000) {
            LightTime(
                TrafficLightColor.RED,
                (redDuration * 1000 - (remainingTimeMillis - greenDuration * 1000)) / 1000
            )
        } else {
            LightTime(TrafficLightColor.GREEN, (greenDuration * 1000 - remainingTimeMillis) / 1000)
        }
    }

}