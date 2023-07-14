package multistepform

import multistepform.Models.Answers

object Sessions {
  // the simplest form of storing data
  // i'll be relying on Render.com killing my app after 15 minutes of inactivity for GC
  // no need to manage concurrency really, because requests for same session are really far apart
  // and load will average to be 10req/day :shrug:
  val sessionReplies = scala.collection.mutable.Map.empty[String, Answers]
}
