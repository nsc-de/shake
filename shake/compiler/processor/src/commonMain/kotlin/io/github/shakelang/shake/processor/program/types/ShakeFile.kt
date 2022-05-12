package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeScope

interface ShakeFile {
    val prj: ShakeProject
    val pkg: ShakePackage
    val name: String
    val scope: ShakeScope

    val fields: List<ShakeField>
    val functions: List<ShakeFunction>
    val classes: List<ShakeClass>
    val imports: List<ShakeImport>

    class Impl : ShakeFile {
        override val prj: ShakeProject
        override val pkg: ShakePackage
        override val name: String
        override val scope: ShakeScope
        override val fields: List<ShakeField>
        override val functions: List<ShakeFunction>
        override val classes: List<ShakeClass>
        override val imports: List<ShakeImport>

        constructor(
            prj: ShakeProject,
            pkg: ShakePackage,
            name: String,
            scope: ShakeScope,
            fields: List<ShakeField>,
            functions: List<ShakeFunction>,
            classes: List<ShakeClass>,
            imports: List<ShakeImport>
        ) {
            this.prj = prj
            this.pkg = pkg
            this.name = name
            this.scope = scope
            this.fields = fields
            this.functions = functions
            this.classes = classes
            this.imports = imports
        }

        constructor(
            prj: ShakeProject,
            pkg: ShakePackage,
            it: ShakeFile
        ) {
            this.prj = prj
            this.pkg = pkg
            this.name = it.name
            this.scope = ShakeScope.from(prj, it.scope)
            this.fields = it.fields.map { ShakeField.from(prj, it) }
            this.functions = it.functions.map { ShakeFunction.from(prj, it) }
            this.classes = it.classes.map { ShakeClass.from(prj, it) }
            this.imports = it.imports.map { ShakeImport.from(prj, it) }
        }
    }
}

interface ShakeImport {
    val it: Array<String>
    val name: String get() = it.joinToString(".")

    class Impl(override val it: Array<String>) : ShakeImport
    companion object {
        fun from(prj: ShakeProject, it: ShakeImport): ShakeImport = Impl(it.it)
    }
}