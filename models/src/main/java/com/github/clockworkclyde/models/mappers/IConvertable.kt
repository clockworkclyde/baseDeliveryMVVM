package com.github.clockworkclyde.models.mappers

import kotlin.reflect.KClass

interface IConvertable {
    fun <I: Any> convertAs(clazz: KClass<I>): I?
}

@Suppress("UNCHECKED_CAST")
interface IConvertableTo<out T>: IConvertable {
    fun convertTo(): T?

    override fun <I : Any> convertAs(clazz: KClass<I>): I? = convertTo() as? I

}

interface IConvertableToWithParams<out T, in P: Any?>: IConvertable {

    fun convertTo(param: P): T

    override fun <I : Any> convertAs(clazz: KClass<I>): I? {
        return null
    }
}