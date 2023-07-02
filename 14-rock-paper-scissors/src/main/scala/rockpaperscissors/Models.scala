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

  // yeah, maybe better to do in outer html div
  final case class Styling(
      var diameter: String,
      var bgDark: String,
      var bgBright: String
  )
  object Styling {
    val scissorsDark = "hsl(39, 89%, 49%)"
    val scissorsBright = "hsl(40, 84%, 53%)"
    val paperDark = "hsl(230, 89%, 62%)"
    val paperBright = "hsl(230, 89%, 65%)"
    val rockDark = "hsl(349, 71%, 52%)"
    val rockBright = "hsl(349, 70%, 56%)"

    def paperStyling(
        diameter: String,
        bgDark: String = paperDark,
        bgBright: String = paperBright
    ) = Styling(diameter, bgDark, bgBright)

    def scissorsStyling(
        diameter: String,
        bgDark: String = scissorsDark,
        bgBright: String = scissorsBright
    ) = Styling(diameter, bgDark, bgBright)

    def rockStyling(
        diameter: String,
        bgDark: String = rockDark,
        bgBright: String = rockBright
    ) = Styling(diameter, bgDark, bgBright)
  }

  sealed trait Choice {
    def name: String
    def iconPath: String
    def isBeating: Set[Choice]
  }
  object Choice {
    case object Paper extends Choice {
      def name: String = "paper"
      def iconPath: String = "public/images/icon-paper.svg"
      def isBeating: Set[Choice] = Set(Rock)
    }
    case object Scissors extends Choice {
      def name: String = "scissors"
      def iconPath: String = "public/images/icon-scissors.svg"
      def isBeating: Set[Choice] = Set(Paper)
    }
    case object Rock extends Choice {
      def name: String = "rock"
      def iconPath: String = "public/images/icon-rock.svg"
      def isBeating: Set[Choice] = Set(Scissors)
    }
  }

  /** this will be Data Transfer Object, because Thymeleaf wants var and i want
    * vals and enums
    */
  final case class ChoiceBadge(
      var s: Styling,
      var c: Choice,
      var p: Positioning
  )

  val choiceSelectionItems = {
    List(
      ChoiceBadge(
        Styling.paperStyling("8rem"),
        Choice.Paper,
        Positioning.Absolute("6rem", "6rem")
      ),
      ChoiceBadge(
        Styling.scissorsStyling("8rem"),
        Choice.Scissors,
        Positioning.Absolute("6rem", "17rem")
      ),
      ChoiceBadge(
        Styling.rockStyling("8rem"),
        Choice.Rock,
        Positioning.Absolute("15rem", "11.5rem")
      )
    )
  }

  final case class ShowdownState(
      var playersChoice: ChoiceBadge,
      var houseChoice: Option[ChoiceBadge],
      var gameEnded: Boolean
  ) {
    def gameResult: Option[String] = {
      houseChoice.map(houseSelectedChoice => {
        val player = playersChoice.c
        val house = houseSelectedChoice.c
        if (player == house) {
          "tied"
        } else if (player.isBeating(house)) {
          "win"
        } else "lose"
      })
    }
    def isHouseWin: Boolean = gameResult.contains("lose")
    def isPlayerWin: Boolean = gameResult.contains("win")
    def scoreChange: Int = {
      houseChoice.map(houseSelectedChoice => {
        val player = playersChoice.c
        val house = houseSelectedChoice.c
        if (player == house) {
          0
        } else if (player.isBeating(house)) {
          1
        } else -1
      }).getOrElse(0)
    }
  }
}
