package rockpaperscissors

import java.beans.BeanProperty

object Models {
  sealed trait Positioning
  object Positioning {
    case object Relative extends Positioning {
      def toStyle(): String = " position: absolute; "
    }
    final case class Absolute(
        topOffset: String,
        leftOffset: String,
        translation: String = "50%"
    ) extends Positioning {
      def toStyle(): String =
        s"""
--top-offset: $topOffset;
--left-offset: $leftOffset;
--translation: $translation;
position: absolute;
"""
    }
  }

  sealed trait Choice {
    def diameter: String
    def bgDark: String
    def bgBright: String
    def name: String
    def iconPath: String
  }
  object Choice {
    val scissorsDark = "hsl(39, 89%, 49%)"
    val scissorsBright = "hsl(40, 84%, 53%)"
    val paperDark = "hsl(230, 89%, 62%)"
    val paperBright = "hsl(230, 89%, 65%)"
    val rockDark = "hsl(349, 71%, 52%)"
    val rockBright = "hsl(349, 70%, 56%)"

    case class Paper(
        diameter: String,
        bgDark: String = paperDark,
        bgBright: String = paperBright
    ) extends Choice {
      def name: String = "paper"
      def iconPath: String = "public/images/icon-paper.svg"
    }
    case class Scissors(
        diameter: String,
        bgDark: String = scissorsDark,
        bgBright: String = scissorsBright
    ) extends Choice {
      def name: String = "scissors"
      def iconPath: String = "public/images/icon-scissors.svg"
    }
    case class Rock(
        diameter: String,
        bgDark: String = rockDark,
        bgBright: String = rockBright
    ) extends Choice {
      def name: String = "rock"
      def iconPath: String = "public/images/icon-rock.svg"
    }
  }

  /** this will be Data Transfer Object, because Thymeleaf wants var and i want
    * vals and enums
    */
  final case class ChoiceBadge(
      var c: Choice,
      var p: Positioning
  )

  val choiceSelectionItems = {
    List(
      ChoiceBadge(Choice.Paper("8rem"), Positioning.Absolute("6rem", "6rem")),
      ChoiceBadge(
        Choice.Scissors("8rem"),
        Positioning.Absolute("6rem", "17rem")
      ),
      ChoiceBadge(Choice.Rock("8rem"), Positioning.Absolute("15rem", "11.5rem"))
    )
  }

}
