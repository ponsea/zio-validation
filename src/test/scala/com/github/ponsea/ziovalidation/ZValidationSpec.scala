package com.github.ponsea.ziovalidation

import org.scalatest.FlatSpec
import zio.{ IO, Runtime, UIO, ZIO }

class ZValidationSpec extends FlatSpec {
  val runtime = Runtime.default

  "ZValidation.mapParN" should "returns success values when all tasks are successful" in {
    val task1 = UIO("task1")
    val task2 = UIO(2)
    val task3 = UIO(Some(3))
    val task4 = UIO(List("task4"))

    val program1 = ZValidation.mapParN(task1, task2)((r1, r2) => (r1, r2))
    val program2 = ZValidation.mapParN(task1, task2, task3)((r1, r2, r3) => (r1, r2, r3))
    val program3 = ZValidation.mapParN(task1, task2, task3, task4)((r1, r2, r3, r4) => (r1, r2, r3, r4))

    val actual1 = runtime.unsafeRun(program1)
    val actual2 = runtime.unsafeRun(program2)
    val actual3 = runtime.unsafeRun(program3)

    assert(actual1 === ("task1", 2))
    assert(actual2 === ("task1", 2, Some(3)))
    assert(actual3 === ("task1", 2, Some(3), List("task4")))
  }

  "ZValidation.mapParN" should "returns the error when one error occurs" in {
    val task1                  = UIO("task1")
    val task2: IO[String, Int] = ZIO.fail("error2")
    val task3                  = UIO(Some(3))
    val task4                  = UIO(List("task4"))

    val program1 = ZValidation
      .mapParN(task1, task2)((r1, r2) => (r1, r2))
      .flip
      .eventually
    val program2 = ZValidation
      .mapParN(task1, task2, task3)((r1, r2, r3) => (r1, r2, r3))
      .flip
      .eventually
    val program3 = ZValidation
      .mapParN(task1, task2, task3, task4)((r1, r2, r3, r4) => (r1, r2, r3, r4))
      .flip
      .eventually

    val actual1 = runtime.unsafeRun(program1)
    val actual2 = runtime.unsafeRun(program2)
    val actual3 = runtime.unsafeRun(program3)

    assert(actual1 === "error2")
    assert(actual2 === "error2")
    assert(actual3 === "error2")
  }

  "ZValidation.mapParN" should "returns all errors when all tasks failed" in {
    val task1: IO[String, Int] = ZIO.fail("error1")
    val task2: IO[String, Int] = ZIO.fail("error2")
    val task3: IO[String, Int] = ZIO.fail("error3")
    val task4: IO[String, Int] = ZIO.fail("error4")

    val program1 = ZValidation
      .mapParN(task1, task2)((r1, r2) => (r1, r2))
      .parallelErrors
      .flip
      .eventually
    val program2 = ZValidation
      .mapParN(task1, task2, task3)((r1, r2, r3) => (r1, r2, r3))
      .parallelErrors
      .flip
      .eventually
    val program3 = ZValidation
      .mapParN(task1, task2, task3, task4)((r1, r2, r3, r4) => (r1, r2, r3, r4))
      .parallelErrors
      .flip
      .eventually

    val actual1 = runtime.unsafeRun(program1)
    val actual2 = runtime.unsafeRun(program2)
    val actual3 = runtime.unsafeRun(program3)

    assert(actual1 === List("error1", "error2"))
    assert(actual2 === List("error1", "error2", "error3"))
    assert(actual3 === List("error1", "error2", "error3", "error4"))
  }

  "ZValidation.mapN" should "returns success values when all tasks are successful" in {
    val task1 = UIO("task1")
    val task2 = UIO(2)
    val task3 = UIO(Some(3))
    val task4 = UIO(List("task4"))

    val program1 = ZValidation.mapN(task1, task2)((r1, r2) => (r1, r2))
    val program2 = ZValidation.mapN(task1, task2, task3)((r1, r2, r3) => (r1, r2, r3))
    val program3 = ZValidation.mapN(task1, task2, task3, task4)((r1, r2, r3, r4) => (r1, r2, r3, r4))

    val actual1 = runtime.unsafeRun(program1)
    val actual2 = runtime.unsafeRun(program2)
    val actual3 = runtime.unsafeRun(program3)

    assert(actual1 === ("task1", 2))
    assert(actual2 === ("task1", 2, Some(3)))
    assert(actual3 === ("task1", 2, Some(3), List("task4")))
  }

  "ZValidation.mapN" should "returns the error when one error occurs" in {
    val task1                  = UIO("task1")
    val task2: IO[String, Int] = ZIO.fail("error2")
    val task3                  = UIO(Some(3))
    val task4                  = UIO(List("task4"))

    val program1 = ZValidation
      .mapN(task1, task2)((r1, r2) => (r1, r2))
      .flip
      .eventually
    val program2 = ZValidation
      .mapN(task1, task2, task3)((r1, r2, r3) => (r1, r2, r3))
      .flip
      .eventually
    val program3 = ZValidation
      .mapN(task1, task2, task3, task4)((r1, r2, r3, r4) => (r1, r2, r3, r4))
      .flip
      .eventually

    val actual1 = runtime.unsafeRun(program1)
    val actual2 = runtime.unsafeRun(program2)
    val actual3 = runtime.unsafeRun(program3)

    assert(actual1 === "error2")
    assert(actual2 === "error2")
    assert(actual3 === "error2")
  }

  "ZValidation.mapN" should "returns all errors when all tasks failed" in {
    val task1: IO[String, Int] = ZIO.fail("error1")
    val task2: IO[String, Int] = ZIO.fail("error2")
    val task3: IO[String, Int] = ZIO.fail("error3")
    val task4: IO[String, Int] = ZIO.fail("error4")

    val program1 = ZValidation
      .mapN(task1, task2)((r1, r2) => (r1, r2))
      .parallelErrors
      .flip
      .eventually
    val program2 = ZValidation
      .mapN(task1, task2, task3)((r1, r2, r3) => (r1, r2, r3))
      .parallelErrors
      .flip
      .eventually
    val program3 = ZValidation
      .mapN(task1, task2, task3, task4)((r1, r2, r3, r4) => (r1, r2, r3, r4))
      .parallelErrors
      .flip
      .eventually

    val actual1 = runtime.unsafeRun(program1)
    val actual2 = runtime.unsafeRun(program2)
    val actual3 = runtime.unsafeRun(program3)

    assert(actual1 === List("error1", "error2"))
    assert(actual2 === List("error1", "error2", "error3"))
    assert(actual3 === List("error1", "error2", "error3", "error4"))
  }
}
