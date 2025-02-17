/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package io.ktor.network.util

import kotlinx.cinterop.*
import platform.osx.*
import platform.posix.*

internal actual fun <T> unpack_sockaddr_un(
    sockaddr: sockaddr,
    block: (family: UShort, path: String) -> T
): T {
    val address = sockaddr.ptr.reinterpret<sockaddr_un>().pointed
    return block(address.sun_family.convert(), address.sun_path.toKString())
}

internal actual fun pack_sockaddr_un(
    family: UShort,
    path: String,
    block: (address: CPointer<sockaddr>, size: socklen_t) -> Unit
) {
    cValue<sockaddr_un> {
        strcpy(sun_path, path)
        sun_family = family.convert()

        block(ptr.reinterpret(), sizeOf<sockaddr_un>().convert())
    }
}
