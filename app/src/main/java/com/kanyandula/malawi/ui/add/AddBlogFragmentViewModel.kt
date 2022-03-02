package com.kanyandula.malawi.ui.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.kanyandula.malawi.data.Blog
import com.kanyandula.malawi.utils.Constants.BLOG_REF
import com.kanyandula.malawi.utils.Constants.EMPTY_VALUES
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class AddBlogFragmentViewModel @Inject constructor(
    databaseAuth: FirebaseAuth,
    private var blogRef: DatabaseReference
) : ViewModel() {

    private val user: String = databaseAuth.uid.toString()
    private val mainPath: DatabaseReference = blogRef.child(user).child(BLOG_REF)
    private var itemId: String = "0"

    val loading = MutableLiveData<Boolean>()
    val validationResult = MutableLiveData<Int>()
    val addingToDatabaseResult = MutableLiveData<Boolean>()


    init {
        setItemId()
    }

    fun  addToDatabase (item: Blog){

        loading.value = true

        val title = item.title
        val desc = item.desc
        val date = item.date
        val time = item.time
        val uid  = item.uid

        val blog = Blog(itemId, title, desc!!,date,time, uid)

        if(validation(blog)){

            mainPath.child(itemId).setValue(blog)
                .addOnCompleteListener() { task ->
                    if (task.isComplete) {
                        if (task.isSuccessful) {
                            loading.value = false
                            addingToDatabaseResult.value = true
                        } else {
                            loading.value = false
                            addingToDatabaseResult.value = false
                        }
                    }
                }
        }

    }

    private fun setItemId() {
        mainPath
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val childrenCount = snapshot.childrenCount
                        itemId = childrenCount.toString()

                        for (i in 0 until childrenCount) {
                            val child = snapshot.child(i.toString()).value
                            if (child.toString() == "null") itemId = i.toString()
                        }
                    }
                }
            })
    }


    private fun validation(blog: Blog): Boolean {
        return if (blog.title.isNullOrEmpty()) {
            loading.value = false
            validationResult.value = EMPTY_VALUES
            false
        } else true
    }

}