package org.helianto.user.api

case class SimpleUser(
userId: String,
principal: String,
displayName: String,
firstName: String,
lastName: String,
gender: String,
imageUrl: String)