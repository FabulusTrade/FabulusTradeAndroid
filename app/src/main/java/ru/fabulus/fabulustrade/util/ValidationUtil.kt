package ru.fabulus.fabulustrade.util

import android.util.Patterns
import ru.fabulus.fabulustrade.mvp.model.entity.CommentBlockedUser
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

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

enum class DateValidation {
    CORRECT, INVALID
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
    if (!(containsUppercaseLetter(password) && containsLowercaseLetter(password) && containsDigit(
            password
        ))
    )
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

fun isValidBirthday(date: String): DateValidation {
    try {
        val dateNumbers = date.split(".").map { it.toInt() }
        if (dateNumbers.last() in 1950..2005) {
            when {
                dateNumbers[1] in listOf(1, 3, 5, 7, 8, 10, 12) &&
                        dateNumbers.first() in 1..31 -> return DateValidation.CORRECT
                dateNumbers[1] in listOf(4, 6, 9, 11) &&
                        dateNumbers.first() in 1..30 -> return DateValidation.CORRECT
                dateNumbers[1] == 2 &&
                        dateNumbers.first() in 1..29 -> return DateValidation.CORRECT
            }
        }
        return DateValidation.INVALID
    } catch (e: Exception) {
        return DateValidation.INVALID
    }
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

fun isCanEditComment(dateCreated: Date): Boolean {
    val diffBetweenTimeZones = getDiffBetweenTimeZones()
    return if (diffBetweenTimeZones >= 0) {
        ((currentTimeInMillis() - dateCreated.time - diffBetweenTimeZones) < EDIT_COMMENT_PERIOD)
    } else {
        ((currentTimeInMillis() - dateCreated.time + diffBetweenTimeZones) < EDIT_COMMENT_PERIOD)
    }
}

fun isCanDeleteComment(dateCreated: Date): Boolean {
    val diffBetweenTimeZones = getDiffBetweenTimeZones()
    return if (diffBetweenTimeZones >= 0) {
        ((currentTimeInMillis() - dateCreated.time - diffBetweenTimeZones) < DELETE_COMMENT_PERIOD)
    } else {
        ((currentTimeInMillis() - dateCreated.time + diffBetweenTimeZones) < DELETE_COMMENT_PERIOD)
    }
}

fun isCanEditPost(dateCreated: Date): Boolean {
    val diffBetweenTimeZones = getDiffBetweenTimeZones()
    return if (diffBetweenTimeZones >= 0) {
        ((currentTimeInMillis() - dateCreated.time - diffBetweenTimeZones) < EDIT_POST_PERIOD)
    } else {
        ((currentTimeInMillis() - dateCreated.time + diffBetweenTimeZones) < EDIT_POST_PERIOD)
    }
}

fun isCanDeletePost(dateCreated: Date): Boolean {
    val diffBetweenTimeZones = getDiffBetweenTimeZones()
    return if (diffBetweenTimeZones >= 0) {
        ((currentTimeInMillis() - dateCreated.time - diffBetweenTimeZones) < DELETE_POST_PERIOD)
    } else {
        ((currentTimeInMillis() - dateCreated.time + diffBetweenTimeZones) < DELETE_POST_PERIOD)
    }
}

fun isCanFlashPostByCreateDate(dateCreated: Date): Boolean {
    return ((currentTimeInMillis() - dateCreated.time) < FLASH_POST_PERIOD)
}

fun currentTimeInMillis(): Long = Calendar.getInstance().timeInMillis

fun getDiffBetweenTimeZones(): Int =
    TimeZone.getTimeZone(SERVER_TIME_ZONE_ID).rawOffset - TimeZone.getDefault().rawOffset

fun List<CommentBlockedUser>.isLocked(userId: String): Boolean {
    var result = false
    if (this.count() > 0) {
        for (item in this) {
            if (item.blockedUserID == userId) {
                result = true
                break
            }
        }
    }
    return result
}