package com.github.ponsea.ziovalidation

import zio.{ Exit, UIO, ZIO }

object ZValidation {
  // same as ZIO.mapParN except that await all results and combine errors
  def mapParN[R, E, A, B, C](zio1: ZIO[R, E, A], zio2: ZIO[R, E, B])(f: (A, B) => C): ZIO[R, E, C] = {
    zio1.validateWithPar(zio2)(f)
  }

  def mapParN[R, E, A, B, C, D](zio1: ZIO[R, E, A], zio2: ZIO[R, E, B], zio3: ZIO[R, E, C])(
    f: (A, B, C) => D
  ): ZIO[R, E, D] = {
    (zio1 <|> zio2 <|> zio3).map {
      case ((a, b), c) => f(a, b, c)
    }
  }

  def mapParN[R, E, A, B, C, D, F](
    zio1: ZIO[R, E, A],
    zio2: ZIO[R, E, B],
    zio3: ZIO[R, E, C],
    zio4: ZIO[R, E, D]
  )(f: (A, B, C, D) => F): ZIO[R, E, F] = {
    (zio1 <|> zio2 <|> zio3 <|> zio4).map {
      case (((a, b), c), d) => f(a, b, c, d)
    }
  }

  implicit class ZIOOps[-R, +E, +A](self: ZIO[R, E, A]) {
    // same as ZIO#zipWithPar except that await results and combine errors
    def validateWithPar[R1 <: R, E1 >: E, B, C](that: ZIO[R1, E1, B])(f: (A, B) => C): ZIO[R1, E1, C] = {
      for {
        fiber1 <- self.fork
        fiber2 <- that.fork
        exit1  <- fiber1.await
        exit2  <- fiber2.await
        result <- exitToZIO(exit1 <&> exit2)
      } yield f.tupled(result)
    }

    // same as ZIO#zipPar except that await results and combine errors
    def validatePar[R1 <: R, E1 >: E, B](that: ZIO[R1, E1, B]): ZIO[R1, E1, (A, B)] = {
      validateWithPar(that)((a, b) => (a, b))
    }

    // alias for `validatePar`
    def <|>[R1 <: R, E1 >: E, B](that: ZIO[R1, E1, B]): ZIO[R1, E1, (A, B)] = {
      self.validatePar(that)
    }
  }

  private def exitToZIO[E, A](exit: Exit[E, A]) = {
    exit.foldM(ZIO.halt(_), UIO(_))
  }
}
