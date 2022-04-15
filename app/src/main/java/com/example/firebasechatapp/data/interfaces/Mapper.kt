package com.example.firebasechatapp.data.interfaces

interface Mapper<Cache, Source> {

    fun mapToCache(c: Source): Cache

    fun mapAllToCache(c: List<Source>): List<Cache>
}