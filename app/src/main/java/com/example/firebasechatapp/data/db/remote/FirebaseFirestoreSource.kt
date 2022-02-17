package com.example.firebasechatapp.data.db.remote

import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.utils.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
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
    fun getChats(id: String): Task<QuerySnapshot> {
        return cloud.collection(Constants.user).document(id)
            .collection(Constants.chats).get()
    }

    /**
     * This function is used to get the user info of a user given tbe id
     */
    fun getUserInfo(id: String): Task<DocumentSnapshot> {
        return cloud.collection(Constants.user).document(id)
            .get()
    }

    /**
     * This function is used to get the last message of the current user with one of his contacts
     */
    fun getLastMessageFromChat(userId: String, chatId: String): Task<DocumentSnapshot> {
        return cloud.collection(Constants.user).document(userId)
            .collection(chatId).document(Constants.lastMessage).get()
    }

    /**
     * This function is used to change the status of the last sent message of the user
     */
    fun changeLastMessageFromChat(id: String, chatId: String): Task<Void> {
        return cloud.collection(Constants.user).document(id)
            .collection(chatId).document(Constants.lastMessage).update("seen", true)
    }

    /**
     * This function is used to change the online status of the user to true or false.
     */
    fun toggleOnline(id: String, online: Boolean): Task<Void> {
        return cloud.collection(Constants.user).document(id)
            .update("online", online)
    }
}