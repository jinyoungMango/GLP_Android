package com.ssafy.gumi_life_project.data.repository.user

import com.ssafy.gumi_life_project.data.model.ErrorResponse
import com.ssafy.gumi_life_project.data.model.Member
import com.ssafy.gumi_life_project.data.model.User
import com.ssafy.gumi_life_project.data.model.UserResponse
import com.ssafy.gumi_life_project.util.network.NetworkResponse
import retrofit2.http.Header

interface UserRepository {
    // 유저의 jwt 토큰을 파라미터로 받아서 member에 대한 객체 정보를 반환
    suspend fun getMemberInfo(accessToken: String): NetworkResponse<Member, ErrorResponse>

    suspend fun getJwtToken(accessToken: String): NetworkResponse<UserResponse, ErrorResponse>

    suspend fun makeNickName(@Header("Authorization") accessToken: String) : NetworkResponse<Member, ErrorResponse>
}