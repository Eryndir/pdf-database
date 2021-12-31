import scala.collection.mutable.PriorityQueue
class TagList:
  val pQueue = new PriorityQueue[String]()(Ordering.by(t => t))

  def addList(tList: List[String]) = tList.foreach(t => pQueue += t)

  def getQuery: String = pQueue.mkString("%")
