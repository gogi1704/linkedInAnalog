package com.example.linkedinanalog.data.models.post

import com.example.linkedinanalog.data.models.Attachment
import com.example.linkedinanalog.data.models.Coordinates

data class PostCreateRequest(
     val id:Int  = -1,
     val content:String,
     val coords: Coordinates?,
     val link:String,
     val attachment: Attachment?,
     val mentionIds:List<Int>?
 )