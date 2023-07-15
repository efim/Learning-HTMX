package multistepform

import java.util.UUID

object Models {
  val testAnsw = Answers(
    sessionId = "id1",
    currentStep = 1,
    step1 = StepAnswers.Step1("Test Name", "some@email.com", "+9876", true),
    step2 = StepAnswers.Step2(PlanType.Advanced, true, true),
    step3 = StepAnswers.Step3(Set(Addons.LargerStorage), true),
    step4 = StepAnswers.Step4(true)
  )

  /** Labels and form info which dynamically depend on user answers e.g plan
    * name 'Arcade (Yearly)' vs 'Pro (Monthly)'
    */
  final case class FormData(
      userAnswers: Answers
  ) {
    def planDiscountMessage: Option[String] =
      if (userAnswers.step2.isYearly) Some("2 monts free") else None

    // yeah, in real world it will not be this simple
    def yearlyCost(monthlyCost: Int): Int = 10 * monthlyCost

    def periodCostLabel: String = {
      if (userAnswers.step2.isYearly) "yr" else "mo"
    }

    def planCost: Int = {
      val monthlyPlanCost = userAnswers.step2.planType match {
        case PlanType.Arcade   => 9
        case PlanType.Advanced => 12
        case PlanType.Pro      => 15
      }
      if (userAnswers.step2.isYearly) yearlyCost(monthlyPlanCost)
      else monthlyPlanCost
    }

    def addonMontlyCost: Addons => Int = {
      case Addons.OnlineService => 1
      case Addons.LargerStorage => 2
      case Addons.CustomProfile => 2
    }

    def addonCost(addon: Addons): Int = {
      val monthCost = addonMontlyCost(addon)
      if (userAnswers.step2.isYearly) yearlyCost(monthCost) else monthCost
    }

    def fullPlanName: String = {
      val period = if (userAnswers.step2.isYearly) "Yearly" else "Monthly"
      s"${userAnswers.step2.planType} (${period})"
    }

    def fullOrderPrice: Int = {
      planCost + userAnswers.step3.addons.map(addonCost).sum
    }
  }

  final case class Answers(
      sessionId: String = "id1",
      currentStep: Int = 1,
      step1: StepAnswers.Step1 =
        StepAnswers.Step1("Test Name", "some@email.com", "+9876", true),
      step2: StepAnswers.Step2 =
        StepAnswers.Step2(PlanType.Advanced, true, true),
      step3: StepAnswers.Step3 =
        StepAnswers.Step3(Set(Addons.LargerStorage), true),
      step4: StepAnswers.Step4 = StepAnswers.Step4()
  ) {
    // this is not enforced by compiler, sad, maintain by hand in html template files
    def fragmentName: String = s"step${currentStep}"
    def updateStep(stepNum: Int, rawData: String, nextStep: Int): Answers = {
      stepNum match {
        case 1 =>
          this.copy(
            step1 = this.step1.fromFormData(rawData),
            currentStep = nextStep
          )
        case 2 =>
          this.copy(
            step2 = this.step2.fromFormData(rawData),
            currentStep = nextStep
          )
        case 3 =>
          this.copy(
            step3 = this.step3.fromFormData(rawData),
            currentStep = nextStep
          )
        case 4 =>
          this.copy(
            step4 = this.step4,
            currentStep = nextStep
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
    def submitted: Boolean
  }
  object StepAnswers {
    final case class Step1(
        name: String = "",
        email: String = "",
        phone: String = "",
        override val submitted: Boolean = false
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

        Step1(name, email, phone, submitted = true)
      }
    }
    final case class Step2(
        planType: PlanType = PlanType.Arcade,
        isYearly: Boolean = false,
        override val submitted: Boolean = false
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

        Step2(planType, isYearly, submitted = true)
      }
    }
    final case class Step3(
        addons: Set[Addons] = Set.empty,
        override val submitted: Boolean = false
    ) extends StepAnswers {

      def containsAddon(addonName: String): Boolean = {
        addons.contains(Addons.valueOf(addonName))
      }

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

        val addonsStrings = fieldValues
          .groupMap(_._1)(_._2)
          .getOrElse("addon-services", Array.empty[String])
        println(s"in step 3 got strings $addonsStrings")
        val addons = addonsStrings.map(Addons.valueOf(_)).toSet

        Step3(addons, submitted = true)
      }
    }
    final case class Step4(
        override val submitted: Boolean = false
    ) extends StepAnswers {
      override def fromFormData(rawData: String): Step4 = Step4(true)
    }
  }
}
