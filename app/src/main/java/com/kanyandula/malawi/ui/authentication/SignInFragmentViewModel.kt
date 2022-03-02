package com.kanyandula.malawi.ui.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.kanyandula.malawi.`object`.ValidationObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class SignInFragmentViewModel  @Inject constructor(
  private var databaseAuth: FirebaseAuth
):ViewModel() {

    val userId = MutableLiveData<String>()
    val validationResult = MutableLiveData<Int>()
    val loading = MutableLiveData<Boolean>()
    val signingInStatus = MutableLiveData<Boolean>()

    fun checkUserId() {
        userId.value = databaseAuth.uid.toString()
    }

    fun signIn(email: String, password: String) {
        loading.value = true

        if (validation(email, password)) {
            databaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isComplete) {
                        loading.value = false
                        signingInStatus.value = task.isSuccessful
                    } else {
                        loading.value = false
                        signingInStatus.value = false
                    }
                }
        }

    }

    private fun validation(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            loading.value = false
            validationResult.value = ValidationObject.EMPTY_VALUES
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loading.value = false
            validationResult.value = ValidationObject.INVALID_EMAIL
            return false
        } else {
            return true
        }
    }

}