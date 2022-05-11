package io.github.shakelang.shake

import io.github.shakelang.shake.processor.ShakePackageBasedProcessor
import io.github.shakelang.shake.processor.program.types.ShakeProject

fun main(args: Array<String>) {

    val processor = ShakePackageBasedProcessor()
    processor.loadFile("shake/compiler/processor/src/commonTest/resources", "test.shake")
    processor.loadFile("shake/compiler/processor/src/commonTest/resources", "io/github/shakelang/test.shake")
    val project = ShakeProject.from(processor.finish())

    println(project.toJsonString())


}