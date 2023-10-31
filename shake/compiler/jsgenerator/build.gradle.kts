import conventions.dependencies

group = "io.github.shakelang.shake"
version = "0.1.0"
description = "js-generator"
java.sourceCompatibility = JavaVersion.VERSION_1_8

plugins {
    id("conventions.all")
    id("conventions.publishing")
}

kotlin {
    dependencies {
        implementation(project(":util:parseutils"))
        implementation(project(":util:shason"))
        implementation(project(":shake:compiler:lexer"))
        implementation(project(":shake:compiler:parser"))
        implementation(project(":shake:compiler:processor"))
        testImplementation(kotlin("test"))
    }
}