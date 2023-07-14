package multistepform

import java.util.UUID

object Models {
  final case class Answers(
      sessionId: String = "id1",
      currentStep: Int = 1,
      step1: StepAnswers.Step1 = StepAnswers.Step1(),
      step2: StepAnswers.Step2 = StepAnswers.Step2(),
      step3: StepAnswers.Step3 = StepAnswers.Step3()
  ) {
    // this is not enforced by compiler, sad, maintain by hand in html template files
    def fragmentName: String = s"step${currentStep}"
    def updateStep(stepNum: Int, rawData: String): Answers = {
      stepNum match {
        case 1 =>
          this.copy(
            step1 = this.step1.fromFormData(rawData),
            currentStep = stepNum + 1
          )
        case 2 =>
          this.copy(
            step2 = this.step2.fromFormData(rawData),
            currentStep = stepNum + 1
          )
        case 3 =>
          this.copy(
            step3 = this.step3.fromFormData(rawData),
            currentStep = stepNum + 1
          )
        case 4 =>
          this.copy(
            currentStep = stepNum + 1
          )
        case _ => this
      }
    }
  }

  enum PlanType:
    case Arcade, Advanced, Pro

  enum Addons:
    case OnlineService, LargerStorage, CustomProfile

  sealed trait StepAnswers {
    def fromFormData(rawData: String): StepAnswers
  }
  object StepAnswers {
    final case class Step1(
        name: String = "",
        email: String = "",
        phone: String = ""
    ) extends StepAnswers {
      override def fromFormData(rawData: String): Step1 = {
        println(s"parsing step 1 data $rawData")
        val fieldValues = rawData
          .split("&")
          .map { field =>
            val Array(name, value) = field.split("=")
            name -> value
          }
          .toMap

        val name = fieldValues.getOrElse("name", "")
        val email = fieldValues.getOrElse("email", "")
        val phone = fieldValues.getOrElse("phone", "")

        Step1(name, email, phone)
      }
    }
    final case class Step2(
        planType: PlanType = PlanType.Arcade,
        isYearly: Boolean = false
    ) extends StepAnswers {
      override def fromFormData(rawData: String): Step2 = {
        println(s"parsing step 2 data $rawData")
        val fieldValues = rawData
          .split("&")
          .map { field =>
            val Array(name, value) = field.split("=")
            name -> value
          }
          .toMap

        val planType =
          PlanType.valueOf(fieldValues.getOrElse("plan-type", "Arcade"))
        val isYearly = fieldValues.get("isPackageYearly").contains("on")

        Step2(planType, isYearly)
      }
    }
    final case class Step3(addons: Set[Addons] = Set.empty)
        extends StepAnswers {
      override def fromFormData(rawData: String): Step3 = {
        println(s"parsing step 3 data $rawData")
        // for multiple checkboxes data comes in form of
        // addon-services=OnlineService&addon-services=CustomProfile
        val fieldValues = rawData
          .split("&")
          .map { field =>
            val Array(name, value) = field.split("=")
            name -> value
          }

        val addonsStrings = fieldValues.groupMap(_._1)(_._2)
          .getOrElse("addon-services", Array.empty[String])
        println(s"in step 3 got strings $addonsStrings")
        val addons = addonsStrings.map(Addons.valueOf(_)).toSet

        Step3(addons)
      }
    }
  }
}
