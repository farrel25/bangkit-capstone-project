package com.b21cap0051.naratik.dataresource.datamodellist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleModel(
	var id : String? = null ,
	var image : String? = null ,
	var imageWriter : String? = null ,
	var title : String? = null ,
	var date : String? = null ,
	var writer : String? = null ,
	var overview : String? = null ,
	
	) : Parcelable