package io.github.shakelang.jvmlib.infos.constants

import io.github.shakelang.parseutils.bytes.stream
import io.github.shakelang.parseutils.bytes.toBytes
import kotlin.test.Test
import kotlin.test.assertEquals

class ConstantFloatTests {

    @Test
    fun test() {
        val constant = ConstantFloatInfo(1.0f)
        assertCompare(1.0f, constant.value)
        assertEquals(4, constant.tag)
        assertEquals("constant_float_info", constant.tagName)
    }

    @Test
    fun testContentFromStream() {
        val inputStream = 1.0f.toBytes().stream()
        val constant = ConstantFloatInfo.contentsFromStream(inputStream)
        assertCompare(1.0f, constant.value)
        assertEquals(4, constant.tag)
        assertEquals("constant_float_info", constant.tagName)
    }

    @Test
    fun testFromStream() {
        val inputStream = byteArrayOf(0x04, *1.0f.toBytes()).stream()
        val constant = ConstantFloatInfo.fromStream(inputStream)
        assertCompare(1.0f, constant.value)
        assertEquals(4, constant.tag)
        assertEquals("constant_float_info", constant.tagName)
    }

    @Test
    fun testContentsFromBytes() {
        val bytes = 1.0f.toBytes()
        val constant = ConstantFloatInfo.contentsFromBytes(bytes)
        assertCompare(1.0f, constant.value)
        assertEquals(4, constant.tag)
        assertEquals("constant_float_info", constant.tagName)
    }

    @Test
    fun testFromBytes() {
        val bytes = byteArrayOf(0x04, *1.0f.toBytes())
        val constant = ConstantFloatInfo.fromBytes(bytes)
        assertCompare(1.0f, constant.value)
        assertEquals(4, constant.tag)
        assertEquals("constant_float_info", constant.tagName)
    }

    @Test
    fun testToBytes() {
        val constant = ConstantFloatInfo(1.0f)
        assertEquals(byteArrayOf(0x04, *1.0f.toBytes()).toList(), constant.toBytes().toList())
    }

}