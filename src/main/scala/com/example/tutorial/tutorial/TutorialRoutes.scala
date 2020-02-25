package com.example.tutorial.tutorial

import cats.effect.Sync
import cats.implicits._
import com.example.tutorial.tutorial.presentation.{Tutorial, UserName}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.CirceEntityCodec._

object TutorialRoutes {

  def jokeRoutes[F[_]: Sync](J: Jokes[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "joke" =>
        for {
          joke <- J.get
          resp <- Ok(joke)
        } yield resp
    }
  }

  def helloWorldRoutes[F[_]: Sync](H: HelloWorld[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting)
        } yield resp
    }
  }

  def tutorialRoutes[F[_]: Sync] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "tutorial" =>
        val tutorial = Tutorial(BigDecimal(1), "Http4s")
        for {
          resp <- Ok(tutorial)
        } yield resp
    }
  }

  def userNameInputRoutes[F[_]: Sync] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case req @ POST -> Root / "user-name" =>
        for {
          body <- req.as[UserName]
          _ = println(body)
          resp <- NoContent()
        } yield resp
    }
  }

  def userLoginRoutes[F[_]: Sync] = {
    val registeredUser = List(UserName(BigDecimal(9999), "Emmett Na"))

    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case req @ POST -> Root / "user-access" =>
        for {
          body <- req.as[UserName]
          resp <- registeredUser.contains(body) match {
            case true  => Ok("Welcome")
            case false => Forbidden("Unauthorized User")
          }
        } yield resp
    }
  }

}