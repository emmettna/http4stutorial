package com.example.tutorial.tutorial.presentation

import io.circe.generic.JsonCodec

@JsonCodec
final case class UserName(id: BigDecimal, name: String)
