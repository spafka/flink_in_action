List(1, 2, 3, 4) map { i => println(i); i + 1 }
val v1=List(1, 2, 3, 4).view.map { i => println(i); i + 1 }
v1.toList