package com.example.firebasechatapp.data.mapper

import com.example.firebasechatapp.cache_source.UserEntity
import com.example.firebasechatapp.data.interfaces.Mapper
import com.example.firebasechatapp.data.models.ChatWithUserInfo
import javax.inject.Inject

class CacheMapperImpl
@Inject constructor() : Mapper<UserEntity, ChatWithUserInfo> {

    override fun mapToCache(c: ChatWithUserInfo): UserEntity {
        return UserEntity(
            channelId = c.mChat.channelId,
            id = c.mUserInfo.id,
            displayName = c.mUserInfo.displayName,
            profileImageUrl = c.mUserInfo.profileImageUrl,
            online = c.mUserInfo.online,
            lastMessage = c.mChat.lastMessage.text,
            sentTime = c.mChat.lastMessage.sentTime,
            receiverId = c.mChat.lastMessage.receiverId,
            seen = c.mChat.lastMessage.seen,
            type = c.mChat.lastMessage.type
        )
    }

    override fun mapAllToCache(c: List<ChatWithUserInfo>): List<UserEntity> {
        val ent = mutableListOf<UserEntity>()
        c.forEach {
            ent.add(mapToCache(it))
        }
        return ent.toList()
    }
}