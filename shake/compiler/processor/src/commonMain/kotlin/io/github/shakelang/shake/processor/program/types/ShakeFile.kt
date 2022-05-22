package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.util.Pointer

interface ShakeFile {
    val signature: String
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

        override val signature: String get() = "${pkg.name}.$name"

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
            this.scope = ShakeScope.ShakeFileScope.from(this, it.scope)
            this.fields = it.fields.map { ShakeField.from(prj, pkg, it) }
            this.functions = it.functions.map { ShakeFunction.from(prj, pkg, it) }
            this.classes = it.classes.map { ShakeClass.from(prj, pkg, it) }
            this.imports = it.imports.map { ShakeImport.from(prj, this, it) }
        }
    }
}

interface ShakeImport {
    val it: Array<String>
    val file: ShakeFile
    val name: String get() = it.joinToString(".")
    val targetPackage: ShakePackage
    val targetPackagePointer: Pointer<ShakePackage>

    class Impl(
        override val it: Array<String>,
        override val file: ShakeFile,
        override val targetPackagePointer: Pointer<ShakePackage>
    ) : ShakeImport {
        override val targetPackage: ShakePackage get() = targetPackagePointer.value
    }

    companion object {
        fun from(prj: ShakeProject, file: ShakeFile, it: ShakeImport): ShakeImport {
            val name = it.targetPackage.qualifiedName
            return Impl(
                it.it,
                file,
                prj.getPackage(name).transform { it ?: throw IllegalStateException("Package not found: $name") }
            )
        }
    }
}
