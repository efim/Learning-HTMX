package multistepform

import java.util.UUID
import scala.jdk.CollectionConverters._

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
    // yeah, in real world it will not be this simple
    def yearlyCost(monthlyCost: Int): Int = 10 * monthlyCost

    def periodCostLabel: String = {
      if (userAnswers.step2.isYearly) "yr" else "mo"
    }

    def selectedPlanCost: Int = planCost(userAnswers.step2.planType)

    def planCost(plan: PlanType): Int = {
      val monthlyPlanCost = plan.monthlyCost
      if (userAnswers.step2.isYearly) yearlyCost(monthlyPlanCost)
      else monthlyPlanCost
    }

    def addonCost(addon: Addons): Int = {
      val monthCost = addon.monthlyCost
      if (userAnswers.step2.isYearly) yearlyCost(monthCost) else monthCost
    }

    def fullPlanName: String = {
      val period = if (userAnswers.step2.isYearly) "Yearly" else "Monthly"
      s"${userAnswers.step2.planType} (${period})"
    }

    def fullOrderPrice: Int = {
      selectedPlanCost + userAnswers.step3.addons.map(addonCost).sum
    }

    def availablePlans = PlanType.values.toList.asJava

    def availableAddons = Addons.values.toList.asJava
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

  enum PlanType(val monthlyCost: Int):
    case Arcade extends PlanType(9)
    case Advanced extends PlanType(12)
    case Pro extends PlanType(15)
    def name(): String = {
      this.toString().replaceAll("([a-z])([A-Z])", "$1 $2")
    }

  enum Addons(val monthlyCost: Int, val description: String):
    case OnlineService extends Addons(1, "Access to multiplayer games")
    case LargerStorage extends Addons(2, "Extra 1TB of cloud storage")
    case CustomProfile extends Addons(2, "Custom theme on your profile")
    /** Change camel case into human readable. Adding single space before each
      * uppercase
      */
    def name(): String = {
      this.toString().replaceAll("([a-z])([A-Z])", "$1 $2")
    }

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
      def addonsAsJava = addons.asJava

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
