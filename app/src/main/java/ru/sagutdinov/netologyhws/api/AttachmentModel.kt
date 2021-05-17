package ru.sagutdinov.netologyhws.api

import ru.sagutdinov.netologyhws.BASE_URL

class AttachmentModel(val id: String, val mediaType: AttachmentType) {
    val url
        get() = "$BASE_URL/api/v1/static/$id"
}

enum class AttachmentType {
    IMAGE
}