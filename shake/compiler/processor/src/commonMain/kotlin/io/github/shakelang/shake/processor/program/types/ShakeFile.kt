package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.notNull

interface ShakeFile {
    val signature: String
    val project: ShakeProject
    val pkg: ShakePackage?
    val name: String
    val scope: ShakeScope
    val parentScope: ShakeScope

    val fields: List<ShakeField>
    val functions: List<ShakeFunction>
    val classes: List<ShakeClass>
    val imports: List<ShakeImport>

    class Impl : ShakeFile {
        override val project: ShakeProject.Impl
        override val pkg: ShakePackage.Impl?
        override val name: String
        override val scope: ShakeScope.ShakeFileScope.Impl
        override val parentScope: ShakeScope.ShakeScopeImpl
        override val fields: List<ShakeField.Impl>
        override val functions: List<ShakeFunction.Impl>
        override val classes: List<ShakeClass.Impl>
        override val imports: List<ShakeImport.Impl>

        override val signature: String get() = "${pkg?.name?.plus(".") ?: ""}$name"

        constructor(
            parentScope: ShakeScope.ShakeFileScope.Impl,
            name: String,
            scope: ShakeScope.ShakeFileScope.Impl,
            fields: List<ShakeField.Impl>,
            functions: List<ShakeFunction.Impl>,
            classes: List<ShakeClass.Impl>,
            imports: List<ShakeImport.Impl>
        ) {
            this.project = parentScope.project
            this.pkg = parentScope.pkg
            this.parentScope = parentScope
            this.name = name
            this.scope = scope
            this.fields = fields
            this.functions = functions
            this.classes = classes
            this.imports = imports
        }

        constructor(
            parentScope: ShakeScope.ShakeScopeImpl,
            it: ShakeFile
        ) {
            this.project = parentScope.project
            this.pkg = parentScope.pkg
            this.parentScope = parentScope
            this.name = it.name
            this.scope = ShakeScope.ShakeFileScope.from(this)
            this.fields = it.fields.map { ShakeField.from(project, pkg, it) }
            this.functions = it.functions.map { ShakeFunction.from(scope, it) }
            this.classes = it.classes.map { ShakeClass.from(scope, it) }
            this.imports = it.imports.map { ShakeImport.from(this, it) }
        }

        constructor(
            name: String,
            parentScope: ShakeScope.ShakeScopeImpl,
            createImports: (Impl) -> List<ShakeImport.Impl>,
            createFields: (Impl) -> List<ShakeField.Impl>,
            createFunctions: (Impl) -> List<ShakeFunction.Impl>,
            createClasses: (Impl) -> List<ShakeClass.Impl>
        ) {
            this.project = parentScope.project
            this.pkg = parentScope.pkg
            this.parentScope = parentScope
            this.name = name
            this.scope = ShakeScope.ShakeFileScope.from(this)
            this.imports = createImports(this)
            this.fields = createFields(this)
            this.functions = createFunctions(this)
            this.classes = createClasses(this)
        }
    }

    companion object {
        fun from(scope: ShakeScope.ShakeScopeImpl, file: ShakeFile): Impl {
            return Impl(scope, file)
        }

        fun create(
            name: String,
            parentScope: ShakeScope.ShakeScopeImpl,
            createImports: (Impl) -> List<ShakeImport.Impl>,
            createFields: (Impl) -> List<ShakeField.Impl>,
            createFunctions: (Impl) -> List<ShakeFunction.Impl>,
            createClasses: (Impl) -> List<ShakeClass.Impl>
        ): Impl {
            return Impl(name, parentScope, createImports, createFields, createFunctions, createClasses)
        }
    }
}

interface ShakeImport {
    val it: Array<String>
    val file: ShakeFile
    val project: ShakeProject
    val name: String get() = it.joinToString(".")
    val targetPackage: ShakePackage
    val targetPackagePointer: Pointer<ShakePackage>

    class Impl(
        override val it: Array<String>,
        override val file: ShakeFile,
    ) : ShakeImport {
        override val project: ShakeProject get() = file.project
        override val targetPackage: ShakePackage get() = targetPackagePointer.value
        override val targetPackagePointer: Pointer<ShakePackage> = project.getPackage(it).notNull()
    }

    companion object {
        fun from(file: ShakeFile, it: ShakeImport): Impl {
            return Impl(
                it.it,
                file,
            )
        }
        fun create(
            file: ShakeFile,
            it: Array<String>
        ): Impl {
            return Impl(
                it,
                file,
            )
        }

    }
}
