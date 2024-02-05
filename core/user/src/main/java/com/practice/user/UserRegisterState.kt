package com.practice.user

enum class UserRegisterState {
    NOT_LOGGED_IN,
    USERNAME_MISSING,
    SCHOOL_NOT_SELECTED,
    ALL_FILLED, // 새로 로그인하는 사용자의 정보가 Firebase에 모두 세팅된 경우
    AUTO_LOGIN, // 로컬에 모든 정보가 저장된 경우 (자동 로그인)
}