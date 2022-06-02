package com.github.hongbeomi.fixtures

import com.github.hongbeomi.domain.Photo
import kotlin.random.Random

object Fixtures {

    private val randomInt: Int
        get() = Random.nextInt()

    fun photo(
        id: String = "1",
        farm: Int = randomInt,
        secret: String = "secret",
        server: Int = randomInt
    ) = Photo(
        id = id,
        farm = farm,
        secret = secret,
        server = server
    )

}