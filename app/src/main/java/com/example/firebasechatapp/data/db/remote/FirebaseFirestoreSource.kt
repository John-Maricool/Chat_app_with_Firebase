package com.example.firebasechatapp.data.db.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.firebasechatapp.data.models.ChatInfo
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.UId
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.utils.Constants
import com.example.firebasechatapp.utils.Constants.currentPage
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFirestoreSource @Inject constructor(
    var cloud: FirebaseFirestore
) {
    /**
     * This function is used to upload a registered user details to the database
     */
    fun uploadUserDetailsToDb(userInfo: UserInfo): Task<Void> {
        return cloud.collection(Constants.user).document(userInfo.id).set(userInfo)
    }

    /**
     * This function is used to get the chats that the current user is following
     */
    suspend fun getChats(id: String): QuerySnapshot? {
        return cloud.collection(Constants.user).document(id)
            .collection(Constants.chat).get().await()
    }

    /**
     * This function is used to get the user info of a user given tbe id
     */
    suspend fun getUserInfo(id: String): DocumentSnapshot {
        return cloud.collection(Constants.user).document(id)
            .get(Source.SERVER).await()
    }

    fun getChangedUserInfo(id: String): DocumentReference {
        return cloud.collection(Constants.user).document(id)
    }

    /**
     * This function is used to get all the registeredUsers
     */
    fun getAllUsers(userId: String): Query {
        return cloud.collection(Constants.user).whereNotEqualTo("id", userId)
    }

    /**
     * This function is used to get the last message of the current user with one of his contacts
     */

    suspend fun getLastMessageFromChat(chatId: String): DocumentSnapshot? {
        return cloud.collection(Constants.chatChannels)
            .document(chatId).collection(Constants.messages)
            .document(Constants.lastMessage).get().await()
    }

    fun getAllMessages(channelId: String): Query {
        return cloud.collection(Constants.chatChannels)
            .document(channelId).collection(Constants.messages).limit(10)
            .orderBy("sentTime", Query.Direction.DESCENDING)
    }

    fun getReloadedMessages(
        channelId: String,
    ): Query {
        return cloud.collection(Constants.chatChannels)
            .document(channelId).collection(Constants.messages)
            .limit((10 * currentPage).toLong())
            .orderBy("sentTime", Query.Direction.DESCENDING)
    }

    suspend fun getChatChannelId(userId: String, chatId: String): DocumentSnapshot? {
        return cloud.collection(Constants.user).document(userId)
            .collection(Constants.chatChannels)
            .document(chatId).get().await()
    }

    /**
     * This function is used to change the status of the last sent message of the user
     */

    /**
     * This function is used to change the online status of the user to true or false.
     */

    suspend fun toggleOnline(id: String, online: Boolean): Void? {
        return cloud.collection(Constants.user).document(id)
            .update("online", online).await()
    }

    /**
     * This function is used to save the user data to the database on sign up.
     */
  /*  fun saveUserDetailsToDb(userInfo: UserInfo): Task<Void> {
        return cloud.collection(Constants.user).document(userInfo.id).set(userInfo)
    }*/

    /**
     * This function created chat channel with the first message in it
     */
    suspend fun createChatInfo(user: String, secUser: String): String {
        val doc = cloud.collection(Constants.user).document(user).collection(Constants.chatChannels)
            .document(secUser).get().await()
        return if (doc.exists()) {
            doc["id"].toString()
        } else {
            val channelIdRef = cloud.collection(Constants.chatChannels).document()
            val channelId = channelIdRef.get().await().reference.id
            val chatInfoForUser = ChatInfo(channelId, user, secUser)
            val chatInfoForSecUser = ChatInfo(channelId, secUser, user)
            channelIdRef.set(chatInfoForUser)
            cloud.collection(Constants.user).document(user).collection(Constants.chatChannels)
                .document(chatInfoForUser.secUserId).set(chatInfoForUser)
            cloud.collection(Constants.user).document(secUser).collection(Constants.chatChannels)
                .document(chatInfoForSecUser.secUserId).set(chatInfoForSecUser)
            channelId
        }
    }

    /**
    This function is used to send message
     */

    suspend fun sendMessage(message: Message, chatId: String) {
        cloud.collection(Constants.chatChannels).document(chatId)
            .collection(Constants.messages)
            .add(message).await()

        cloud.collection(Constants.chatChannels).document(chatId)
            .collection(Constants.messages)
            .document(Constants.lastMessage).set(message).await()
    }

    /**
    This function is used to add users to chats
     */

    suspend fun addUserToChats(user: String, secUser: String) {
        val userDet = UId(secUser)
        val mainUserDet = UId(user)
        cloud.collection(Constants.user).document(user).collection(Constants.chat)
            .document(secUser).set(userDet).await()
        cloud.collection(Constants.user).document(secUser).collection(Constants.chat)
            .document(user).set(mainUserDet).await()
    }

    /**
     * This function checks if the user is added to the list of chats
     */
    suspend fun isUserAddedToChats(user: String, secUser: String): DocumentSnapshot? {
        return cloud.collection(Constants.user).document(user).collection(Constants.chat)
            .document(secUser).get().await()
    }

    /**
     * This function updates the user name
     */
    fun changeName(userId: String, name: String): Task<Void> {
        return cloud.collection(Constants.user).document(userId).update("displayName", name)
    }

    fun checkIfUserHasNewMessages(userId: String): MutableLiveData<Boolean> {
        val state = MutableLiveData<Boolean>()
        cloud.collection(Constants.user).document(userId).collection(Constants.chatChannels)
            .addSnapshotListener { value, error ->
                val chats = value?.toObjects(ChatInfo::class.java)
                chats?.forEach {
                    cloud.collection(Constants.chatChannels).document(it.id)
                        .collection(Constants.messages)
                        .document(Constants.lastMessage)
                        .addSnapshotListener { value, error ->
                            val seen = value?.get("seen")
                            val receiver = value?.get("receiverId")
                            if (seen == false && receiver == userId) {
                                state.value = !(seen as Boolean?)!!
                                Log.d("testTag", state.value.toString())
                                Log.d("testTag", seen.toString())
                                //  values.add(true)
                            } else {
                                if (state.value == true) {
                                    state.value = true
                                } else {
                                    Log.d("testTag", seen.toString())
                                    Log.d("testTag", state.value.toString())
                                    state.value = false
                                }
                            }
                        }
                }
            }
        return state
    }
}