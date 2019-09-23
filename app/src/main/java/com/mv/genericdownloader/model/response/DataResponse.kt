package com.mv.genericdownloader.model.response


import com.google.gson.annotations.SerializedName

data class DataResponse(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("width")
    val width: Int = 0,
    @SerializedName("height")
    val height: Int = 0,
    @SerializedName("color")
    val color: String = "",
    @SerializedName("likes")
    val likes: Int = 0,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean = false,
    @SerializedName("user")
    val user: User = User(),
    @SerializedName("current_user_collections")
    val currentUserCollections: List<Any> = listOf(),
    @SerializedName("urls")
    val urls: Urls = Urls(),
    @SerializedName("categories")
    val categories: List<Category> = listOf(),
    @SerializedName("links")
    val links: Links = Links()
) {
    data class User(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("username")
        val username: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("profile_image")
        val profileImage: ProfileImage = ProfileImage(),
        @SerializedName("links")
        val links: Links = Links()
    ) {
        data class ProfileImage(
            @SerializedName("small")
            val small: String = "",
            @SerializedName("medium")
            val medium: String = "",
            @SerializedName("large")
            val large: String = ""
        )

        data class Links(
            @SerializedName("self")
            val self: String = "",
            @SerializedName("html")
            val html: String = "",
            @SerializedName("photos")
            val photos: String = "",
            @SerializedName("likes")
            val likes: String = ""
        )
    }

    data class Urls(
        @SerializedName("raw")
        val raw: String = "",
        @SerializedName("full")
        val full: String = "",
        @SerializedName("regular")
        val regular: String = "",
        @SerializedName("small")
        val small: String = "",
        @SerializedName("thumb")
        val thumb: String = ""
    )

    data class Category(
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("title")
        val title: String = "",
        @SerializedName("photo_count")
        val photoCount: Int = 0,
        @SerializedName("links")
        val links: Links = Links()
    ) {
        data class Links(
            @SerializedName("self")
            val self: String = "",
            @SerializedName("photos")
            val photos: String = ""
        )
    }

    data class Links(
        @SerializedName("self")
        val self: String = "",
        @SerializedName("html")
        val html: String = "",
        @SerializedName("download")
        val download: String = ""
    )
}