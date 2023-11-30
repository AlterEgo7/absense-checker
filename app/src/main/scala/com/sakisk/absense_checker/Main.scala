/*
 * Copyright 2023 Sakis Karagiannis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sakisk.absense_checker

import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s.*
import com.sakisk.absense_checker.http.Routes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.syntax.all.*
import cats.syntax.all.*

object Main extends IOApp:
  override def run(args: List[String]): IO[ExitCode] =
    Routes[IO].routes
      .flatMap { routes =>
        EmberServerBuilder
          .default[IO]
          .withPort(port"9000")
          .withHost(host"0.0.0.0")
          .withHttpApp((routes <+> Routes[IO].docRoutes).orNotFound)
          .build
      }
      .useForever
      .as(ExitCode.Success)
