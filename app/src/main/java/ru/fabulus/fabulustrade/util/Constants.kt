package ru.fabulus.fabulustrade.util

const val PREFERENCE_NAME = "app"
const val HAS_VISITED_TUTORIAL = "hasVisitedTutorial"
const val IS_AUTHORIZED = "isAuthorized"
const val REGISTRATION_DATA = "registrationData"
const val TRADER_REG_INFO_TAG = "trader registration info"
// период в течении которого можно редактировать/удалять комментарий в милилисекуднах
// 1час * 60 минут * 60 секунд * 1000 миллисекунд
const val EDIT_COMMENT_PERIOD = 1 * 60 * 60 * 1000
const val DELETE_COMMENT_PERIOD = 1 * 60 * 60 * 1000
// Макисмальная длина текста в при шаринге поста
const val MAX_SHARED_LEN_POST_TEXT = 400
