case class PdfObject(
    name: String = "N/A Name",
    source: String = "N/A Source",
    driveLink: String = "N/A driveLink",
    genre: String = "N/A genre",
    tags: TagList = new TagList,
    pageNumbers: Int = 0,
    rating: String = "N/A rating",
    read: Boolean = false,
    favourite: Boolean = false,
    extraMaterial: String = "N/A extraMaterial",
    category: Category = Category.Uncategorized,
    categoryInfo: List[String] = List(), // BUILDER PATTERN?
    rpg: String = "System Neutral",
    description: String = "N/A description"
):

  def categorySpecifics: String =
    if (category.header == List()) {
      ""
    } else {
      val zippedSingle = category.header
        .zip(categoryInfo)
        .map { case (header, info) =>
          s"$header: $info"
        }
        .mkString("\n")
      s"\n| $zippedSingle"
    }

  def categoryName: String = category.title

  override def toString =
    s"$name - $categoryName\n" +
      s"| $rpg\n" +
      s"| $description\n" +
      s"| Source: $source\n" +
      s"| drivelink: $driveLink\n" +
      s"| Genre: $genre, Tags: ${tags.pQueue.mkString(", ")}\n" +
      s"| Pages: $pageNumbers, Rating: $rating\n" +
      s"| Read: $read, Favourite: $favourite\n" +
      s"| Extra material: $extraMaterial" +
      s"$categorySpecifics";

  def toStringSmall =
    s"$name - $categoryName\n"

  def tagsInString = tags.pQueue.mkString("\n")
