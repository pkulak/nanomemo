package com.pkulak.memo.util

import com.google.common.io.BaseEncoding

fun String.bytes() = BaseEncoding.base16().decode(this)
fun ByteArray.hex() = BaseEncoding.base16().encode(this)