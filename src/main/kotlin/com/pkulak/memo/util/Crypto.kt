package com.pkulak.memo.util

import com.google.common.io.BaseEncoding
import uk.oczadly.karl.jnano.model.NanoAccount

fun String.bytes() = BaseEncoding.base16().decode(this)
fun String.accountBytes() = NanoAccount.parseAddress(this).toPublicKey().bytes()
fun ByteArray.hex() = BaseEncoding.base16().encode(this)