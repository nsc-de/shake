package io.github.shakelang.shake.processor.program.mutable.code.values

import io.github.shakelang.shake.processor.program.types.code.values.*

interface MutableShakeConcatenation : MutableShakeValue, ShakeConcatenation

interface MutableShakeCalculation : ShakeConcatenation, MutableShakeConcatenation
interface MutableShakeAddition : MutableShakeCalculation, ShakeAddition
interface MutableShakeSubtraction : MutableShakeCalculation, ShakeSubtraction
interface MutableShakeMultiplication : MutableShakeCalculation, ShakeMultiplication
interface MutableShakeDivision : MutableShakeCalculation, ShakeDivision
interface MutableShakeModulus : MutableShakeCalculation, ShakeModulus
interface MutableShakePower : MutableShakeCalculation, ShakePower

interface MutableShakeComparison : MutableShakeConcatenation, ShakeComparison

interface MutableShakeEquals : MutableShakeComparison, ShakeEquals
interface MutableShakeNotEquals : MutableShakeComparison, ShakeNotEquals
interface MutableShakeLessThan : MutableShakeComparison, ShakeLessThan
interface MutableShakeLessThanOrEqual : MutableShakeComparison, ShakeLessThanOrEqual
interface MutableShakeGreaterThan : MutableShakeComparison, ShakeGreaterThan
interface MutableShakeGreaterThanOrEqual : MutableShakeComparison, ShakeGreaterThanOrEqual

interface MutableShakeLogical : MutableShakeValue, ShakeLogical
interface MutableShakeLogicalConcatenation : MutableShakeConcatenation, MutableShakeLogical

interface MutableShakeAnd : MutableShakeLogicalConcatenation, ShakeAnd
interface MutableShakeOr : MutableShakeLogicalConcatenation, ShakeOr
interface MutableShakeXor : MutableShakeLogicalConcatenation, MutableShakeLogical, ShakeXor
interface MutableShakeNot : MutableShakeLogical, ShakeNot
