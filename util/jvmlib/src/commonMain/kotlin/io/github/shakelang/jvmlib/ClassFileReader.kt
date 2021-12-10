package io.github.shakelang.jvmlib

import io.github.shakelang.jvmlib.infos.ClassInfo
import io.github.shakelang.parseutils.streaming.CountingInputStream
import io.github.shakelang.parseutils.streaming.DataInputStream
import io.github.shakelang.parseutils.streaming.InputStream

object ClassFileReader
{

    fun readClass(input: InputStream): ClassInfo {

        val counter = CountingInputStream(input)
        val stream = DataInputStream(counter)

        try {
            return ClassInfo.fromStream(stream)
        } catch (e: Throwable) {
            throw RuntimeException("Error at position 0x${counter.getCount().toString(16)} while parsing class", e)
        }

    }

}