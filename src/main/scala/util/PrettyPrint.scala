package util

object PrettyPrint {
  def pp[A](x: A): Unit = {
    print(x.toString + " ")
  }
}
