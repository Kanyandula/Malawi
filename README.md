# Travel Blog
This is an Android app written in kotlin language, it implements offline first functionality which means it  cache the results offline to provide a great user experience when internet access is limited.The app fatches blog posts from firebase realtime database and the data is catched in the app using Room persistant libraly. The main idea of the app was to give tourist first had information about best places to visit  which are owned by small business in towns and cities.The app is still in production.

<img src="https://firebasestorage.googleapis.com/v0/b/travelmantics-6a12f.appspot.com/o/appImages%2FHome.jpg?alt=media&token=5091dbf7-b52a-4d8a-87b1-6bf4ed57b03c" width="300"/> | <img src="https://firebasestorage.googleapis.com/v0/b/travelmantics-6a12f.appspot.com/o/appImages%2FNetwork%20Not%20available.jpg?alt=media&token=144c5fae-f777-4ec1-8be6-008a8b02d940" width="300"/>|<img src="https://firebasestorage.googleapis.com/v0/b/travelmantics-6a12f.appspot.com/o/appImages%2FSearch.jpg?alt=media&token=13fb5ec7-86b4-43e7-b4b6-94867e6d7b05" width="300"/> | <img src="https://firebasestorage.googleapis.com/v0/b/travelmantics-6a12f.appspot.com/o/appImages%2FBookMark.jpg?alt=media&token=2c75ebbb-5b38-4e8e-abe5-673468942238" width="300"/>


#### Features:
- Model-View-ViewModel (MVVM) architecture with Jetpack ViewModels and the repository pattern
|  <img src="https://firebasestorage.googleapis.com/v0/b/travelmantics-6a12f.appspot.com/o/blog_images%2Fstructure.png?alt=media&token=7ee7d695-dfa9-475a-aaec-9f85e1248746" width="300"/> 

- Database caching [Room](https://developer.android.com/topic/libraries/architecture/room)
- Monitoring Network Connectivity
- [Process Death](https://medium.com/inloopx/android-process-kill-and-the-big-implications-for-your-app-1ecbed4921cb)
- Dependency Injection [HILT](https://dagger.dev/hilt/components.html)
- Kotlin [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/)
- Pagination with [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) and [RemoteMediator
](https://developer.android.com/rference/kotlin/androidx/paging/RemoteMediator)





### Data
 The data for this app is fetched from  [Firebase Realtime Database](https://www.raywenderlich.com/books/saving-data-on-android/v1.0/chapters/12-introduction-to-firebase-realtime-database) which gets post from a NodeJs web app connected to the same Firebase Realtime Database. The web app has users who post their blogs on it. 

### Connect with Me

- <a href = "https://www.linkedin.com/in/ephraim-kanyandula/">LinkedIn | Ephraim Kanyandula<a/>

