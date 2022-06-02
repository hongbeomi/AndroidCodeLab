package com.github.hongbeomi.domain

fun Photo.getImageUrl(): String {
    return "https://live.staticflickr.com/$server/${id}_$secret.jpg"
}