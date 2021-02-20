package com.gnb.gnbapp.utils

import com.gnb.gnbapp.utils.Status.SUCCESS
import com.gnb.gnbapp.utils.Status.FAILURE
import com.gnb.gnbapp.utils.Status.LOADING

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(status = SUCCESS, data = data, message = null)

        fun <T> failure(data: T?, message: String) =
            Resource(status = FAILURE, data = data, message = message)

        fun <T> loading(data: T?): Resource<T> =
            Resource(status = LOADING, data = data, message = null)
    }
}