package cz.cvut.riskio

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform