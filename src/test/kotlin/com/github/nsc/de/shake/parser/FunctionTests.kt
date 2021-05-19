package com.github.nsc.de.shake.parser

import com.github.nsc.de.shake.assertType
import org.junit.jupiter.api.Test
import com.github.nsc.de.shake.parser.node.AccessDescriber
import com.github.nsc.de.shake.parser.node.VariableType
import com.github.nsc.de.shake.parser.node.functions.FunctionDeclarationNode
import com.github.nsc.de.shake.parser.node.factor.IntegerNode
import com.github.nsc.de.shake.parser.node.functions.ReturnNode
import org.junit.jupiter.api.Assertions

class FunctionTests {
    @Test
    fun testFunction() {
        val tree = ParserTestUtil.parse("<FunctionTest>", "function f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PACKAGE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.DYNAMIC, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testFunctionChildren() {
        val tree = ParserTestUtil.parse("<FunctionChildrenTest>", "function f() { print(10); }")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PACKAGE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(1, node.body.children.size)
        Assertions.assertSame(VariableType.DYNAMIC, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testPublicFunction() {
        val tree = ParserTestUtil.parse("<PublicFunctionTest>", "public function f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PUBLIC, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.DYNAMIC, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testProtectedFunction() {
        val tree = ParserTestUtil.parse("<ProtectedFunctionTest>", "protected function f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PROTECTED, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.DYNAMIC, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testPrivateFunction() {
        val tree = ParserTestUtil.parse("<PrivateFunctionTest>", "private function f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PRIVATE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.DYNAMIC, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testFinalFunction() {
        val tree = ParserTestUtil.parse("<FinalFunctionTest>", "final function f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PACKAGE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.DYNAMIC, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertTrue(node.isFinal)
    }

    @Test
    fun testPublicFinalFunction() {
        val tree = ParserTestUtil.parse("<PublicFinalFunctionTest>", "public final function f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PUBLIC, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.DYNAMIC, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertTrue(node.isFinal)
    }

    @Test
    fun testProtectedFinalFunction() {
        val tree = ParserTestUtil.parse("<ProtectedFinalFunctionTest>", "protected final function f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PROTECTED, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.DYNAMIC, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertTrue(node.isFinal)
    }

    @Test
    fun testPrivateFinalFunction() {
        val tree = ParserTestUtil.parse("<PrivateFinalFunctionTest>", "private final function f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PRIVATE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.DYNAMIC, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertTrue(node.isFinal)
    }

    // ********************************************************************
    // C-Style
    @Test
    fun testCStyleFunction() {
        val tree = ParserTestUtil.parse("<CStyleFunctionTest>", "int f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PACKAGE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.INTEGER, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testCStyleFunctionChildren() {
        val tree = ParserTestUtil.parse("<CStyleFunctionChildrenTest>", "int f() { print(10); }")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PACKAGE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(1, node.body.children.size)
        Assertions.assertSame(VariableType.INTEGER, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testCStylePublicFunction() {
        val tree = ParserTestUtil.parse("<CStylePublicFunctionTest>", "public int f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PUBLIC, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.INTEGER, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testCStyleProtectedFunction() {
        val tree = ParserTestUtil.parse("<CStyleProtectedFunctionTest>", "protected int f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PROTECTED, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.INTEGER, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testCStylePrivateFunction() {
        val tree = ParserTestUtil.parse("<CStylePrivateFunctionTest>", "private int f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PRIVATE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.INTEGER, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testCStyleFinalFunction() {
        val tree = ParserTestUtil.parse("<CStyleFinalFunctionTest>", "final int f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PACKAGE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.INTEGER, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertTrue(node.isFinal)
    }

    @Test
    fun testCStylePublicFinalFunction() {
        val tree = ParserTestUtil.parse("<CStylePublicFinalFunctionTest>", "public final int f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PUBLIC, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.INTEGER, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertTrue(node.isFinal)
    }

    @Test
    fun testCStyleProtectedFinalFunction() {
        val tree = ParserTestUtil.parse("<CStyleProtectedFinalFunctionTest>", "protected final int f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PROTECTED, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.INTEGER, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertTrue(node.isFinal)
    }

    @Test
    fun testCStylePrivateFinalFunction() {
        val tree = ParserTestUtil.parse("<CStylePrivateFinalFunctionTest>", "private final int f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PRIVATE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.INTEGER, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertTrue(node.isFinal)
    }

    @Test
    fun testCStyleFunctionByte() {
        val tree = ParserTestUtil.parse("<CStyleFunctionByteTest>", "byte f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PACKAGE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.BYTE, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testCStyleFunctionShort() {
        val tree = ParserTestUtil.parse("<CStyleFunctionShortTest>", "short f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PACKAGE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.SHORT, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testCStyleFunctionLong() {
        val tree = ParserTestUtil.parse("<CStyleFunctionLongTest>", "long f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PACKAGE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.LONG, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testCStyleFunctionFloat() {
        val tree = ParserTestUtil.parse("<CStyleFunctionFloatTest>", "float f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PACKAGE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.FLOAT, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testCStyleFunctionDouble() {
        val tree = ParserTestUtil.parse("<CStyleFunctionDoubleTest>", "double f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PACKAGE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.DOUBLE, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testCStyleFunctionChar() {
        val tree = ParserTestUtil.parse("<CStyleFunctionCharTest>", "char f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PACKAGE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.CHAR, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testCStyleFunctionBoolean() {
        val tree = ParserTestUtil.parse("<CStyleFunctionBooleanTest>", "boolean f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PACKAGE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.BOOLEAN, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testCStyleFunctionVoid() {
        val tree = ParserTestUtil.parse("<CStyleFunctionVoidTest>", "void f() {}")
        Assertions.assertEquals(1, tree.children.size)
        assertType(FunctionDeclarationNode::class.java, tree.children[0])
        val node = tree.children[0] as FunctionDeclarationNode
        Assertions.assertSame(AccessDescriber.PACKAGE, node.access)
        Assertions.assertEquals("f", node.name)
        Assertions.assertSame(0, node.body.children.size)
        Assertions.assertSame(VariableType.VOID, node.type)
        Assertions.assertFalse(node.isInClass)
        Assertions.assertFalse(node.isStatic)
        Assertions.assertFalse(node.isFinal)
    }

    @Test
    fun testReturn() {
        val tree = ParserTestUtil.parse("<FunctionTest>", "return 10;")
        Assertions.assertEquals(1, tree.children.size)
        assertType(ReturnNode::class.java, tree.children[0])
        val node = tree.children[0] as ReturnNode
        assertType(IntegerNode::class.java, node.value)
    }
}