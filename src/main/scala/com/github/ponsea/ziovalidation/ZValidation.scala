package com.github.ponsea.ziovalidation

import zio.{ UIO, ZIO }

object ZValidation {
  import com.github.ponsea.ziovalidation.syntax._

  // same as ZIO.mapN except that await all results and combine errors
  def mapN[R, E, A, B, C](zio1: ZIO[R, E, A], zio2: ZIO[R, E, B])(f: (A, B) => C): ZIO[R, E, C] = {
    zio1.conzipWith(zio2)(f)
  }

  def mapN[R, E, A, B, C, D](zio1: ZIO[R, E, A], zio2: ZIO[R, E, B], zio3: ZIO[R, E, C])(
    f: (A, B, C) => D
  ): ZIO[R, E, D] = {
    (zio1 <+> zio2 <+> zio3).map {
      case ((a, b), c) => f(a, b, c)
    }
  }

  def mapN[R, E, A, B, C, D, F](
    zio1: ZIO[R, E, A],
    zio2: ZIO[R, E, B],
    zio3: ZIO[R, E, C],
    zio4: ZIO[R, E, D]
  )(f: (A, B, C, D) => F): ZIO[R, E, F] = {
    (zio1 <+> zio2 <+> zio3 <+> zio4).map {
      case (((a, b), c), d) => f(a, b, c, d)
    }
  }

  // same as ZIO.mapParN except that await all results and combine errors
  def mapParN[R, E, A, B, C](zio1: ZIO[R, E, A], zio2: ZIO[R, E, B])(f: (A, B) => C): ZIO[R, E, C] = {
    zio1.conzipWithPar(zio2)(f)
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

}
