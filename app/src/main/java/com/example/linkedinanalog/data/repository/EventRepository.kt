package com.example.linkedinanalog.data.repository

interface EventRepository {
   suspend fun getEvents()
}