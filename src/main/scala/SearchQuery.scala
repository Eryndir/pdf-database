case class SearchQuery(
    name: String = "",
    genre: String = "",
    tags: List[String] = List(),
    pageNumbers: Int = 0,
    rating: String = "",
    read: (Boolean, Boolean) = (false, false),
    favourite: (Boolean, Boolean) = (false, false),
    category: (Category, Boolean) = (Category.Uncategorized, false),
    rpg: String = ""
):
  def tagsInString = tags.mkString("\n")
