package com.axon.demobiometricauth.base.usecase

interface UseCaseWithoutParamsSync<out T> {
    operator fun invoke(): T
}