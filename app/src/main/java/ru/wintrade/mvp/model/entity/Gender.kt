package ru.wintrade.mvp.model.entity

sealed class Gender {
    abstract val char: String
    abstract val text: String

    companion object {
        fun getGender(text: String?): Gender =
            when (text) {
                Man.text -> Man
                Woman.text -> Woman
                else -> Man
            }
    }

    object Man : Gender() {
        override val char: String
            get() = "M"
        override val text: String
            get() = "Мужчина"
    }

    object Woman : Gender() {
        override val char: String
            get() = "F"
        override val text: String
            get() = "Женщина"
    }
}
