package multistepform

import java.util.UUID

object Models {
  final case class Answers(
    sessionId: String = "id1",
    currentStep: Int = 1,
  )
}
