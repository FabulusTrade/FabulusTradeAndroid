package ru.wintrade.util

import android.util.Patterns

private const val passwordMinLength = 8
private const val nicknameMinLength = 3
private const val nicknameMaxLength = 15

enum class PasswordValidation {
    TOO_SHORT, NO_UPPERCASE_OR_LOWERCASE_OR_DIGIT, OK
}

enum class NicknameValidation {
    TOO_SHORT, TOO_LONG, ALREADY_EXISTS, ONLY_ENG_AND_DIGIT, OK
}

enum class EmailValidation {
    INCORRECT, ALREADY_EXISTS, OK
}

enum class PhoneValidation {
    INCORRECT, ALREADY_EXISTS, OK
}

fun isValidEmail(email: String): EmailValidation {
    val pattern = Patterns.EMAIL_ADDRESS
    return if (pattern.matcher(email).matches()) EmailValidation.OK else EmailValidation.INCORRECT
}

fun isValidPhone(phone: String): PhoneValidation {
    val pattern = Patterns.PHONE
    return if (pattern.matcher(phone).matches()) PhoneValidation.OK else PhoneValidation.INCORRECT
}

fun isValidPassword(password: String): PasswordValidation {
    if (password.length < passwordMinLength)
        return PasswordValidation.TOO_SHORT
    if (!(containsUppercaseLetter(password) && containsLowercaseLetter(password) && containsDigit(password)))
        return PasswordValidation.NO_UPPERCASE_OR_LOWERCASE_OR_DIGIT
    return PasswordValidation.OK
}

fun isValidNickname(nickname: String): NicknameValidation {
    if (nickname.length < nicknameMinLength)
        return NicknameValidation.TOO_SHORT
    if (nickname.length > nicknameMaxLength)
        return NicknameValidation.TOO_LONG
    if (!nickname.matches("^[a-zA-Z0-9]*$".toRegex()))
        return NicknameValidation.ONLY_ENG_AND_DIGIT
    return NicknameValidation.OK
}

private fun containsUppercaseLetter(line: String): Boolean {
    return line.matches(".*[A-ZА-ЯЁ].*".toRegex())
}

private fun containsLowercaseLetter(line: String): Boolean {
    return line.matches(".*[a-zа-яё].*".toRegex())
}

private fun containsDigit(line: String): Boolean {
    return line.matches(".*[0-9].*".toRegex())
}

