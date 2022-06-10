package io.github.shakelang.shake

import io.github.shakelang.shake.processor.ShakeCodeProcessor

fun main(args: Array<String>) {

    val processor = ShakeCodeProcessor()
    processor.loadFile("shake/compiler/processor/src/commonTest/resources", "test.shake")
    processor.loadFile("shake/compiler/processor/src/commonTest/resources", "io/github/shakelang/test.shake")
    val project = processor.finish()
    println(project.toJsonString())

}