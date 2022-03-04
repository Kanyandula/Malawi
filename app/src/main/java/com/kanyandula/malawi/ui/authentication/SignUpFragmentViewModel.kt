package com.kanyandula.malawi.ui.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.firebase.auth.FirebaseAuth
import com.kanyandula.malawi.utils.Constants.EMPTY_VALUES
import com.kanyandula.malawi.utils.Constants.INVALID_EMAIL
import com.kanyandula.malawi.utils.Constants.PASSWORD_TOO_SHORT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SignUpFragmentViewModel @Inject constructor(
    private var databaseAuth: FirebaseAuth
):ViewModel() {



    val validationResult = MutableLiveData<Int>()
    val loading = MutableLiveData<Boolean>()
    val signingUpStatus = MutableLiveData<Boolean>()



    fun signUp(email: String, password: String) {
        loading.value = true

        if (validation(email, password)) {
            databaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        loading.value = false
                        signingUpStatus.value = true
                    } else {
                        loading.value = false
                        signingUpStatus.value = false
                    }
                }
        }

    }

    private fun validation(email: String, password: String): Boolean {

        if (email.isEmpty() || password.isEmpty()) {
            loading.value = false
            validationResult.value = EMPTY_VALUES
            return false
        } else if (password.length < 6) {
            loading.value = false
            validationResult.value = PASSWORD_TOO_SHORT
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loading.value = false
            validationResult.value = INVALID_EMAIL
            return false
        } else {
            return true
        }
    }
}