//> using dep org.scalameta::munit::1.0.0-M1


class MyTests extends munit.FunSuite {
  test("test") {
    val x = 2
    assertEquals(x, 2)
  }
}
