package testimonialsgrid
import scala.beans.BeanProperty
import java.util.UUID
import scala.util.Random

final case class Testimonial(
    @BeanProperty var avatarUrl: String,
    @BeanProperty var author: String,
    @BeanProperty var header: String,
    @BeanProperty var text: String,
    @BeanProperty var id: String,
    @BeanProperty var additionalColorClasses: String,
    @BeanProperty var additionalSizeClasses: String,
    @BeanProperty var followupOrientation: Int = 0,
    sequentSizeClasses: List[String] = List("")
) {
  def setNextSizeClass(key: Int): Unit = {
    val nextOrientationIndex = (key + 1) % sequentSizeClasses.size
    this.additionalSizeClasses = sequentSizeClasses(nextOrientationIndex)
    this.followupOrientation = nextOrientationIndex
  }
}

object Testimonial {
  val colorful = List(
    new Testimonial(
      "public/images/image-patrick.jpg",
      "Leopold",
      "  Odio facilisis mauris sit amet massa vitae tortor condimentum lacinimport java.util.UUID ia quis vel eros donec ac odio tempor orci dapibus ultrices. ",
      "  Nibh sed pulvinar proin gravida hendrerit? Massa tincidunt nunc pulvinar sapien et ligula libero nunc!",
      UUID.randomUUID().toString(),
      "bg-red-500",
      ""
    ),
    new Testimonial(
      "public/images/image-jonathan.jpg",
      "Aragorn",
      "  Eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada bibendum! ",
      "  Egestas fringilla phasellus faucibus scelerisque eleifend! Dignissim enim, sit amet venenatis urna cursus eget nunc scelerisque viverra mauris, in aliquam sem fringilla ut morbi tincidunt augue interdum velit euismod in! ",
      UUID.randomUUID().toString(),
      "bg-blue-500",
      "md:col-span-2"
    ),
    new Testimonial(
      "public/images/image-jeanette.jpg",
      "Jeanatte Mamamia",
      "  Id venenatis a, condimentum vitae sapien pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas sed tempus, urna et pharetra pharetra! ",
      "  Amet nulla facilisi morbi tempus iaculis urna, id volutpat lacus laoreet non curabitur gravida arcu ac tortor dignissim convallis aenean et tortor. Lorem dolor, sed viverra ipsum nunc aliquet bibendum? ",
      UUID.randomUUID().toString(),
      "bg-green-200 text-black",
      "md:row-span-2"
    ),
    new Testimonial(
      "public/images/image-kira.jpg",
      "Mamma mia",
      "  Id venenatis a, condimentum vitae sapien pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas sed tempus, urna et pharetra pharetra! ",
      """        Quis hendrerit dolor magna eget est lorem ipsum dolor sit amet, consectetur adipiscing elit pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis! Nam libero justo, laoreet sit amet cursus sit amet, dictum sit amet justo donec.
""",
      UUID.randomUUID().toString(),
      "bg-amber-200 text-black",
      "md:col-span-2"
    )
  )

  val sameAsRequested = List(
    new Testimonial(
      "public/images/image-daniel.jpg",
      "Daniel Clifford",
      """I received a job offer mid-course, and the subjects I learned were current, if not more so,
  in the company I joined. I honestly feel I got every penny’s worth.
""",
      """  “ I was an EMT for many years before I joined the bootcamp. I’ve been looking to make a
  transition and have heard some people who had an amazing experience here. I signed up
  for the free intro course and found it incredibly fun! I enrolled shortly thereafter.
  The next 12 weeks was the best - and most grueling - time of my life. Since completing
  the course, I’ve successfully switched careers, working as a Software Engineer at a VR startup. ”
""",
      UUID.randomUUID().toString(),
      "bg-moderate-violet",
      "md:col-span-2",
      sequentSizeClasses = List("md:row-span-2", "md:col-span-2")
    ),
    new Testimonial(
      "public/images/image-jonathan.jpg",
      "Jonathan Walters",
      "  The team was very supportive and kept me motivated ",
      """      "  Egestas fringilla phasellus faucibus scelerisque eleifend! Dignissim enim, sit amet venenatis urna cursus eget nunc scelerisque viverra mauris, in aliquam sem fringilla ut morbi tincidunt augue interdum velit euismod in! ",
""",
      UUID.randomUUID().toString(),
      "bg-very-dark-grayish-blue",
      "md:col-span-1"
    ),
    new Testimonial(
      "public/images/image-jeanette.jpg",
      "Jeanette Harmon",
      "An overall wonderful and rewarding experience",
      """  “ Thank you for the wonderful experience! I now have a job I really enjoy, and make a good living
  while doing something I love. ”
""",
      UUID.randomUUID().toString(),
      "bg-white text-very-dark-blackish-blue",
      "md:col-span-1"
    ),
    new Testimonial(
      "public/images/image-patrick.jpg",
      "Patrick Abrams",
      """      "  Eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada bibendum! ",
""",
      """  “ The staff seem genuinely concerned about my progress which I find really refreshing. The program
  gave me the confidence necessary to be able to go out in the world and present myself as a capable
  junior developer. The standard is above the rest. You will get the personal attention you need from
  an incredible community of smart and amazing people. ”
""",
      UUID.randomUUID().toString(),
      "bg-very-dark-blackish-blue",
      "md:col-span-2",
      sequentSizeClasses = List("md:row-span-2", "md:col-span-2")
    ),
    new Testimonial(
      "public/images/image-kira.jpg",
      "Kira Whittle",
      "  Such a life-changing experience. Highly recommended! ",
      """          “ Before joining the bootcamp, I’ve never written a line of code. I
          needed some structure from professionals who can help me learn
          programming step by step. I was encouraged to enroll by a former
          student of theirs who can only say wonderful things about the program.
          The entire curriculum and staff did not disappoint. They were very
          hands-on and I never had to wait long for assistance. The agile team
          project, in particular, was outstanding. It took my learning to the
          next level in a way that no tutorial could ever have. In fact, I’ve
          often referred to it during interviews as an example of my developent
          experience. It certainly helped me land a job as a full-stack
          developer after receiving multiple offers. 100% recommend! ”
""",
      UUID.randomUUID().toString(),
      "bg-white text-very-dark-blackish-blue",
      "md:row-span-2 md:col-end-[-1] md:row-start-1",
      sequentSizeClasses = List("md:row-span-2","md:col-span-2")
    )
  )

}
