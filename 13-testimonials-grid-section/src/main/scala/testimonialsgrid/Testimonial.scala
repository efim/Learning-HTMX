package testimonialsgrid
import scala.beans.BeanProperty

final case class Testimonial(
    @BeanProperty var avatarUrl: String,
    @BeanProperty var author: String,
    @BeanProperty var header: String,
    @BeanProperty var text: String,
    @BeanProperty var age: Int,
    @BeanProperty var additionalClasses: String
)

object Testimonial {
  val colorful = List(
    new Testimonial(
      "public/images/image-patrick.jpg",
      "Leopold",
      "  Odio facilisis mauris sit amet massa vitae tortor condimentum lacinia quis vel eros donec ac odio tempor orci dapibus ultrices. ",
      "  Nibh sed pulvinar proin gravida hendrerit? Massa tincidunt nunc pulvinar sapien et ligula ullamcorper malesuada proin libero nunc, consequat interdum varius sit amet, mattis vulputate enim nulla aliquet porttitor lacus! ",
      91,
      "!bg-red-500"
    ),
    new Testimonial(
      "public/images/image-jeanette.jpg",
      "Jeanatte Mamamia",
      "  Id venenatis a, condimentum vitae sapien pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas sed tempus, urna et pharetra pharetra! ",
      "  Amet nulla facilisi morbi tempus iaculis urna, id volutpat lacus laoreet non curabitur gravida arcu ac tortor dignissim convallis aenean et tortor. Lorem dolor, sed viverra ipsum nunc aliquet bibendum? ",
      91,
      "!bg-green-200 !text-black"
    ),
    new Testimonial(
      "public/images/image-jonathan.jpg",
      "Aragorn",
      "  Eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada bibendum! ",
      "  Egestas fringilla phasellus faucibus scelerisque eleifend! Dignissim enim, sit amet venenatis urna cursus eget nunc scelerisque viverra mauris, in aliquam sem fringilla ut morbi tincidunt augue interdum velit euismod in! ",
      55,
      "!bg-blue-500"
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
      55,
      "!bg-moderate-violet"
    ),
    new Testimonial(
      "public/images/image-jonathan.jpg",
      "Jonathan Walters",
      "  The team was very supportive and kept me motivated ",
      """      "  Egestas fringilla phasellus faucibus scelerisque eleifend! Dignissim enim, sit amet venenatis urna cursus eget nunc scelerisque viverra mauris, in aliquam sem fringilla ut morbi tincidunt augue interdum velit euismod in! ",
""",
      55,
      "!bg-very-dark-grayish-blue"
    ),
    new Testimonial(
      "public/images/image-jeanette.jpg",
      "Jeanette Harmon",
      "An overall wonderful and rewarding experience",
      """  “ Thank you for the wonderful experience! I now have a job I really enjoy, and make a good living
  while doing something I love. ”
""",
      55,
      "!bg-white !text-very-dark-blackish-blue"
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
      55,
      "!bg-bg-very-dark-blackish-blue"
    ),
    new Testimonial(
      "public/images/image-kira.jpg",
      "Kira Whittle",
      "  Such a life-changing experience. Highly recommended! ",
      """      "  Egestas fringilla phasellus faucibus scelerisque eleifend! Dignissim enim, sit amet venenatis urna cursus eget nunc scelerisque viverra mauris, in aliquam sem fringilla ut morbi tincidunt augue interdum velit euismod in! ",
""",
      55,
      "!bg-white !text-very-dark-blackish-blue"
    )
  )

}
