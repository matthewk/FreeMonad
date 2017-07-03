package utils

/**
  * A set of helpers to extract out the writing to console as we run each
  * application with its variants (Future and State Monad based)
  */
trait ApplicationWrapper {
  def application(appName: String)(f: => Unit): Unit = {
    println("--------------------------------------------------------")
    println(s"Executing $appName \n")
    f
  }

  def appVariantExecution(variant: String)(f: => Unit): Unit = {
    println(s"\n------- as $variant ------")
    println(s"Completed result ==> ")
    f
  }

  def appLogViewer(f: => Unit): Unit = {
    println("\nWould have written the following to log ==>")
    f
  }
}

