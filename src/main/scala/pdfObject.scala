import scala.compiletime.ops.boolean
case class pdfObject(
    name: String = "N/A",
    source: String = "N/A",
    driveLink: String = "N/A",
    genre: String = "N/A",
    tags: List[String] = List(),
    pageNumber: Int = -1,
    rating: Double = -1,
    read: Boolean = false,
    favourite: Boolean = false,
    extraMaterial: String = "N/A",
    category: Category,
    categoryInfo: List[String] = List()
):
  def tagsToString: String = tags.mkString(", ")

  def categorySpecifics: String =
    val zippedTouples = category.header.zip(categoryInfo)
    val zippedSingle = zippedTouples
      .map { case (header, info) =>
        s"$header: $info"
      }
    zippedSingle.mkString(", ")

  override def toString =
    s"$name \n| Source: $source, drivelink: $driveLink\n" +
      s"| Genre: $genre, Tags: $tagsToString\n" +
      s"| Pages: $pageNumber, Rating: $rating\n" +
      s"| Read: $read, Favourite: $favourite\n" +
      s"| Extra material: $extraMaterial\n" +
      s"| $categorySpecifics";

enum Category:
  case CollsCampaigns

  override def toString: String =
    this match
      case CollsCampaigns => "Adventure Collections and Campaigns"

  def header: List[String] =
    this match
      case CollsCampaigns => List("No. of", "Levelrange")
