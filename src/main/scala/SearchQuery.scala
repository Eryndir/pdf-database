case class SearchQuery(
    name: String = "",
    genre: String = "",
    tags: List[String] = List(),
    pageNumbers: Int = 0,
    rating: String = "",
    read: Boolean = false,
    favourite: Boolean = false,
    category: Category = Category.Uncategorized,
    rpg: String = ""
):
  def tagsInString = tags.mkString("\n")
