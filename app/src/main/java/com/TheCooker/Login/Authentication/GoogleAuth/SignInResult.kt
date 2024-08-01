package com.TheCooker.Login.Authentication.GoogleAuth

import java.io.Serializable

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?,
    val isSignInSuccessfulwithGoogle: Boolean
)

data class UserData(
    val userId: String,
    val userName: String?,
    var profilerPictureUrl: String?
): Serializable