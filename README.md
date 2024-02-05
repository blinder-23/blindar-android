[![Build Status](https://app.bitrise.io/app/a3b53150cc64506b/status.svg?token=QmWjAPkNDhHShc53zwJzmw&branch=main)](https://app.bitrise.io/app/a3b53150cc64506b)

# 블린더 안드로이드 앱

<a href='https://play.google.com/store/apps/details?id=com.practice.hanbitlunch&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='블린더 다운로드하기 Google Play' width=250 src='https://play.google.com/intl/ko/badges/static/images/badges/ko_badge_web_generic.png'/></a>

## 기술 스택
* [Jetpack Compose](https://developer.android.com/jetpack/compose): 디자인 시스템 및 UI 개발
* [Firebase](https://firebase.google.com/?hl=ko): 사용자 인증
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture?hl=ko): Room, ViewModel, WorkManager 등을 활용하여 핵심 비즈니스 로직 구현
* [Retrofit](https://github.com/square/retrofit), [Gson](https://github.com/google/gson): 서버 API 통신
* [KoreanLunarCalendar](https://github.com/usingsky/KoreanLunarCalendar): 음력 공휴일 계산

## 아키텍처
[Android Architecture Guideline](https://developer.android.com/topic/architecture)을 참고하여, feature - data - core 모듈로 구성하였습니다.

* Feature 모듈: UI 코드 및 UI 로직을 포함합니다. Composable과 ViewModel, UI state 객체 등이 구현되어 있습니다.
* Data 모듈: 데이터베이스 로직을 포함합니다. Room 라이브러리를 활용하여 도메인 객체별로 DataSource 및 Repository를 구현하였습니다.
* Core 모듈: 그 밖에 앱에서 공통적으로 사용되는 코드를 작성하였습니다. 디자인 시스템, Firebase, WorkManager 등이 구현되어 있습니다.

![모듈 구조 이미지](previews/dependency-graph-blindar.png)

## 스크린샷
![앱 스크린샷](https://github.com/blinder-23/blindar-android/assets/45386920/fbcc72dd-5193-4233-a72f-307d68e2f238)


## Navigation Flow
<img width="1879" alt="Android Navigation Flow" src="https://github.com/blinder-23/blindar-android/assets/45386920/3f80bb8a-5b24-45c8-944f-1edd2d0993fd">
