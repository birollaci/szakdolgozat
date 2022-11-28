package hu.bme.aut.rentapp.validations

import android.widget.EditText

class Validation {
    // ha ures volt nem null kerult a jsonbe, ezert kellettek
    fun validateEmptyString(editText: EditText): String? {
        if(editText.text.toString() == "") {
            return null
        }
        return editText.text.toString()
    }
    fun validateEmptyInt(editText: EditText): Int? {
        if(editText.text.toString() == "") {
            return null
        }
        return editText.text.toString().toInt()
    }

    // ha a kotelezo mezok nincsenek kitoltve, akkor hibat jelez a megfelelo helyen
    fun validateEditTexts(values: List<EditText>): Boolean{
        for( value in values) {
            if (value.text.toString().isEmpty()) {
                value.requestFocus()
                value.error = "Please enter the value"
                return false
            }
        }
        return true
    }

    fun validationUsername(editText: EditText): Boolean{
        val len: Int = editText.text.toString().length
        return len in 3..24
    }

    fun validationPassword(editText: EditText): Boolean{
        val str = editText.text.toString()
        val len: Int = str.length
        return if (len < 3 || len > 256) {
            false
        } else if (!str.hasEnoughDigits()) {
            false
        } else if (!str.isMixedCase()) {
            false
        } else {
            str.hasSpecialChar()
        }
    }

    fun validationEmail(editText: EditText): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(editText.text.toString()).matches();
    }

    private fun String.hasEnoughDigits() = count(Char::isDigit) > 0
    private fun String.isMixedCase() = any(Char::isLowerCase) && any(Char::isUpperCase)
    private fun String.hasSpecialChar() = any { it in "!,+^-_@" }
}